import com.android.build.gradle.internal.scope.publishBuildArtifacts

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.peihua8858.tools"
        compileSdk = 35
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "toolsKit"
    jvm()
    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // 源集声明。
    //声明目标会自动创建具有相同名称的源集。默认情况下，
    //Kotlin Gradle 插件会创建相互依赖的其他源代码集，因为它是
    //在相关目标之间共享源。
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
                implementation(libs.kotlinx.coroutines.core)
//                implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
                // Add KMP dependencies here
            }
        }
        jvmMain{
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
                // Add KMP dependencies here
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.21")
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.8.0")
            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test:1.9.21")
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.android)
                implementation(libs.ktor.client.core.jvm)
                implementation(libs.kotlin.stdlib)
                implementation(kotlin("stdlib"))
                implementation (libs.androidx.loader)
                implementation ("androidx.startup:startup-runtime:1.1.1")
                implementation ("androidx.core:core-ktx:1.17.0")
                implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
                implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
                implementation ("androidx.exifinterface:exifinterface:1.4.1")
                implementation ("androidx.documentfile:documentfile:1.1.0")
                implementation("androidx.interpolator:interpolator:1.0.0")
                implementation(libs.androidx.runtime.android)
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation("androidx.test:runner:1.7.0")
                implementation("androidx.test:core:1.7.0")
                implementation("androidx.test.ext:junit:1.3.0")
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}
apply(from = "push_maven1.gradle")