import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.compose") version "0.3.2"
}

group = "com.sumygg"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    testImplementation(kotlin("test-testng"))
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
}

tasks.test {
    useTestNG()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
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
