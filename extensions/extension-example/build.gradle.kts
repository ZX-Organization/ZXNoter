plugins {
}

group = "org.zxnoter.example"
version = "1.0-SNAPSHOT"


dependencies {
    compileOnly(project(":zxnoter-core-api"))
    compileOnly(project(":zxnoter-core-utils"))
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
}

tasks.shadowJar {
    destinationDirectory.set(Config.extensionsDir)
    archiveFileName.set("example.jar")
}
