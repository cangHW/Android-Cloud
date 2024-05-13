import com.proxy.service.buildsrc.MavenConfig


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
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

//    kotlin {
//        jvmToolchain(8)
//    }

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

    if (MavenConfig.Service_Utils_Info.isLoadMaven()) {
        implementation(libs.service.utils.info)
    } else {
        implementation(project(mapOf("path" to ":Service-Utils:Service-Utils-Info")))
    }

    if (MavenConfig.Service_Ui_Info.isLoadMaven()) {
        implementation(libs.service.ui.info)
    } else {
        implementation(project(mapOf("path" to ":Service-UI:Service-UI-Info")))
    }

    if (MavenConfig.Cloud_Compiler.isLoadMaven()) {
        kapt(libs.cloud.compiler)
    } else {
        kapt(project(mapOf("path" to ":Cloud-Compiler")))
    }

    if (MavenConfig.Service_Net_Base.isLoadMaven()) {
        implementation(libs.service.net.base)
    } else {
        implementation(project(mapOf("path" to ":Service-NetWork:Service-NetWork-Base")))
    }

    if (MavenConfig.Service_Media_Base.isLoadMaven()) {
        implementation(libs.service.media.base)
    } else {
        implementation(project(mapOf("path" to ":Service-Media:Service-Media-Base")))
    }

}
