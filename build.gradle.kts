plugins {
    id("java")
    id("scala")
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
    implementation("org.scala-lang:scala-library:2.13.12")
    implementation("org.scala-lang:scala-reflect:2.13.12")
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        kotlin {
            srcDirs("src/main/kotlin")
        }
        scala {
            srcDirs("src/main/scala")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    modules("javafx.controls", "javafx.fxml", "javafx.media")
}
kotlin {
    jvmToolchain(20)
}