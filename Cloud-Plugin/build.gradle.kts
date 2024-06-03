import com.proxy.service.buildsrc.NormalConfig

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

extra[NormalConfig.Group] = libs.cloud.plugin.get().module.group
extra[NormalConfig.Artifact] = libs.cloud.plugin.get().module.name
extra[NormalConfig.Version] = libs.versions.cloud.plugin.version.get()
extra[NormalConfig.Library_Name] = NormalConfig.Library_Name_Default
extra[NormalConfig.Library_Description] = NormalConfig.Library_Description_Default

apply(from = "../publish.gradle")
apply(from = "../upload.gradle")


