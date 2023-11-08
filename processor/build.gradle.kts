plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

group = "com.gaurang.storybook"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.gaurang"
            artifactId = "storybook"
            version = project.version.toString()

            from(components["java"])
        }
    }
}