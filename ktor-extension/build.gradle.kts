@file:Suppress("PropertyName")

val kotlin_version: String by project
val ktor_version: String by project

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
