architectury {
    common(rootProject.property("enabled_platforms").toString().split(","))
}

loom {
    accessWidenerPath.set(file("src/main/resources/ae2additions.accesswidener"))
}

repositories {
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

dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury:${rootProject.property("architectury_version")}")
    val jei = project.dependencies.create(group = "mezz.jei", name = "jei-1.20.1-forge", version = rootProject.property("jeiVersion").toString())
    val ae2 = project.dependencies.create(group = "appeng", name = "appliedenergistics2-forge", version = rootProject.property("aeVersion").toString())
    modImplementation(ae2)
    modImplementation(jei)



    modImplementation("mekanism:Mekanism:${rootProject.property("mekanismVersion")}")
    modImplementation("curse.maven:applied-mekanistics-574300:4842281")
    modImplementation("curse.maven:ae2-things-forge-609977:4616683")
    modApi("curse.maven:ftb-teams-404468:5176343")
}