plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.cloud.service")
}

android {
    namespace = "com.proxy.androidcloud"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.chx.androidcloud"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("CLOUD_MODULE_NAME", project.getName())
            }
        }
    }
    buildTypes {
        debug {
//            minifyEnabled true//开启混淆
//            zipAlignEnabled true
//            shrinkResources true//资源压缩
        }
        release {
            isMinifyEnabled = true//开启混淆
            isZipAlignEnabled = true
            isShrinkResources = true//资源压缩
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(mapOf("path" to ":Service-Utils:Service-Utils-Info")))

    implementation(project(mapOf("path" to ":Service-UI:Service-UI-Info")))
    kapt(libs.cloud.compiler)
    implementation(project(mapOf("path" to ":Service-NetWork:Service-NetWork-Base")))
    implementation(project(mapOf("path" to ":Service-Media:Service-Media-Base")))
}

apply(from = File(project.rootDir.absolutePath, "Plugins/gradle/common.gradle").absolutePath)
