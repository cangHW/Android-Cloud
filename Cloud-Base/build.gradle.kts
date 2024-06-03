
import com.proxy.service.buildsrc.NormalConfig

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

extra[NormalConfig.Group] = libs.cloud.base.get().module.group
extra[NormalConfig.Artifact] = libs.cloud.base.get().module.name
extra[NormalConfig.Version] = libs.versions.cloud.base.version.get()
extra[NormalConfig.Library_Name] = NormalConfig.Library_Name_Default
extra[NormalConfig.Library_Description] = NormalConfig.Library_Description_Default

apply(from = "../publish.gradle")
apply(from = "../upload.gradle")

