plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.utils.base"
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

    implementation("androidx.appcompat:appcompat:1.1.0")

//    if (Boolean.parseBoolean(isRelease) && Boolean.parseBoolean(cloud_api_isFromMaven)) {
//        api "${publishedGroupId}:Cloud-Api:${cloud_api_version}"
//    } else {
        api(project(mapOf("path" to ":CloudApi")))
//    }
}

//ext {
//    artifact = 'Service-Utils-Base'
//    libraryName = 'Cloud utils module'
//    libraryDescription = 'The perfect framework for Android'
//    libraryVersion = service_utils_base_version
//}
//
//apply from: '../../publish.gradle'
//apply from: '../../upload.gradle'