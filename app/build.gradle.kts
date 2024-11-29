plugins {

}
group = rootProject.group
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(project(":api"))
//    implementation("io.insert-koin:koin-core:3.1.2")
    implementation("com.google.code.gson:gson:2.11.0")


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

