package com.proxy.cloudplugin

import com.proxy.cloudplugin.gradle_7.Gradle7
import com.proxy.cloudplugin.gradle_8.Gradle8
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author: cangHX
 * @data: 2024/3/13 17:55
 * @desc:
 */
class PluginImpl : Plugin<Project> {

    companion object{
        const val className = "com/proxy/service/api/plugin/DataByPlugin.class"
        const val servicePath = "com/cloud/service/\$\$Cloud\$\$Service\$\$Cache"
    }

    override fun apply(target: Project) {
        val version: Int

        try {
            val gradleVersion: String = target.gradle.gradleVersion
            version = gradleVersion.split(".")[0].toInt()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            return
        }

        println("gradleVersion = $version")

        if (version >= 8) {
            Gradle8.run(target)
        } else {
            Gradle7.run(target)
        }

    }
}