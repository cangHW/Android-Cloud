
import com.proxy.service.buildsrc.VersionConfig

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

extra[VersionConfig.Group] = libs.cloud.base.get().group
extra[VersionConfig.Artifact] = libs.cloud.base.get().name
extra[VersionConfig.Version] = libs.cloud.base.get().version
extra[VersionConfig.Library_Name] = VersionConfig.Library_Name_Default
extra[VersionConfig.Library_Description] = VersionConfig.Library_Description_Default

apply(from = "../publish.gradle")
apply(from = "../upload.gradle")

