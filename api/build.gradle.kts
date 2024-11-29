plugins {
}

group = rootProject.group
version = "1.0-SNAPSHOT"



dependencies {

}



tasks.jar {
    destinationDirectory.set(Config.runtimeDir)
    archiveFileName.set("zxnoter-api.jar")
}

