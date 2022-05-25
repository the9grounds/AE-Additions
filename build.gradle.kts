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
    }

    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "4.1.+") {
            isChanging = true
        }

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
    }
}

plugins {
    `java-library`
    id("net.darkhax.curseforgegradle") version "1.0.10"
}

apply {
    plugin("net.minecraftforge.gradle")
    plugin("kotlin")
    plugin("idea")
}

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
val cofhVersion: String by project
val curseForgeProjectId: String by project
val modBaseName: String by project
val modCurseId: String by project

project.group = "com.the9grounds.aeadditions"
base.archivesBaseName = "AEAdditions-${minecraftVersion}"

configure<UserDevExtension> {
    mappings(mcpChannel, mcpMappings)
    
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

        }
        create("server") {
            workingDirectory(project.file("run"))

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

            args("--mod", "aeadditions", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources"))
        }
    }
}

repositories {
    jcenter()
    mavenCentral()
    maven(url = "http://maven.shadowfacts.net/")
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
}

dependencies {
    "minecraft"("net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}")

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("thedarkcolour:kotlinforforge:1.14.0")

    val jeiApi = project.dependencies.create(group = "mezz.jei", name = "jei-${minecraftVersion}", version = jeiVersion, classifier = "api")
    val jei = project.dependencies.create(group = "mezz.jei", name = "jei-${minecraftVersion}", version = jeiVersion)
    val ae2 = project.dependencies.create(group = "appeng", name = "appliedenergistics2", version = aeVersion)

    compileOnly(project.the<DependencyManagementExtension>().deobf(jeiApi))
    runtimeOnly(project.the<DependencyManagementExtension>().deobf(jei))

    implementation(project.the<DependencyManagementExtension>().deobf(ae2))

    implementation(project.the<DependencyManagementExtension>().deobf("mekanism:Mekanism:${mekanismVersion}"))

    implementation(project.the<DependencyManagementExtension>().deobf("curse.maven:hwyla-253449:${hwylaVersion}"))
}

val Project.minecraft: UserDevExtension
    get() = extensions.getByName<UserDevExtension>("minecraft")

tasks.withType<Jar> {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", getBetterVersion())

    baseName = "${modBaseName}-${getBetterVersion()}"

    // replace stuff in mcmod.info, nothing else
    filesMatching("META-INF/mods.toml") {
        expand(mapOf(
            "version" to getBetterVersion(),
            "mcversion" to "1.16.5"
        ))
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
    mainFile.addModLoader("Forge")
}

fun getBuildNumber(): String? {

    if (System.getenv("CI") == null) {
        return "LOCAL"
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

tasks.create("copyResourceToClasses", Copy::class) {
    tasks.classes.get().dependsOn(this)
    dependsOn(tasks.processResources.get())
    onlyIf { gradle.taskGraph.hasTask(tasks.getByName("prepareRuns")) }
    into("$buildDir/classes/kotlin/main")
    from(tasks.processResources.get().destinationDir)
}
