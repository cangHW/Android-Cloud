plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)

