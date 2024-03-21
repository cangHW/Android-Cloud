import com.proxy.service.buildsrc.MavenConfig
import com.proxy.service.buildsrc.VersionConfig

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.proxy.service.api"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("androidx.appcompat:appcompat:1.1.0")

    if (MavenConfig.Cloud_Annotations.isLoadMaven()) {
        api(libs.cloud.annotations)
    } else {
        api(project(mapOf("path" to ":Cloud-Annotations")))
    }

    if (MavenConfig.Cloud_Base.isLoadMaven()) {
        api(libs.cloud.base)
    } else {
        api(project(mapOf("path" to ":Cloud-Base")))
    }

}

extra[VersionConfig.Group] = libs.cloud.api.get().group
extra[VersionConfig.Artifact] = libs.cloud.api.get().name
extra[VersionConfig.Version] = libs.cloud.api.get().version
extra[VersionConfig.Library_Name] = VersionConfig.Library_Name_Default
extra[VersionConfig.Library_Description] = VersionConfig.Library_Description_Default

apply(from = "../publish.gradle")
apply(from = "../upload.gradle")
