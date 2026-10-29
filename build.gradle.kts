import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.sumygg"
version = "1.0"

repositories {
    mavenCentral()
    google {
        content { includeGroupByRegex("androidx.*") }
    }
}

kotlin {
    jvm {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
    jvmToolchain(17)

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("an-arts")
        browser {
            commonWebpackConfig {
                outputFileName = "an-arts.js"
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.compose.ui:ui:${property("compose.version")}")
            implementation("org.jetbrains.compose.runtime:runtime:${property("compose.version")}")
            implementation("org.jetbrains.compose.foundation:foundation:${property("compose.version")}")
            implementation("org.jetbrains.compose.material:material:${property("compose.version")}")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.sumygg.anarts.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "an-arts"
            packageVersion = "1.0.0"
        }
    }
}
