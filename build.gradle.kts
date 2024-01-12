plugins {
    idea
    java
    id("gg.essential.loom") version "1.3.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val modGroup: String by project
val modName: String by project
version = "0.2.0-BETA"
val modID = "paperfixes"

val tweakClass: String by project
val mixinConfig: String by project
val accessTransformer: String by project

loom {
    runs.all {
        property("mixin.debug.export", "true")
        property("asmhelper.verbose", "true")
        property("--tweakClass", tweakClass)
        property("--mixin", mixinConfig)
    }
    runConfigs.all {
        isIdeConfigGenerated = true
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig(mixinConfig)
        accessTransformer("src/main/resources/$accessTransformer")
    }
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("$modID-refmap.json")
    }
}

sourceSets.main {
    output.resourcesDir = file("$buildDir/classes/java/main")
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

repositories {
    maven("https://repo.spongepowered.org/maven/")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:1.12.2")
    mappings("de.oceanlabs.mcp:mcp_stable:39-1.12")
    forge("net.minecraftforge:forge:1.12.2-14.23.5.2840")

    shade("com.github.LlamaLad7:MixinExtras:0.1.1")
    annotationProcessor("com.github.LlamaLad7:MixinExtras:0.1.1")
    shade("org.spongepowered:mixin:0.8.5") {
        isTransitive = false
    }
    annotationProcessor("org.spongepowered:mixin:0.8.5")

    // Workaround loom 1.2 bug
    annotationProcessor("com.google.guava:guava:21.0")
    annotationProcessor("com.google.code.gson:gson:2.2.4")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
    options.release.set(8)
}

java {
    withSourcesJar()
}

tasks {
    processResources {
        rename("(.+_at.cfg)", "META-INF/$1")
    }
    jar {
        archiveClassifier.set("thin")
        manifest.attributes(
            mapOf(
                "ForceLoadAsMod" to true,
                "FMLAT" to "${modID}_at.cfg",
                "TweakClass" to tweakClass,
                "MixinConfigs" to mixinConfig
            )
        )
        dependsOn(shadowJar)
    }
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
    }
    shadowJar {
        archiveClassifier.set("dev")
        configurations = listOf(shade)
        relocate("com.llamalad7.mixinextras", "$modGroup.mixinextras")
        rename("LICENSE.txt", "LICENSE_Mixin.txt")
        exclude("module-info.class")
        mergeServiceFiles()
    }
}

tasks.assemble.get().dependsOn(tasks.remapJar)
