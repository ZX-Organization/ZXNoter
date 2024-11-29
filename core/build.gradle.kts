plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = rootProject.group
version = "1.0-SNAPSHOT"


dependencies {
    compileOnly(project(":api"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("org.slf4j:slf4j-api:2.0.0")
}

tasks.shadowJar {
    destinationDirectory.set(Config.runtimeDir)
    archiveFileName.set("zxnoter-core.jar")
}
