package com.proxy.cloudplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.proxy.cloudplugin.transform.ServiceTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            def android = project.extensions.getByType(AppExtension)
            def serviceTransform = new ServiceTransform(project)
            android.registerTransform(serviceTransform)
        }
    }
}