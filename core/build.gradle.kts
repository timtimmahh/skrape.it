@file:Suppress("PropertyName")

val kotlin_version = "1.4.10"
val ktor_version = "1.5.2"

plugins {
    jacoco
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

dependencies {
    val jsoupVersion = "1.13.1"
    val htmlUnitVersion = "2.44.0"
    val kohttpVersion = "0.12.0"
    val testContainersVersion = "1.15.0-rc2"
    val wireMockVersion = "2.27.2"
    val log4jOverSlf4jVersion = "1.7.30"
    val logbackVersion = "1.2.3"

    implementation("org.jsoup:jsoup:$jsoupVersion")
    implementation("net.sourceforge.htmlunit:htmlunit:$htmlUnitVersion") {
        exclude("org.eclipse.jetty.websocket") // avoid android crash; see #93
    }
    implementation("io.github.rybalkinsd:kohttp:$kohttpVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")

    testImplementation("com.github.tomakehurst:wiremock-jre8:$wireMockVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("org.slf4j:log4j-over-slf4j:$log4jOverSlf4jVersion")
    testImplementation("io.ktor:ktor-client-core:$ktor_version")
    testImplementation("io.ktor:ktor-client-apache:$ktor_version")
}
