plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("com.squareup:javapoet:1.12.1")

//    if (MavenConfig.Cloud_Annotations.isLoadMaven()) {
//        implementation(libs.cloud.annotations)
//    } else {
        implementation(project(mapOf("path" to ":CloudAnnotations")))
//    }

//    if (MavenConfig.Cloud_Base.isLoadMaven()) {
//        implementation(libs.cloud.base)
//    } else {
        implementation(project(mapOf("path" to ":CloudBase")))
//    }
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)

//extra[NormalConfig.Group] = libs.cloud.compiler.get().module.group
//extra[NormalConfig.Artifact] = libs.cloud.compiler.get().module.name
//extra[NormalConfig.Version] = libs.versions.cloud.compiler.version.get()
//extra[NormalConfig.Library_Name] = NormalConfig.Library_Name_Default
//extra[NormalConfig.Library_Description] = NormalConfig.Library_Description_Default
//
//apply(from = "../publish.gradle")
//apply(from = "../upload.gradle")
