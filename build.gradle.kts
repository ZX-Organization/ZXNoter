plugins {
    kotlin("jvm") version "2.1.0-Beta2"
    id("com.gradleup.shadow") version "8.3.5"
}
group = "org.zxnoter"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}

val runtimeDir = file("${project.rootDir}/runtime")
val extensionsDir = file("${runtimeDir}/extensions")
val resourcepacksDir = file("${runtimeDir}/resourcepacks")
val logsDir = file("${runtimeDir}/logs")
val configDir = file("${runtimeDir}/config")
allprojects {
    ext["runtimeDir"] = runtimeDir
    ext["extensionsDir"] = extensionsDir
    ext["resourcepacksDir"] = resourcepacksDir
    ext["logsDir"] = logsDir
    ext["configDir"] = configDir
}
subprojects {
    repositories {
        mavenCentral()
    }
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.gradleup.shadow")

    tasks.test {
        useJUnitPlatform()
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
//        implementation("com.google.code.gson:gson:2.8.9")
        implementation("org.slf4j:slf4j-api:2.0.0")
//        implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
//        implementation("org.bytedeco:ffmpeg:6.1.1-1.5.10")
        testImplementation(kotlin("test"))
    }
}

allprojects {
    group = "org.zxnoter"
}

task("buildApp") {
    group = "zxnoter"
    description = "Build the app module"
    dependsOn(":app:shadowJar")
}
task("buildAll") {
    group = "zxnoter"
    description = "Build all module"
    dependsOn(":buildApp")
}
task("run") {
    group = "zxnoter"
    description = "run the app"
    dependsOn(":buildAll")
    val javaHome = project.findProperty("javaHome") ?: System.getProperty("java.home")
    val jarPath = "${Config.runtimeDir.absoluteFile}/zxnoter-app.jar"

    doLast {
        exec {
            commandLine(
                "cmd", "/c", """
                chcp 65001 && "${javaHome}/bin/java" -jar "$jarPath"
            """.trimIndent()
            )
        }
    }
}
