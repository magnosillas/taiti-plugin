import org.jetbrains.intellij.tasks.RunIdeTask

plugins {
        id("java")
        id("org.jetbrains.intellij") version "1.15.0"

    }

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

    dependencies {
        testImplementation( "org.junit.jupiter:junit-jupiter-api:5.9.2")
        testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.9.2")

        // https://mvnrepository.com/artifact/com.mashape.unirest/unirest-java
        implementation ("com.mashape.unirest:unirest-java:1.4.9")
// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
        implementation ("com.squareup.okhttp3:okhttp:4.11.0")

        implementation (files("libs/taiti-conflicts.jar"))
    }

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.3.3")
    type.set("IU") // Target IDE Platform

}
val versaoJava = System.getProperty("java.version")
println("Versão do Java usada para a tarefa runIde: $versaoJava")

tasks.named("runIde", JavaExec::class) {
    doLast{
        val versaoJava = System.getProperty("java.version")
        println("Versão do Java usada para a tarefa runIde: $versaoJava")
    }
}
tasks {


    // Set the JVM compatibility versions


    patchPluginXml {
        sinceBuild.set("223")
        untilBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
