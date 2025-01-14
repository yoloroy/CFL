plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.yoloroy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Lexer"))
    implementation(project(":Parser"))
    implementation(project(":Interpreter"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}