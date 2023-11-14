plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    kotlin("jvm") version "1.9.20"
}

group = "zottaa.com.github"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}
kotlin {
    jvmToolchain(20)
}