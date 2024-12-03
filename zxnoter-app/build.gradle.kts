plugins {

}
group = rootProject.group
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":zxnoter-core"))
    implementation(project(":zxnoter-core-api"))
    implementation(project(":zxnoter-core-utils"))
//    implementation("com.google.code.gson:gson:2.11.0")


}

tasks.shadowJar {
    destinationDirectory.set(Config.runtimeDir)
    archiveFileName.set("zxnoter.jar")
    manifest {
        attributes(
            "Main-Class" to "${project.group}.app.Main"
        )
    }
}

