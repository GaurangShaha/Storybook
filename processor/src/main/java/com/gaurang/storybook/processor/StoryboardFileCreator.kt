package com.gaurang.storybook.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies

class StoryboardFileCreator(
    codeGenerator: CodeGenerator,
    packageName: String,
    fileName: String,
    functionNames: Sequence<String>,
    functionDetails: Sequence<String>
) {
    init {
        codeGenerator.createNewFile(
            dependencies = Dependencies(false),
            packageName = packageName,
            fileName = fileName
        ).use {
            it.write(
                String.format(
                    """package %s

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun %s() {
    val componentList = remember {
        listOf(
            %s
        )
    }

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            var selectedComponentIndex by remember { mutableIntStateOf(-1) }
            var query by remember { mutableStateOf("") }
            val filteredComponentList by remember {
                derivedStateOf { componentList.filter { it.contains(query, true) } }
            }

            Column {
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                    value = query,
                    onValueChange = { query = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { query = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    },
                    placeholder = { Text(text = "Enter component name to search") },
                    singleLine = true
                )

                ComponentList(filteredComponentList) {
                    selectedComponentIndex = componentList.indexOf(it)
                }
            }

            if (selectedComponentIndex != -1) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = componentList[selectedComponentIndex],
                            style = MaterialTheme.typography.subtitle1
                        )
                        ComponentDetails(selectedComponentIndex) { selectedComponentIndex = -1 }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComponentDetails(index: Int, onBackPress: () -> Unit) {
    when (index) {
        %s
    }
    BackHandler(onBack = onBackPress)
}

@Composable
private fun ComponentList(components: List<String>, onComponentClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(components) { component ->
            Row(
                modifier = Modifier
                    .clickable { onComponentClick(component) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(1f), text = component)

                Image(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }

        }
    }
}""",
                    packageName,
                    fileName,
                    functionNames.joinToString(separator = ",\n"),
                    functionDetails.joinToString("\n")
                ).toByteArray()
            )
        }
    }
}
