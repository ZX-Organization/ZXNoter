plugins {
    kotlin("jvm")
}

group = "org.zxnoter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":api"))
}

tasks.test {
    useJUnitPlatform()
}