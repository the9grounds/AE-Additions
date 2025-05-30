import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.darkhax.curseforgegradle.Constants as CFG_Constants

plugins {
    java
    idea
    `maven-publish`
    id("net.neoforged.moddev") version "1.0.11"
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("net.darkhax.curseforgegradle") version "1.0.10"
    id("com.modrinth.minotaur") version "2.+"
}

neoForge {
    version = project.extra["neoForgeVersion"]!!.toString()
    accessTransformers {
        file("src/main/resources/META-INF/accesstransformer.cfg")
    }

    parchment {
        mappingsVersion = project.extra["parchmentMappingsVersion"]!!.toString()
        minecraftVersion = project.extra["parchmentMinecraftVersion"]!!.toString()
    }

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel.set(org.slf4j.event.Level.DEBUG)
        }

        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["modId"]!!.toString())
            systemProperty("guideme.ae2.guide.sources", file("src/main/resources/assets/ae2additions/ae2guide").absolutePath)
            systemProperty("guideme.ae2.guide.sourcesNamespace", project.extra["modId"]!!.toString())
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["modId"]!!.toString())
        }

        create("gameTestServer") {
            type.set("gameTestServer")
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["modId"]!!.toString())
        }

        create("data") {
            data()
            programArguments.addAll(
                listOf(
                    "--mod", project.extra["modId"]!!.toString(),
                    "--all",
                    "--output", file("src/generated/resources/").absolutePath,
                    "--existing", file("src/main/resources/").absolutePath
                )
            )
        }
    }

    mods {
        create(project.extra["modId"]!!.toString()) {
            sourceSet(sourceSets["main"])
        }
    }
}

configurations {
    create("localRuntime")
    getByName("runtimeClasspath").extendsFrom(getByName("localRuntime"))
}

repositories {
    jcenter()
    mavenCentral()
    maven { // TOP
        url = uri("https://maven.k-4u.nl")
    }

    maven {
        name = "Modmaven"
        url = uri("https://modmaven.dev/")
    }

    maven {
        url = uri("https://www.cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }

    maven {
        name = "kotlinforforge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
    
    maven {
        url = uri("https://maven.bai.lol")
    }
    maven {
        url = uri("https://repo.spongepowered.org/maven")
    }
    maven {
        url = uri("https://squiddev.cc/maven/")
        content {
            includeGroup("org.squiddev")
        }
    }
}

val coroutines_version = "1.8.22"
dependencies {
    val jeiApi = project.dependencies.create(group = "mezz.jei", name = "jei-${project.extra["mcVersion"]}-neoforge", version = project.extra["jeiVersion"].toString(), classifier = "api")
    val jei = project.dependencies.create(group = "mezz.jei", name = "jei-1.21.1-neoforge", version = project.extra["jeiVersion"].toString())
    val ae2 = project.dependencies.create(group = "org.appliedenergistics", name = "appliedenergistics2", version = project.extra["aeVersion"].toString())

//    compileOnly(project.the<DependencyManagementExtension>().deobf(jeiApi))
    implementation(jei)

    implementation(ae2)

    implementation("thedarkcolour:kotlinforforge-neoforge:5.8.0")

    implementation("mekanism:Mekanism:${project.extra["mekanismVersion"]!!.toString()}")
    implementation("curse.maven:applied-mekanistics-574300:5978711")
    implementation("curse.maven:ae2-things-forge-609977:5637783")
//    implementation(project.the<DependencyManagementExtension>().deobf("curse.maven:the-one-probe-245211:3927520"))
    compileOnly("curse.maven:ftb-teams-404468:6119437")
    compileOnly("org.appliedenergistics:guideme:${project.extra["guideMeVersion"]!!}:api")
    runtimeOnly("org.appliedenergistics:guideme:${project.extra["guideMeVersion"]!!}")



//    compileOnly(project.the<DependencyManagementExtension>().deobf("curse.maven:applied-botanics-610632:3770580"))
//    compileOnly(project.the<DependencyManagementExtension>().deobf("curse.maven:applied-botanics-610632:3770580"))
    
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to project.extra["mcVersion"]!!,
        "minecraft_version_range" to project.extra["minecraftVersionRange"]!!,
        "neo_version" to project.extra["neoForgeVersion"]!!,
        "neo_version_range" to project.extra["neoVersionRange"]!!,
        "loader_version_range" to project.extra["loaderVersionRange"]!!,
        "mod_id" to project.extra["modId"]!!,
        "mod_name" to project.extra["modName"]!!,
        "mod_license" to project.extra["modLicense"]!!,
        "mod_version" to getBetterVersion(),
        "mod_authors" to project.extra["modAuthors"]!!,
        "mod_description" to project.extra["modDescription"]!!
    )

    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

val fileName = "${project.extra["modBaseName"]}-${project.extra["mcVersion"]}-${getBetterVersion()}"

base.archivesName.set(fileName)

tasks.register<TaskPublishCurseForge>("publishCurseForge") {
    apiToken = System.getenv("CURSEFORGE_API_KEY")

    val mainFile = upload(project.extra["modCurseId"], file("${project.buildDir}/libs/${fileName}.jar"))
    mainFile.addGameVersion(project.extra["mcVersion"])
    mainFile.changelogType = CFG_Constants.CHANGELOG_MARKDOWN
    mainFile.changelog = file("CHANGELOG.md")
    mainFile.releaseType = getReleaseType()
    mainFile.addRequirement("applied-energistics-2")
    mainFile.addRequirement("kotlin-for-forge")
//    mainFile.addOptional("mekanism")
//    mainFile.addOptional("applied-mekanistics")
    mainFile.addModLoader("NeoForge")
}

val changeLogFile = file("CHANGELOG.md")
val changeLogContents = if (changeLogFile.exists()) changeLogFile.readText(Charsets.UTF_8) else ""

modrinth {
    token.set(System.getenv("MODRINTH_API_TOKEN"))
    projectId.set(project.extra["modrinthProjectId"].toString())
    uploadFile.set(file("${project.buildDir}/libs/${fileName}.jar"))
    loaders.add("neoforge")
    gameVersions.add(project.extra["mcVersion"].toString())
    versionNumber = getBetterVersion()
    versionName = fileName
    changelog.set(changeLogContents)
    versionType.set(getReleaseType())

    dependencies {
        required.project("ae2")
        required.project("kotlin-for-forge")
    }
}

fun getBuildNumber(): String? {

    if (System.getenv("CI") == null) {
        return "0.0.0.1"
    }

    if (System.getenv("TAG") != null) {
        return null
    }

    if (System.getenv("GITHUB_HEAD_REF") != null) {
        return "0.0.1-pr-${System.getenv("GITHUB_HEAD_REF")}-${System.getenv("SHORT_SHA")}"
    }

    return "0.0.1-ci-${System.getenv("BRANCH_NAME")}-${System.getenv("SHORT_SHA")}"
}

fun getBetterVersion(): String {
    val buildNumber = getBuildNumber()

    if (buildNumber == null) {
        val tag = System.getenv("TAG")

        return "${tag}"
    }

    return buildNumber
}

fun getReleaseType(): String {
    val preReleaseEnv = System.getenv("PRERELEASE")

    if (preReleaseEnv == null) {
        return "beta"
    }

    val preRelease = preReleaseEnv.toBoolean()

    if (preRelease) {
        return "beta"
    }

    return "release"
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("src/generated/resources")
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}