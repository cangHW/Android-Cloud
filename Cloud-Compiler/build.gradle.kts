import com.proxy.service.buildsrc.MavenConfig
import com.proxy.service.buildsrc.VersionConfig

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("com.squareup:javapoet:1.12.1")

    if (MavenConfig.Cloud_Annotations.isLoadMaven()) {
        implementation(libs.cloud.annotations)
    } else {
        implementation(project(mapOf("path" to ":Cloud-Annotations")))
    }

    if (MavenConfig.Cloud_Base.isLoadMaven()) {
        implementation(libs.cloud.base)
    } else {
        implementation(project(mapOf("path" to ":Cloud-Base")))
    }
}
//java {
//    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8
//}

extra[VersionConfig.Group] = libs.cloud.compiler.get().group
extra[VersionConfig.Artifact] = libs.cloud.compiler.get().name
extra[VersionConfig.Version] = libs.cloud.compiler.get().version
extra[VersionConfig.Library_Name] = VersionConfig.Library_Name_Default
extra[VersionConfig.Library_Description] = VersionConfig.Library_Description_Default

apply(from = "../publish.gradle")
apply(from = "../upload.gradle")
