@file:Suppress("PropertyName")

val kotlin_version = "1.4.10"
val ktor_version = "1.5.2"

plugins {
    jacoco
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

dependencies {

//    provided(project(":core"))
    implementation(project(":core"))
    testImplementation(project(":core"))
    provided("io.ktor:ktor-server-test-host:$ktor_version")
    provided("io.ktor:ktor-server-netty:$ktor_version")
    provided("io.ktor:ktor-freemarker:$ktor_version")
    provided("io.ktor:ktor-locations:$ktor_version")
}

fun DependencyHandlerScope.provided(dependencyNotation: Any) {
    compileOnly(dependencyNotation)
    testCompileOnly(dependencyNotation)
    runtimeOnly(dependencyNotation)
}
