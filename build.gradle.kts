@file:Suppress("UnstableApiUsage")

import earth.terrarium.cloche.api.metadata.ModMetadata
import net.msrandom.minecraftcodev.core.utils.toPath
import net.msrandom.minecraftcodev.runs.task.WriteClasspathFile
import kotlin.io.path.*

plugins {
    idea
    `maven-publish`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.terrarium.cloche)
    alias(libs.plugins.meowdding.repo)
    //alias(libs.plugins.meowdding.resources)
    alias(libs.plugins.kotlin.symbol.processor)
}


group = "me.owdding"

repositories {
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
    maven(url = "https://maven.teamresourceful.com/repository/msrandom/")
    maven(url = "https://repo.hypixel.net/repository/Hypixel/")
    maven(url = "https://api.modrinth.com/maven")
    maven(url = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven(url = "https://maven.nucleoid.xyz")
    mavenCentral()
}

tasks.getByName<ProcessResources>("processResources") {
    inputs.dir("src/main/lang")
    with(copySpec {
        from("src/main/lang").include("*.json").into("assets/meowdding-dev-utils/lang")
    })
}


cloche {
    metadata {
        modId = "meowdding-dev-utils"
        name = "meowdding-dev-utils"
        license = ""
        clientOnly = true
    }

    common {
        mixins.from("src/mixins/meowdding-dev-utils.mixins.json")

        dependencies {
            compileOnly(libs.meowdding.ktcodecs)
            compileOnly(libs.meowdding.ktmodules)

            modImplementation(libs.skyblockapi)
            modImplementation(libs.meowdding.lib)

            modImplementation(libs.fabric.language.kotlin)
        }
    }

    fun createVersion(
        name: String,
        version: String = name,
        loaderVersion: Provider<String> = libs.versions.fabric.loader,
        fabricApiVersion: Provider<String> = libs.versions.fabric.api,
        minecraftVersionRange: ModMetadata.VersionRange.() -> Unit = {
            start = version
            end = version
            endExclusive = false
        },
        dependencies: MutableMap<String, Provider<MinimalExternalModuleDependency>>.() -> Unit = { },
    ) {

        fabric(name) {
            includedClient()
            minecraftVersion = version
            this.loaderVersion = loaderVersion.get()

            //include(libs.hypixelapi) included in sbapi
            include(libs.skyblockapi)
            include(libs.meowdding.lib)
            //mixins.from("src/mixins/skyblock-pv.${sourceSet.name}.mixins.json")

            metadata {
                entrypoint("client") {
                    adapter = "kotlin"
                    value = "me.owdding.devutils.DevUtils"
                }

                fun dependency(modId: String, version: Provider<String>? = null) {
                    dependency {
                        this.modId = modId
                        this.required = true
                        if (version != null) version {
                            this.start = version
                        }
                    }
                }

                dependency {
                    modId = "minecraft"
                    required = true
                    version(minecraftVersionRange)
                }
                dependency("fabric")
                dependency("fabricloader", loaderVersion)
                dependency("fabric-language-kotlin", libs.versions.fabric.language.kotlin)
                dependency("skyblock-api", libs.versions.skyblockapi)
                dependency("meowdding-lib", libs.versions.meowdding.lib)
            }

            dependencies {
                fabricApi(fabricApiVersion, minecraftVersion)

                implementation("io.github.spair:imgui-java-binding:1.87.7") { isTransitive = false }
                implementation("io.github.spair:imgui-java-lwjgl3:1.87.7") { isTransitive = false }

                implementation("io.github.spair:imgui-java-natives-windows:1.87.7") { isTransitive = false }
                implementation("io.github.spair:imgui-java-natives-linux:1.87.7") { isTransitive = false }
                implementation("io.github.spair:imgui-java-natives-macos:1.87.7") { isTransitive = false }

                implementation("net.kyori:adventure-text-minimessage:4.21.0")
                implementation("net.kyori:adventure-text-serializer-gson:4.22.0")
                implementation("net.kyori:adventure-text-serializer-legacy:4.21.0")
                implementation("net.kyori:adventure-api:4.21.0")
            }

            runs {
                client {
                    arguments("--quickPlaySingleplayer=\"${name.replace(".", "")}\"")
                }
            }
        }
    }

    createVersion("1.21.5", fabricApiVersion = provider { "0.127.1" }) {
    }
    createVersion("1.21.8", minecraftVersionRange = {
        start = "1.21.6"
    }) {
    }

    mappings { official() }
}

dependencies {
    compileOnly(libs.bundles.meowdding)
    ksp(libs.meowdding.ktmodules)
    ksp(libs.meowdding.ktcodecs)


    //modImplementation(libs.fabric.api)
    //modImplementation(libs.fabric.loader)
    //modImplementation(libs.fabric.language.kotlin)

    implementation(libs.kotlin.stdlib)

    //modImplementation("net.kyori:adventure-platform-mod-shared-fabric-repack:6.4.0")
    implementation("net.kyori:adventure-text-minimessage:4.21.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.22.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.21.0")
    implementation("net.kyori:adventure-api:4.21.0")

    //modImplementation(libs.skyblockapi)
    //include(libs.skyblockapi)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

ksp {
    sourceSets.filterNot { it.name == SourceSet.MAIN_SOURCE_SET_NAME }
        .forEach { this.excludedSources.from(it.kotlin.srcDirs) }
    arg("meowdding.project_name", "DevUtils")
    arg("meowdding.package", "me.owdding.devutils.generated")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "meowdding-dev-utils"
            from(components["java"])

            pom {
                name.set("MeowddingDevUtils")
                url.set("https://github.com/meowdding/meowdding-dev-utils")

                scm {
                    connection.set("git:https://github.com/meowdding/meowdding-dev-utils.git")
                    developerConnection.set("git:https://github.com/meowdding/meowdding-dev-utils.git")
                    url.set("https://github.com/meowdding/meowdding-dev-utils")
                }
            }
        }
    }
    repositories {
        maven {
            setUrl("https://maven.teamresourceful.com/repository/thatgravyboat/")
            credentials {
                username = System.getenv("MAVEN_USER") ?: providers.gradleProperty("maven_username").orNull
                password = System.getenv("MAVEN_PASS") ?: providers.gradleProperty("maven_password").orNull
            }
        }
    }
}

// TODO temporary workaround for a cloche issue on certain systems, remove once fixed
tasks.withType<WriteClasspathFile>().configureEach {
    actions.clear()
    actions.add {
        output.get().toPath().also { it.parent.createDirectories() }.takeUnless { it.exists() }?.createFile()
        generate()
        val file = output.get().toPath()
        file.writeText(file.readText().lines().joinToString(File.pathSeparator))
    }
}