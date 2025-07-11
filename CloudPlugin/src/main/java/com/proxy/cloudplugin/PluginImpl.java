package com.proxy.cloudplugin;

import com.proxy.cloudplugin.gradle_7.Gradle7;
import com.proxy.cloudplugin.gradle_8.Gradle8;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * cangHX
 * on 2024/3/13 17:55
 */
public class PluginImpl implements Plugin<Project> {

    public static final String className = "com/proxy/service/api/plugin/DataByPlugin.class";
    public static final String servicePath = "com/cloud/service/Cloud$$Service$$Cache$$";

    @Override
    public void apply(Project target) {
        int version;

        try {
            String gradleVersion = target.getGradle().getGradleVersion();
            System.out.println("gradleVersion = " + gradleVersion);
            version = Integer.parseInt(gradleVersion.split("\\.")[0]);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return;
        }

        System.out.println("version = " + version);

        if (version >= 8) {
            Gradle8.run(target);
        } else {
            Gradle7.run(target);
        }
    }
}