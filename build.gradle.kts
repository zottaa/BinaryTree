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
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.scala-lang:scala-library:2.13.12")
    implementation("org.scala-lang:scala-reflect:2.13.12")
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.7.2")
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
    test {
        kotlin {
            srcDirs("src/test/kotlin")
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