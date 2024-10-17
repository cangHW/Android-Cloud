plugins {
    id("com.android.library")
}

android {
    namespace="com.proxy.service.ui.base"
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

//    if (MavenConfig.Cloud_Api.isLoadMaven()) {
//        api(libs.cloud.api)
//    } else {
        api(project(mapOf("path" to ":CloudApi")))
//    }
}

//extra[NormalConfig.Group] = libs.service.ui.base.get().module.group
//extra[NormalConfig.Artifact] = libs.service.ui.base.get().module.name
//extra[NormalConfig.Version] = libs.versions.service.ui.base.version.get()
//extra[NormalConfig.Library_Name] = NormalConfig.Library_Name_Default
//extra[NormalConfig.Library_Description] = NormalConfig.Library_Description_Default
//
//apply(from = "../../publish.gradle")
//apply(from = "../../upload.gradle")