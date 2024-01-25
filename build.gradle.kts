import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.welniak.gameoflife"
version = "1.0"

kotlin {
    jvm()

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        jvmTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.junit.jupiter:junit-jupiter:5.10.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                implementation("app.cash.turbine:turbine:1.0.0")
            }

            tasks.withType<Test> {
                useJUnitPlatform()
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "YAIoGoL"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}
