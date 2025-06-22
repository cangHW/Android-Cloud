package com.proxy.cloudplugin.gradle_8.task;

import com.proxy.cloudplugin.PluginImpl;
import com.proxy.cloudplugin.asm.ServiceInject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * cangHX
 * on 2024/3/13 18:04
 */
public abstract class ServiceTask extends DefaultTask {

    // 所有的 class 文件输入信息
    @InputFiles
    public abstract ListProperty<Directory> getAllDirectories();

    // 所有的 jar 文件输入信息
    @InputFiles
    public abstract ListProperty<RegularFile> getAllJars();

    // 经过插桩修改后的输出信息
    @OutputFile
    public abstract RegularFileProperty getOutput();

    private final List<String> services = new ArrayList<>();

    @TaskAction
    public void taskAction() throws IOException {
        services.clear();
        File dataJarFile = null;

        // 输出到 output 的流
        JarOutputStream jarOutput = new JarOutputStream(
                new BufferedOutputStream(new FileOutputStream(getOutput().get().getAsFile()))
        );

        // 处理所有的目录
        for (Directory dir : getAllDirectories().get()) {
            for (File file : dir.getAsFile().listFiles()) {
                if (!file.isFile()) {
                    continue;
                }
                findService(file.getAbsolutePath());
                String relativePath = dir.getAsFile().toURI().relativize(file.toURI()).getPath();
                copyFile(new FileInputStream(file), jarOutput, relativePath.replace(File.separatorChar, '/'));
            }
        }

        // 处理所有的 jar 文件
        for (RegularFile jarInputFile : getAllJars().get()) {
            JarFile jarFile = new JarFile(jarInputFile.getAsFile());
            for (Iterator<JarEntry> it = jarFile.entries().asIterator(); it.hasNext(); ) {
                JarEntry jarEntry = it.next();
                if (!jarEntry.getName().endsWith(".class")) {
                    continue;
                }
                if (jarEntry.getName().contains("META-INF")) {
                    continue;
                }
                if (jarEntry.getName().endsWith(PluginImpl.className)) {
                    dataJarFile = jarInputFile.getAsFile();
                    continue;
                }
                findService(jarEntry.getName());
                copyFile(jarFile.getInputStream(jarEntry), jarOutput, jarEntry.getName());
            }
            jarFile.close();
        }

        if (dataJarFile != null) {
            serviceInject(services, dataJarFile, jarOutput);
        }

        jarOutput.close();
    }

    private void findService(String jarEntryName) {
        if (jarEntryName.contains(PluginImpl.servicePath)) {
            String className = jarEntryName.substring(jarEntryName.lastIndexOf("/") + 1);
            String service = "com.cloud.service." + className.substring(0, className.length() - 6);
            if (!services.contains(service)) {
                services.add(service);
            }
        }
    }

    private void serviceInject(List<String> services, File dataJarFile, JarOutputStream jarOutput) throws IOException {
        JarFile jarFile = new JarFile(dataJarFile);

        for (Iterator<JarEntry> it = jarFile.entries().asIterator(); it.hasNext(); ) {
            JarEntry jarEntry = it.next();
            if (!jarEntry.getName().endsWith(PluginImpl.className)) {
                continue;
            }
            copyFile(
                    new ByteArrayInputStream(ServiceInject.inject(services, jarFile.getInputStream(jarEntry))),
                    jarOutput,
                    jarEntry.getName()
            );
        }
        jarFile.close();
    }

    private void copyFile(InputStream jarInput, JarOutputStream jarOutput, String jarEntryName) throws IOException {
        jarOutput.putNextEntry(new JarEntry(jarEntryName));
        try (jarInput) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = jarInput.read(buffer)) != -1) {
                jarOutput.write(buffer, 0, bytesRead);
            }
        }
        jarOutput.closeEntry();
    }
}