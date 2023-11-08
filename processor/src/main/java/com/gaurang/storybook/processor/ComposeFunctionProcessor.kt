package com.gaurang.storybook.processor

import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate

private const val PREVIEW_ANNOTATION = "androidx.compose.ui.tooling.preview.Preview"

class ComposeFunctionProcessor(
    private val codeGenerator: CodeGenerator, private val logger: KSPLogger, private val options: Map<String, String>
) : SymbolProcessor {


    override fun process(resolver: Resolver): List<KSAnnotated> {
        val previewAnnotation = mutableListOf(PREVIEW_ANNOTATION)
        options["extendedPreviews"]?.split(",")?.let { previewAnnotation.addAll(it) }

        val previewFunctions = sequence {
            previewAnnotation.forEach { annotation ->
                @Suppress("UNCHECKED_CAST")
                yieldAll(resolver.getSymbolsWithAnnotation(annotation)
                    .filter { ksAnnotated ->
                        ksAnnotated is KSFunctionDeclaration && (ksAnnotated.isPublic() || ksAnnotated.isInternal()) && ksAnnotated.validate()
                    } as Sequence<KSFunctionDeclaration>)
            }
        }

        if (!previewFunctions.iterator().hasNext()) return emptyList()

        val moduleName = resolver.moduleName()

        StoryboardFileCreator(
            codeGenerator = codeGenerator,
            packageName = "com.gaurang.storybook.${moduleName.toPackageName()}",
            fileName = "${moduleName.toFileName()}Storybook",
            functionNames = previewFunctions.map {
                "\"${it.simpleName.getShortName().replace("Preview", "").toSpaceSeparatedWords()}\""
            }, functionDetails = previewFunctions.mapIndexed { index, it ->
                "$index -> ${it.qualifiedName?.asString()}()"
            }
        )

        return previewFunctions.filterNot { it.validate() }.toList()
    }

    private fun String.toSpaceSeparatedWords(): String {
        return buildString {
            this@toSpaceSeparatedWords.forEachIndexed { index, it ->
                if (it.isUpperCase() && index != 0) {
                    append(" ")
                }
                append(it)
            }
        }
    }

    private fun Resolver.moduleName(): String {
        val moduleDescriptor = this::class.java.getDeclaredField("module").apply { isAccessible = true }.get(this)
        val rawName = moduleDescriptor::class.java.getMethod("getName").invoke(moduleDescriptor).toString()
        return rawName.removeSurrounding("<", ">")
            .replace("_debug", "")
            .replace("_release", "")
    }

    private fun String.toFileName(): String {
        var capitalizeNextChar = true
        return buildString {
            this@toFileName.forEach { c ->
                if (c == '-' || c == '_') {
                    capitalizeNextChar = true
                } else {
                    if (capitalizeNextChar) {
                        append(c.uppercase())
                        capitalizeNextChar = false
                    } else {
                        append(c)
                    }
                }
            }
        }
    }

    private fun String.toPackageName(): String {
        return buildString {
            replace('-', '.').replace('_', '.').forEachIndexed { index, c ->
                if (c.isUpperCase()) {
                    if (index != 0) {
                        append(".")
                    }
                    append(c.lowercase())
                } else {
                    append(c)
                }
            }
        }
    }
}
