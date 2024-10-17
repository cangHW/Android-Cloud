plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.media.info"
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

//    kotlinOptions {
//        jvmTarget = "17"
//    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("androidx.appcompat:appcompat:1.1.0")

//    if (MavenConfig.Cloud_Compiler.isLoadMaven()) {
//        kapt(libs.cloud.compiler)
//    } else {
//        kapt(project(mapOf("path" to ":CloudCompiler")))
//    }

//    if (MavenConfig.Service_Media_Base.isLoadMaven()) {
//        api(libs.service.media.base)
//    } else {
        api(project(mapOf("path" to ":Service-Media:Service-Media-Base")))
//    }
}

//extra[NormalConfig.Group] = libs.service.media.info.get().module.group
//extra[NormalConfig.Artifact] = libs.service.media.info.get().module.name
//extra[NormalConfig.Version] = libs.versions.service.media.info.version.get()
//extra[NormalConfig.Library_Name] = NormalConfig.Library_Name_Default
//extra[NormalConfig.Library_Description] = NormalConfig.Library_Description_Default
//
//apply(from = "../../publish.gradle")
//apply(from = "../../upload.gradle")