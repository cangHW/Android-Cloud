plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)

