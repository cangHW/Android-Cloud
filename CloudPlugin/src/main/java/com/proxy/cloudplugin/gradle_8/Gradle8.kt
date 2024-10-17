package com.proxy.cloudplugin.gradle_8

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.proxy.cloudplugin.gradle_8.task.ServiceTask
import org.gradle.api.Project

/**
 * @author: cangHX
 * @data: 2024/3/19 14:28
 * @desc:
 */
object Gradle8 {

    fun run(target: Project){
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