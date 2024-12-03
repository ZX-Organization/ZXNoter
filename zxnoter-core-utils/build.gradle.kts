plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
}

group = rootProject.group
version = "1.0-SNAPSHOT"



dependencies {
    implementation(project(":zxnoter-core"))
    implementation(project(":zxnoter-core-api"))
    /*    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
        api("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
        implementation("org.slf4j:slf4j-api:2.0.16")
        implementation("org.slf4j:slf4j-nop:2.0.16")*/
}

