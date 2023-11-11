plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "zottaa.com.github"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}