plugins {
    kotlin("jvm") version "1.9.0"
}

group = "com.gaurang.storybook"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")
}