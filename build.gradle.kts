plugins {
    kotlin("jvm") version "1.9.0"
    id("com.google.devtools.ksp")
}

group = "com.gaurang.storybook"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

ksp {
    arg("extendedPreviews", "com.gaurang.demo.preview.MyPreview")
}
dependencies {
    ksp(project(":processor"))
}

kotlin {
    jvmToolchain(8)
}