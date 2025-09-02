import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

fun properties(key: String) = providers.gradleProperty(key).get()

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
}

group = properties("pluginGroup")
version = properties("pluginVersion")

sourceSets["main"].java.srcDirs("src/main/gen")

// Configure project's dependencies
repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(properties("pluginVersion"))
    groups.empty()
    repositoryUrl = properties("pluginRepositoryUrl")
}

kotlin {
    jvmToolchain(properties("javaVersion").toInt())
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }

    buildSearchableOptions {
        enabled = false
    }
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.opentest4j)

    // IntelliJ Platform Gradle Plugin Dependencies Extension
    // read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        val platformType = properties("platformType")
        val platformVersion = when (platformType) {
            "IU", "IC" -> properties("ideaVersion")
            "CL" -> properties("clionVersion")
            "GO" -> properties("golandVersion")
            else -> throw IllegalArgumentException("Unknown IDE type: $platformType, supported types: IU, IC, CL, GO")
        }
        val platformPlugins = when (platformType) {
            "IU" -> properties("ideaPlugins")
            "IC" -> properties("ideaCommunityPlugins")
            "CL" -> properties("clionPlugins")
            "GO" -> properties("golandPlugins")
            else -> throw IllegalArgumentException("Unknown IDE type: $platformType, supported types: IU, IC, CL, GO")
        }
        val bundledPlugins = when (platformType) {
            "IU" -> properties("ideaBundledPlugins")
            "IC" -> properties("ideaCommunityBundledPlugins")
            "CL" -> properties("clionBundledPlugins")
            "GO" -> properties("golandBundledPlugins")
            else -> throw IllegalArgumentException("Unknown IDE type: $platformType, supported types: IU, IC, CL, GO")
        }
        val useInstaller = !platformVersion.contains(Regex("EAP|SNAPSHOT"))

        create(platformType, platformVersion, useInstaller)

        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        bundledPlugins(bundledPlugins.split(',', ' ').filter(String::isNotEmpty))

        // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
        plugins(platformPlugins.split(',', ' ').filter(String::isNotEmpty))

//        instrumentationTools()
        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {

        version = properties("pluginVersion")


        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = with(changelog) {
            renderItem(
                (getOrNull(properties("pluginVersion")) ?: getLatest())
                    .withHeader(false)
                    .withEmptySections(false),
                Changelog.OutputType.HTML,
            )
        }

        ideaVersion {
            sinceBuild = properties("pluginSinceBuild")
//            untilBuild = properties("pluginUntilBuild")
            untilBuild = provider { null }
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = providers.gradleProperty("pluginVersion")
            .map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

intellijPlatformTesting {
    runIde {
        register("runIdeForUiTests") {
            task {
                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Drobot-server.port=8082",
                        "-Dide.mac.message.dialogs.as.sheets=false",
                        "-Djb.privacy.policy.text=<!--999.999-->",
                        "-Djb.consents.confirmation.enabled=false",
                    )
                }
            }

            plugins {
                robotServerPlugin()
            }
        }
    }
}
