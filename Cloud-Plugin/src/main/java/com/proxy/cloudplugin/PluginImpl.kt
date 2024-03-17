package com.proxy.cloudplugin

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.proxy.cloudplugin.task.ServiceTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author: cangHX
 * @data: 2024/3/13 17:55
 * @desc:
 */
class PluginImpl : Plugin<Project> {
    override fun apply(target: Project) {
        val androidComponentsExtension =
            target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponentsExtension.onVariants {
            //注册 ServiceTask 任务
            val taskProvider = target.tasks.register(
                "${it.name}AndroidCloudServiceTask", ServiceTask::class.java
            )
            //扫描所有class
            it.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                .use(taskProvider)
                .toTransform(
                    type = ScopedArtifact.CLASSES,
                    inputJars = ServiceTask::allJars,
                    inputDirectories = ServiceTask::allDirectories,
                    into = ServiceTask::output
                )
        }
    }
}