package com.proxy.cloudplugin.gradle_7

import org.gradle.api.Project
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.AppExtension
import com.proxy.cloudplugin.gradle_7.transform.ServiceTransform

/**
 * @author: cangHX
 * @data: 2024/3/19 14:28
 * @desc:
 */
object Gradle7 {

    fun run(target: Project){
        val isApp = target.plugins.hasPlugin(AppPlugin::class.java)
        if (isApp) {
            val android = target.extensions.getByType(AppExtension::class.java)
            val serviceTransform = ServiceTransform(target)
            android.registerTransform(serviceTransform)
        }
    }

}