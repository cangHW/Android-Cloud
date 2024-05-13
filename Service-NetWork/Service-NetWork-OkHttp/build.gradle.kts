import com.proxy.service.buildsrc.MavenConfig
import com.proxy.service.buildsrc.NormalConfig

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace="com.proxy.service.network.okhttp"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

        kapt {
            arguments {
                arg("CLOUD_MODULE_NAME", project.getName())
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("androidx.appcompat:appcompat:1.1.0")

    if (MavenConfig.Cloud_Compiler.isLoadMaven()) {
        kapt(libs.cloud.compiler)
    } else {
        kapt(project(mapOf("path" to ":Cloud-Compiler")))
    }

    if (MavenConfig.Service_Utils_Info.isLoadMaven()) {
        implementation(libs.service.utils.info)
    } else {
        implementation(project(mapOf("path" to ":Service-Utils:Service-Utils-Info")))
    }

    if (MavenConfig.Service_Net_Base.isLoadMaven()) {
        api(libs.service.net.base)
    } else {
        api(project(mapOf("path" to ":Service-NetWork:Service-NetWork-Base")))
    }
}
