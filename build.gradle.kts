plugins {
	idea
	java
	id("gg.essential.loom") version "1.2.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val modGroup: String by project
val modBaseName: String by project
version = "0.1.2"
val modID = "paperfixes"
base.archivesName.set(modBaseName)

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

val shadowImplementation: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

repositories {
    maven("https://repo.spongepowered.org/maven/")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:1.12.2")
    mappings("de.oceanlabs.mcp:mcp_snapshot:20171003-1.12")
    forge("net.minecraftforge:forge:1.12.2-14.23.5.2840")

    shadowImplementation("com.github.LlamaLad7:MixinExtras:0.1.1")
    annotationProcessor("com.github.LlamaLad7:MixinExtras:0.1.1")
    shadowImplementation("org.spongepowered:mixin:0.8.5") {
        isTransitive = false
    }
    annotationProcessor("org.spongepowered:mixin:0.8.5")

    // Workaround loom 1.2 bug
    annotationProcessor("com.google.guava:guava:21.0")
    annotationProcessor("com.google.code.gson:gson:2.2.4")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.processResources {
    rename("(.+_at.cfg)", "META-INF/$1")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.flatMap { it.archiveFile })
}

tasks.shadowJar {
    configurations = listOf(shadowImplementation)
    mergeServiceFiles()
    relocate("com.llamalad7.mixinextras", "$modGroup.mixinextras")
    exclude("LICENSE_MixinExtras", "LICENSE.txt")
    exclude("**/module-info.class")
}

tasks.jar {
    archiveClassifier.set("dev")
    manifest.attributes(mapOf(
        "FMLAT" to "${modID}_at.cfg",
        "TweakClass" to tweakClass,
        "MixinConfigs" to mixinConfig
    ))
}