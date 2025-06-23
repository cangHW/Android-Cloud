plugins {
    id("java-library")
}

dependencies {

}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)
