version = "1.0.0"

plugins {
    groovy
    application
    id("com.diffplug.spotless") version "6.9.1"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.codehaus.groovy:groovy:3.0.10")
    testImplementation("org.spockframework:spock-core:2.1-groovy-3.0")
    testImplementation("junit:junit:4.13.2")
}

application {
    mainClass.set("blackjack.Main")
}


tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
                         "Implementation-Version" to project.version,
                         "Main-Class" to application.mainClass))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat("1.15.0").aosp().groupArtifact("com.google.googlejavaformat:google-java-format")
    }
}

tasks.register("fmt") {
    description = "Format the Java code with google-java-format"
    doLast {
        project.exec {
            workingDir("src/main/java/blackjack")
            commandLine("/bin/sh", "-c", "google-java-format -a -i *.java")
        }
    }
}
