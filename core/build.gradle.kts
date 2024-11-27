plugins {
}

group = rootProject.group
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    implementation("com.google.code.gson:gson:2.11.0")
}
