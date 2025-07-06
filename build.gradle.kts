plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.loom)
    `maven-publish`
}

group = "me.owdding"

repositories {
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
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

dependencies {
    compileOnly(libs.bundles.meowdding)
    ksp(libs.meowdding.ktmodules)
    ksp(libs.meowdding.ktcodecs)

    minecraft(libs.minecraft)
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.parchmentmc.get().toString())
    })

    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.language.kotlin)

    implementation(libs.kotlin.stdlib)

    implementation("net.kyori:adventure-text-minimessage:4.21.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.22.0")
    modImplementation("net.kyori:adventure-platform-mod-shared-fabric-repack:6.4.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.21.0")
    implementation("net.kyori:adventure-api:4.21.0")

    implementation("io.github.spair:imgui-java-binding:1.87.7") { isTransitive = false }
    implementation("io.github.spair:imgui-java-lwjgl3:1.87.7") { isTransitive = false }

    implementation("io.github.spair:imgui-java-natives-windows:1.87.7") { isTransitive = false }
    implementation("io.github.spair:imgui-java-natives-linux:1.87.7") { isTransitive = false }
    implementation("io.github.spair:imgui-java-natives-macos:1.87.7") { isTransitive = false }

    modImplementation(libs.skyblockapi)
    include(libs.skyblockapi)

    modRuntimeOnly(libs.devauth)
    modRuntimeOnly(libs.modmenu)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

ksp {
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
