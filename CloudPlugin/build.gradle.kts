plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.kotlin)
    compileOnly("com.android.tools.build:gradle:8.3.0")
    implementation(gradleApi())
    implementation("org.ow2.asm:asm:9.6")
    implementation("org.ow2.asm:asm-commons:9.6")
}
apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)

