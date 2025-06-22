package com.proxy.cloudplugin.gradle_7;

import org.gradle.api.Project;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.AppExtension;
import com.proxy.cloudplugin.gradle_7.transform.ServiceTransform;

/**
 * cangHX
 * on 2024/3/19 14:28
 */
public class Gradle7 {

    public static void run(Project target){
        boolean isApp = target.getPlugins().hasPlugin(AppPlugin.class);
        if (isApp) {
            AppExtension android = target.getExtensions().getByType(AppExtension.class);
            ServiceTransform serviceTransform = new ServiceTransform(target);
            android.registerTransform(serviceTransform);
        }
    }

}