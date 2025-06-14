import org.allaymc.gradle.plugin.AllayExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.allaymc.gradle) apply false
}

version = libs.versions.allaymc.experimental.get()

subprojects {
    version = rootProject.version

    if (project.parent?.name == "plugins") {
        plugins.apply(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
        plugins.apply(rootProject.libs.plugins.allaymc.gradle.get().pluginId)
        group = "vip.cdms.allay_exp"
        pluginManager.withPlugin(rootProject.libs.plugins.allaymc.gradle.get().pluginId) {
            extensions.configure<AllayExtension>("allay") {
                version = rootProject.libs.versions.allaymc.api.get()
                plugin {
                    authors += "MineBuilder"
                    website = "https://github.com/MineBuilders/allaymc-experimental"
                }
            }
        }
        dependencies {
            "implementation"(rootProject.libs.minebuilder.allaymc.kotlinx)
            "implementation"(rootProject.projects.utils)
        }
    }
}
