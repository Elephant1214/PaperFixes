plugins {
    idea
    java
    id("gg.essential.loom") version "1.3.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

val modGroup: String by project
val modID: String by project
group = modGroup
version = "0.4.0-BETA"

val tweakClass: String by project
val mixinConfig: String by project

loom {
    runs {
        all {
            programArgs("--tweakClass=$tweakClass")
            property("mixin.debug.export", "true")
        }
        getByName("server").runDir("runServer")
    }
    runConfigs.all {
        isIdeConfigGenerated = true
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig(mixinConfig)
        accessTransformer("src/main/resources/${project.properties["accessTransformer"]}")
    }
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("$modID-refmap.json")
    }
}

configurations.implementation {
    extendsFrom(configurations.getByName("shadow"))
}

repositories {
    maven("https://repo.spongepowered.org/maven/")
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.properties["mcVersion"]}")
    mappings("de.oceanlabs.mcp:${project.properties["mappings"]}")
    forge("net.minecraftforge:forge:${project.properties["forgeVersion"]}")

    annotationProcessor("com.google.guava:guava:32.1.2-jre")
    annotationProcessor("com.google.code.gson:gson:2.8.9")

    shadow("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")
    shadow("org.spongepowered:mixin:0.8.5")
    annotationProcessor("org.spongepowered:mixin:0.8.5")
}

tasks.withType<JavaCompile> {
    sourceCompatibility  = "1.8"
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

        project.projectDir.walkTopDown().forEach { file ->
            if (file.name in listOf("mcmod.info", "PaperFixes.java")) {
                println("Processing ${file.name}")
                var content = file.readText()
                content = content.replace(Regex("\"version\": \"[^\"]*\""), "\"version\": \"$version\"")
                content = content.replace(Regex("VERSION = \"[^\"]*\";"), "VERSION = \"$version\";")
                file.writeText(content)
            }
        }
        from(project.file("LICENSE")) { rename { "LICENSE_PaperFixes.txt" } }
    }
    shadowJar {
        configurations = listOf(project.configurations.getByName("shadow"))
        relocate("com.llamalad7.mixinextras", "$modGroup.mixinextras")
        exclude("module-info.class", "MixinLaunchPlugin.java", "MixinTransformationService.java", "ContainerHandleModLauncherEx.java")
        mergeServiceFiles()
    }
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
    }
    jar {
        manifest.attributes(
            "ForceLoadAsMod" to true,
            "FMLAT" to "${modID}_at.cfg",
            "TweakClass" to tweakClass,
            "MixinConfigs" to mixinConfig
        )
        duplicatesStrategy = DuplicatesStrategy.WARN
        dependsOn(shadowJar)
    }
}

tasks.assemble.get().dependsOn(tasks.remapJar)
