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
    dependsOn(":zxnoter-app:shadowJar")
}

task("buildExtensionExample") {
    group = "zxnoter"
    description = "Build the core module"
    dependsOn(":extensions:extension-example:shadowJar")
}
task("buildAll") {
    group = "zxnoter"
    description = "Build all module"
    dependsOn(":buildApp", ":buildExtensionExample")
}
task("run") {
    group = "zxnoter"
    description = "run the app"
    dependsOn(":buildAll")
    val javaHome = project.findProperty("javaHome") ?: System.getProperty("java.home")
    val jarPath = "${Config.runtimeDir.absoluteFile}/zxnoter.jar"

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
