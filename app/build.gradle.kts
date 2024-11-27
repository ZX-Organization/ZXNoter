plugins {

}
group = rootProject.group
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":core"))
}

tasks.shadowJar {
    destinationDirectory.set(Config.runtimeDir)
    archiveFileName.set("zxnoter-app.jar")
    manifest {
        attributes(
            "Main-Class" to "${project.group}.app.Main"
        )
    }
}

