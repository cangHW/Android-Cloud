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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("androidx.appcompat:appcompat:1.1.0")

//    if (MavenConfig.Cloud_Annotations.isLoadMaven()) {
//        api(libs.cloud.annotations)
//    } else {
        api(project(mapOf("path" to ":CloudAnnotations")))
//    }

//    if (MavenConfig.Cloud_Base.isLoadMaven()) {
//        api(libs.cloud.base)
//    } else {
        api(project(mapOf("path" to ":CloudBase")))
//    }

}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)

//extra[NormalConfig.Group] = libs.cloud.api.get().module.group
//extra[NormalConfig.Artifact] = libs.cloud.api.get().module.name
//extra[NormalConfig.Version] = libs.versions.cloud.api.version.get()
//extra[NormalConfig.Library_Name] = NormalConfig.Library_Name_Default
//extra[NormalConfig.Library_Description] = NormalConfig.Library_Description_Default
//
//apply(from = "../publish.gradle")
//apply(from = "../upload.gradle")
