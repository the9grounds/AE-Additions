import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.jvm.tasks.Jar
import net.darkhax.curseforgegradle.Constants as CFG_Constants

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://repo.spongepowered.org/maven")
    }

    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "5.1.+") {
            isChanging = true
        }

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    kotlin("jvm") version "1.6.10"
    java
    id("net.darkhax.curseforgegradle") version "1.0.10"
}

apply {
    plugin("net.minecraftforge.gradle")
    plugin("idea")
}

apply(plugin = "org.spongepowered.mixin")

// Properties
val kotlinVersion: String by project
val minecraftVersion: String by project
val mcpChannel: String by project
val mcpMappings: String by project
val forgeVersion: String by project
val aeVersion: String by project
val hwylaVersion: String by project
val jeiVersion: String by project
val mekanismVersion: String by project
val wthitVersion: String by project
val cofhVersion: String by project
val curseForgeProjectId: String by project
val modBaseName: String by project
val modCurseId: String by project

apply(from= "https://raw.githubusercontent.com/thedarkcolour/KotlinForForge/site/thedarkcolour/kotlinforforge/gradle/kff-3.1.0.gradle")

project.group = "com.the9grounds.aeadditions"
base.archivesBaseName = "AEAdditions-${minecraftVersion}"

configure<UserDevExtension> {
    mappings(mcpChannel, mcpMappings)
    
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            args("-mixin.config=ae2additions.mixins.json")

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

        }
        create("server") {
            workingDirectory(project.file("run"))
            args("-mixin.config=ae2additions.mixins.json")

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
        }
        create("data") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            args("--mod", "ae2additions", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources"), "-mixin.config=ae2additions.mixins.json")
        }
    }
}

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
}

val coroutines_version = "1.6.0"
dependencies {
    "minecraft"("net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}")
    val jeiApi = project.dependencies.create(group = "mezz.jei", name = "jei-${minecraftVersion}", version = jeiVersion, classifier = "api")
    val jei = project.dependencies.create(group = "mezz.jei", name = "jei-${minecraftVersion}", version = jeiVersion)
    val ae2 = project.dependencies.create(group = "appeng", name = "appliedenergistics2", version = aeVersion)

    compileOnly(project.the<DependencyManagementExtension>().deobf(jeiApi))
    runtimeOnly(project.the<DependencyManagementExtension>().deobf(jei))

    implementation(project.the<DependencyManagementExtension>().deobf(ae2))

    implementation(project.the<DependencyManagementExtension>().deobf("mekanism:Mekanism:${mekanismVersion}"))
    implementation(project.the<DependencyManagementExtension>().deobf("curse.maven:applied-mekanistics-574300:3797910"))
    implementation(project.the<DependencyManagementExtension>().deobf("curse.maven:ae2-things-forge-609977:3795991"))

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("org.spongepowered:mixin:0.8.5") { isTransitive = false }
    
}

val Project.minecraft: UserDevExtension
    get() = extensions.getByName<UserDevExtension>("minecraft")

tasks.withType<Jar> {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", getBetterVersion())
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE

    baseName = "${modBaseName}-${getBetterVersion()}"
    
    manifest.attributes(
        "MixinConfigs" to "ae2additions.mixins.json",
    )

    // replace stuff in mcmod.info, nothing else
    filesMatching("META-INF/mods.toml") {
        expand(mapOf(
            "version" to getBetterVersion(),
            "mcversion" to "1.18.2"
        ))
        filter { line ->
            line.replace("version=\"0.0.0.0.1\"", "version=\"${getBetterVersion()}\"")
        }
    }
}

tasks.register<TaskPublishCurseForge>("publishCurseForge") {
    apiToken = System.getenv("CURSEFORGE_API_KEY")
    
    val fileName = "${modBaseName}-${getBetterVersion()}"
    
    val mainFile = upload(modCurseId, file("${project.buildDir}/libs/${fileName}.jar"))
    mainFile.addGameVersion(minecraftVersion)
    mainFile.changelogType = CFG_Constants.CHANGELOG_MARKDOWN
    mainFile.changelog = file("CHANGELOG.md")
    mainFile.releaseType = getReleaseType()
    mainFile.addRequirement("applied-energistics-2")
    mainFile.addRequirement("kotlin-for-forge")
    mainFile.addOptional("mekanism")
    mainFile.addOptional("applied-mekanistics")
    mainFile.addModLoader("Forge")
}

fun getBuildNumber(): String? {

    if (System.getenv("CI") == null) {
        return "0.0.0.1"
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

tasks.create("copyResourceToClasses", Copy::class) {
    tasks.classes.get().dependsOn(this)
    dependsOn(tasks.processResources.get())
    onlyIf { gradle.taskGraph.hasTask(tasks.getByName("prepareRuns")) }
    into("$buildDir/classes/kotlin/main")
    from(tasks.processResources.get().destinationDir)
}

configure<org.spongepowered.asm.gradle.plugins.MixinExtension> {
    add(sourceSets.main.get(), "ae2additions.refmap.json")
    config("ae2additions.mixins.json")
    quiet = false
}