plugins {
    idea
    java
    id("gg.essential.loom") version "1.3.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    // id("com.github.johnrengelman.shadow") version "7.1.2"
}

val modGroup: String by project
val modID: String by project
group = modGroup
version = "0.3.0-BETA"

val tweakClass: String by project
val mixinConfig: String by project

loom {
    runs.all {
        property("mixin.debug.export", "true")
        property("asmhelper.verbose", "true")
        programArgs("--tweakClass", tweakClass)
        programArgs("--mixin", "mixins.${modID}.json")
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

sourceSets.main {
    output.resourcesDir = file("$buildDir/classes/java/main")
}

// val shade: Configuration by configurations.creating {
//     configurations.implementation.get().extendsFrom(this)
// }

repositories {
    maven("https://maven.cleanroommc.com/")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.properties["mcVersion"]}")
    mappings("de.oceanlabs.mcp:${project.properties["mappings"]}")
    forge("net.minecraftforge:forge:${project.properties["forgeVersion"]}")

    annotationProcessor("org.ow2.asm:asm-debug-all:5.2")
    annotationProcessor("com.google.guava:guava:32.1.2-jre")
    annotationProcessor("com.google.code.gson:gson:2.8.9")

    implementation("zone.rong:mixinbooter:8.9") {
        isTransitive = false
    }
    annotationProcessor("zone.rong:mixinbooter:8.9") {
        isTransitive = false
    }

    // Workaround for Loom bug
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
    jar {
        manifest.attributes(
            mapOf(
                "ForceLoadAsMod" to true,
                "FMLAT" to "${modID}_at.cfg",
                "TweakClass" to tweakClass,
                "MixinConfigs" to mixinConfig
            )
        )
        // dependsOn(shadowJar)
    }
    // remapJar {
    //     inputFile.set(shadowJar.get().archiveFile)
    // }
    // shadowJar {
    //     configurations = listOf(shade)
    //     mergeServiceFiles()
    // }
}

tasks.assemble.get().dependsOn(tasks.remapJar)
