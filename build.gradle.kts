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
//        implementation("com.google.code.gson:gson:2.8.9")
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

task("buildApi") {
    group = "zxnoter"
    description = "Build the api module"
    dependsOn(":app:jar")
}
task("buildCore") {
    group = "zxnoter"
    description = "Build the core module"
    dependsOn(":core:shadowJar")
}
task("buildExtensionExample") {
    group = "zxnoter"
    description = "Build the core module"
    dependsOn(":extensions:extension-example:shadowJar")
}
task("buildAll") {
    group = "zxnoter"
    description = "Build all module"
    dependsOn(":buildApp", ":buildApi", ":buildCore", ":buildExtensionExample")
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
                chcp 65001 && "${javaHome}/bin/java" -Xbootclasspath/a:"${Config.runtimeDir}/zxnoter-api.jar";"${Config.runtimeDir}/zxnoter-core.jar" -jar "$jarPath"
            """.trimIndent()
            )
        }
    }
}
