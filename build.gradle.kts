plugins {
    idea
    java
    id("gg.essential.loom") version ("1.9.31")
    id("dev.architectury.architectury-pack200") version ("0.1.3")
}

val modGroup: String by project
val modID: String by project
group = modGroup
version = "2.0.0-SNAPSHOT"

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
        useLegacyMixinAp.set(true)
        defaultRefmapName.set("$modID-refmap.json")
    }
}

sourceSets.main {
    output.dir("${layout.buildDirectory.get()}/classes/java/main")
}

repositories {
    maven("https://maven.cleanroommc.com")
    maven("https://repo.spongepowered.org/repository/maven-public")
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["mcVersion"]}")
    mappings("de.oceanlabs.mcp:${properties["mappings"]}")
    forge("net.minecraftforge:forge:${properties["forgeVersion"]}")

    annotationProcessor("com.google.guava:guava:33.2.1-jre")
    annotationProcessor("com.google.code.gson:gson:2.10")

    implementation("zone.rong:mixinbooter:10.6")
    annotationProcessor("org.spongepowered:mixin:0.8.7")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0-rc.1")
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
    jar {
        manifest.attributes(
            "FMLCorePlugin" to ("$modGroup.core.PFLoadingPlugin"),
            "FMLCorePluginContainsFMLMod" to "true",
            "FMLAT" to "${modID}_at.cfg",
            "ForceLoadAsMod" to "true",
        )
        duplicatesStrategy = DuplicatesStrategy.WARN
    }
}

tasks.assemble.get().dependsOn(tasks.remapJar)
