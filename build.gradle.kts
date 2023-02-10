import kr.entree.spigradle.kotlin.*

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("kr.entree.spigradle") version "2.4.2"
    java
}

group = "io.github.cloudate9.endermaning"
version = "2.0.0"

repositories {
    mavenCentral()
    codemc()
    jitpack()
    papermc()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    implementation("com.github.secretx33.sc-cfg:sccfg-bukkit:main-SNAPSHOT")
    implementation("com.github.secretx33.sc-cfg:sccfg-yaml:main-SNAPSHOT")
    annotationProcessor("com.google.dagger:dagger-compiler:2.45")
    implementation("com.google.dagger:dagger:2.45")
    implementation("dev.triumphteam:triumph-gui:3.1.3")
    implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

tasks {

    compileJava {
        options.release.set(17)
    }

    shadowJar {
        archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")

        relocate("com.github.secretx33.sccfg", "${rootProject.group}.dependencies.com.github.secretx33.sccfg")
        relocate("de.exlll.configlib", "${rootProject.group}.dependencies.de.exlll.configlib")
        relocate("dev.triumphteam.gui", "${rootProject.group}.dependencies.dev.triumphteam.gui")
        relocate("net.wesjd", "${rootProject.group}.dependencies.net.wesjd")
        relocate("org.bstats", "${rootProject.group}.dependencies.org.bstats")
        relocate("org.intellij", "${rootProject.group}.dependencies.org.intellij")
        relocate("org.jetbrains", "${rootProject.group}.dependencies.org.jetbrains")
    }

    prepareSpigotPlugins {
        setDependsOn(mutableListOf(shadowJar.get()))
    }

    runSpigot {
        jvmArgs = mutableListOf(
            "-Xmx2G",
            "-Xms2G",
            "-XX:+UseZGC",
            "-XX:+ZUncommit",
            "-XX:ZUncommitDelay=3600",
            "-XX:+ZProactive",
            "-XX:+AlwaysPreTouch",
            "-XX:+DisableExplicitGC",
        )
    }

}

spigot {
    authors = listOf("Cloudate9")
    apiVersion = "1.18"
    description = "Allows players to become part Enderman."
    website = "https://cloudate9.com"
    excludeLibraries = listOf("*")
    commands {
        create("endermaning") {
            aliases = listOf("edm")
            description = "All endermaning commands."
            usage = "/endermaning"
        }
    }
    permissions {
        create("endermaning.configure") {
            description = "If someone can configure this plugin."
            defaults = "op"
        }
        create("endermaning.update") {
            description = "If someone can view updates for this plugin."
            defaults = "op"
        }
    }
}
