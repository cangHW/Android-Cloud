plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.network.retrofit"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.5.0")

//    if (Boolean.parseBoolean(isRelease) && Boolean.parseBoolean(cloud_compiler_isFromMaven)) {
//        annotationProcessor "${publishedGroupId}:Cloud-Compiler:${cloud_compiler_version}"
//    } else {
//        annotationProcessor project(path: ':Cloud-Compiler')
//    }

//    if (Boolean.parseBoolean(isRelease) && Boolean.parseBoolean(service_utils_info_isFromMaven)) {
//        implementation "${publishedGroupId}:Service-Utils-Info:${service_utils_info_version}"
//    } else {
        implementation(project(mapOf("path" to ":Service-Utils:Service-Utils-Info")))
//    }

//    if (Boolean.parseBoolean(isRelease) && Boolean.parseBoolean(service_net_base_isFromMaven)) {
//        api "${publishedGroupId}:Service-Net-Base:${service_net_base_version}"
//    } else {
        api(project(mapOf("path" to ":Service-NetWork:Service-NetWork-Base")))
//    }
}

//ext {
//    artifact = 'Service-Net-Retrofit'
//    libraryName = 'Cloud net module'
//    libraryDescription = 'The perfect framework for Android'
//    libraryVersion = service_net_retrofit_version
//}
//
//apply from: '../../publish.gradle'
//apply from: '../../upload.gradle'