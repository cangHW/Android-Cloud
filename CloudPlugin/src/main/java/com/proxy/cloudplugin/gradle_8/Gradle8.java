package com.proxy.cloudplugin.gradle_8;

import com.android.build.api.artifact.ScopedArtifact;
import com.android.build.api.variant.AndroidComponentsExtension;
import com.android.build.api.variant.ScopedArtifacts;
import com.android.build.api.variant.Variant;
import com.proxy.cloudplugin.gradle_8.task.ServiceTask;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

/**
 * cangHX
 * on 2024/3/19 14:28
 */
public class Gradle8 {

    public static void run(Project target) {
        AndroidComponentsExtension androidComponentsExtension = target.getExtensions()
                .getByType(AndroidComponentsExtension.class);
        androidComponentsExtension.onVariants(androidComponentsExtension.selector().all(), (Action<Variant>) variant -> {
            //注册 ServiceTask 任务
            TaskProvider<ServiceTask> taskProvider = target.getTasks().register(
                    variant.getName() + "AndroidCloudServiceTask", ServiceTask.class
            );

            //扫描所有class
            variant.getArtifacts().forScope(ScopedArtifacts.Scope.ALL)
                    .use(taskProvider)
                    .toTransform(
                            ScopedArtifact.CLASSES.INSTANCE,
                            ServiceTask::getAllJars,
                            ServiceTask::getAllDirectories,
                            ServiceTask::getOutput
                    );
        });
    }

}