import net.darkhax.curseforgegradle.Constants as CFG_Constants

plugins {
    id("fabric-loom")
    val kotlinVersion: String = "1.7.20"
    kotlin("jvm").version(kotlinVersion)
    id("net.darkhax.curseforgegradle") version "1.0.10"
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}
val modVersion: String by project
version = getBetterVersion()
val mavenGroup: String by project
group = mavenGroup
repositories {
    jcenter()
    mavenCentral()
    maven {
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
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

    maven { url = uri("https://maven.bai.lol") }

    maven {
        url = uri("https://repo.spongepowered.org/maven")
    }
    maven { 
        url = uri("https://maven.shedaniel.me") 
    }
}
val aeVersion: String by project
val reiVersion: String by project
val modBaseName: String by project
val modCurseId: String by project
val minecraftVersion: String by project
dependencies {
    val minecraftVersion: String by project
    minecraft("com.mojang", "minecraft", minecraftVersion)
    val yarnMappings: String by project
    mappings(loom.officialMojangMappings())
    val loaderVersion: String by project
    modImplementation("net.fabricmc", "fabric-loader", loaderVersion)
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricVersion)
    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc", "fabric-language-kotlin", fabricKotlinVersion)
//    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:$reiVersion")
//    modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:$reiVersion")
    
    modImplementation("teamreborn", "energy", "2.2.0") {
        exclude(group = "net.fabricmc.fabric-api")
    }

//    include modApi('teamreborn:energy:<latest_version>') {
//        exclude(group: "net.fabricmc.fabric-api")
//    }

//    modImplementation("appeng:appliedenergistics2-fabric:$aeVersion")
//    modImplementation("curse.maven:ae2-things-563800:3796436")
    modImplementation("curse.maven:ae2-223794:4030069")

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    modCompileOnly("org.spongepowered:mixin:0.8.5") { isTransitive = false }
}
tasks.withType<Jar> {
    manifest.attributes(
        "MixinConfigs" to "ae2additions.mixins.json",
    )
}
tasks {
    val javaVersion = JavaVersion.VERSION_17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
//        sourceCompatibility = javaVersion.toString()
//        targetCompatibility = javaVersion.toString()
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        inputs.property("version", getBetterVersion())
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to getBetterVersion())) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}

tasks.register<net.darkhax.curseforgegradle.TaskPublishCurseForge>("publishCurseForge") {
    apiToken = System.getenv("CURSEFORGE_API_KEY")

    val fileName = "${modBaseName}-${getBetterVersion()}"

    val mainFile = upload(modCurseId, file("${project.buildDir}/libs/${fileName}.jar"))
    mainFile.addGameVersion(minecraftVersion)
    mainFile.changelogType = CFG_Constants.CHANGELOG_MARKDOWN
    mainFile.changelog = file("CHANGELOG.md")
    mainFile.releaseType = getReleaseType()
    mainFile.addRequirement("applied-energistics-2")
    mainFile.addRequirement("fabric-language-kotlin")
    mainFile.addOptional("ae2things")
    mainFile.addModLoader("Fabric")
}

fun getBuildNumber(): String? {

    if (System.getenv("CI") == null) {
        return "3.0.4"
    }

    if (System.getenv("TAG") != null) {
        return null
    }

    if (System.getenv("GITHUB_HEAD_REF") != null) {
        return "pr-${System.getenv("GITHUB_HEAD_REF")}-${System.getenv("SHORT_SHA")}"
    }

    return "ci-${System.getenv("BRANCH_NAME")}-${System.getenv("SHORT_SHA")}"
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
    
    return "release"

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