plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("com.squareup:javapoet:1.12.1")

    implementation(project(mapOf("path" to ":CloudAnnotations")))
    implementation(project(mapOf("path" to ":CloudBase")))
}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)
