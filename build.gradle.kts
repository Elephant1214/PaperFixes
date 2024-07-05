plugins {
    idea
    java
    id("gg.essential.loom") version "1.6.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val modGroup: String by project
val modID: String by project
group = modGroup
version = "1.1.0"

loom {
    runs {
        all {
            programArgs("--tweakClass=${properties["tweakClass"]}")
            property("mixin.debug.export", "true")
        }
        getByName("server").runDir("runServer")
    }
    runConfigs.all {
        isIdeConfigGenerated = true
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfigs("paperfixes.mixins.json", "paperfixes.mixins.init.json")
        accessTransformer("src/main/resources/${properties["accessTransformer"]}")
    }
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("$modID-refmap.json")
    }
}

sourceSets.main {
    output.dir("${layout.buildDirectory.get()}/classes/java/main")
}

configurations.implementation {
    extendsFrom(configurations.getByName("shadow"))
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["mcVersion"]}")
    mappings("de.oceanlabs.mcp:${properties["mappings"]}")
    forge("net.minecraftforge:forge:${properties["forgeVersion"]}")

    annotationProcessor("com.google.guava:guava:33.2.1-jre")
    annotationProcessor("com.google.code.gson:gson:2.10")

    shadow("net.fabricmc:sponge-mixin:0.14.0+mixin.0.8.6")
    annotationProcessor("net.fabricmc:sponge-mixin:0.14.0+mixin.0.8.6")
    shadow("io.github.llamalad7:mixinextras-common:0.3.6")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.6")
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
        mergeServiceFiles()
        exclude(
            "module-info.class",
            "org/spongepowered/asm/launch/MixinLaunchPlugin.class",
            "org/spongepowered/asm/launch/MixinTransformationService.class",
            "org/spongepowered/asm/launch/platform/container/ContainerHandleModLauncherEx.class",
            "org/spongepowered/asm/launch/platform/container/ContainerHandleModLauncherEx\$SecureJarResource.class"
        )
    }
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
    }
    jar {
        manifest.attributes(
            "FMLCorePlugin" to ("$modGroup.core.PFLoadingPlugin"),
            "FMLCorePluginContainsFMLMod" to "true",
            "FMLAT" to "${modID}_at.cfg",
            "MixinConfigs" to "paperfixes.mixins.json, paperfixes.mixins.init.json",
            "ForceLoadAsMod" to "true",
        )
        duplicatesStrategy = DuplicatesStrategy.WARN
        dependsOn(shadowJar)
    }
}

tasks.assemble.get().dependsOn(tasks.remapJar)
