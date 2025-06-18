plugins {
    idea
    java
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

configurations.implementation {
    extendsFrom(configurations.getByName("shadow"))
}

repositories {
    mavenCentral()
}

dependencies {
    shadow("com.lmax:disruptor:3.4.4")
}

val javaTarget = "8"
java {
    withSourcesJar()

    val javaVer = JavaVersion.toVersion(javaTarget)
    sourceCompatibility = javaVer
    targetCompatibility = javaVer
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaTarget))
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
    }
    shadowJar {
        configurations = listOf(project.configurations.getByName("shadow"))
        mergeServiceFiles()
        archiveClassifier = "shadow"
    }
    jar {
        duplicatesStrategy = DuplicatesStrategy.WARN
        dependsOn(shadowJar)
    }
}
