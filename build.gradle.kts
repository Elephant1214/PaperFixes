plugins {
	idea
	java
	id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "0.1.0-beta"

loom {
    launchConfigs {
        "client" {
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            arg("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
            arg("--mixin", "paperfixes.mixins.json")
        }
        "server" {
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            arg("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
            arg("--mixin", "paperfixes.mixins.json")
        }
    }
    runConfigs {
        "client" {
            isIdeConfigGenerated = true
        }
        "server" {
            isIdeConfigGenerated = true
        }
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig("paperfixes.mixins.json")
    }
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("paperfixes-refmap.json")
    }
}

sourceSets.main {
    output.setResourcesDir(file("$buildDir/classes/java/main"))
}

val shadowImplementation: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

repositories {
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.12.2")
    mappings("de.oceanlabs.mcp:mcp_snapshot:20171003-1.12")
    forge("net.minecraftforge:forge:1.12.2-14.23.5.2840")

    shadowImplementation("org.spongepowered:mixin:0.8.5") {
        isTransitive = false
    }
    annotationProcessor("org.spongepowered:mixin:0.8.5")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.withType(Jar::class) {
    archiveBaseName.set("PaperFixes")
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"
        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["MixinConfigs"] = "paperfixes.mixins.json"
    }
}

val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("dep")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
    doLast {
        println("Jar name: ${archiveFile.get().asFile}")
    }
}

tasks.shadowJar {
    archiveClassifier.set("dep-dev")
    configurations = listOf(shadowImplementation)
    exclude("**/module-info.class")
}