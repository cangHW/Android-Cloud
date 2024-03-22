import com.proxy.service.buildsrc.MavenConfig
import com.proxy.service.buildsrc.NormalConfig

plugins {
    id("com.android.library")
}

android {
    namespace = "com.proxy.service.network.base"
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

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("com.google.code.gson:gson:2.8.5")
    implementation("androidx.appcompat:appcompat:1.1.0")

    if (MavenConfig.Cloud_Api.isLoadMaven()) {
        api(libs.cloud.api)
    } else {
        api(project(mapOf("path" to ":Cloud-Api")))
    }

    if (MavenConfig.Service_Net_Base.isLoadMaven()) {
        implementation(libs.service.net.base)
    } else {
        api(project(mapOf("path" to ":Service-Utils:Service-Utils-Info")))
    }
}

extra[NormalConfig.Group] = libs.service.net.base.get().group
extra[NormalConfig.Artifact] = libs.service.net.base.get().name
extra[NormalConfig.Version] = libs.service.net.base.get().version
extra[NormalConfig.Library_Name] = NormalConfig.Library_Name_Default
extra[NormalConfig.Library_Description] = NormalConfig.Library_Description_Default

apply(from = "../../publish.gradle")
apply(from = "../../upload.gradle")
