import com.proxy.service.buildsrc.VersionConfig

plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly("com.android.tools.build:gradle:8.3.0")
    implementation(gradleApi())
    implementation("org.ow2.asm:asm:9.6")
    implementation("org.ow2.asm:asm-commons:9.6")
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8
//}

extra[VersionConfig.Group] = libs.cloud.plugin.get().group
extra[VersionConfig.Artifact] = libs.cloud.plugin.get().name
extra[VersionConfig.Version] = libs.cloud.plugin.get().version
extra[VersionConfig.Library_Name] = VersionConfig.Library_Name_Default
extra[VersionConfig.Library_Description] = VersionConfig.Library_Description_Default

apply(from = "../publish.gradle")
apply(from = "../upload.gradle")

