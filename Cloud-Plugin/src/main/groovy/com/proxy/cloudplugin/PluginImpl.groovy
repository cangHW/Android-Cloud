package com.proxy.cloudplugin

import com.android.build.gradle.AppExtension
import com.proxy.cloudplugin.transform.ServiceTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        def serviceTransform = new ServiceTransform(project)
        android.registerTransform(serviceTransform)
    }
}