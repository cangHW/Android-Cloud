package com.proxy.cloudplugin.gradle_8.task

import com.proxy.cloudplugin.PluginImpl
import com.proxy.cloudplugin.asm.ServiceInject
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

/**
 * @author: cangHX
 * @data: 2024/3/13 18:04
 * @desc:
 */
abstract class ServiceTask : DefaultTask() {

    //所有的class文件输入信息
    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    //所有的jar文件输入信息
    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    //经过插桩修改后的输出信息
    @get:OutputFile
    abstract val output: RegularFileProperty

    private val services = ArrayList<String>()

    @TaskAction
    fun taskAction() {
        services.clear()
        var dataJarFile: File? = null

        //输出到output的流
        val jarOutput = JarOutputStream(
            BufferedOutputStream(FileOutputStream(output.get().asFile))
        )

        allDirectories.get().forEach { dir ->
            dir.asFile.walk().forEach loop@{ file ->
                if (!file.isFile) {
                    return@loop
                }
                findService(file.absolutePath)
                val relativePath = dir.asFile.toURI().relativize(file.toURI()).path
                copyFile(
                    file.inputStream(),
                    jarOutput,
                    relativePath.replace(File.separatorChar, '/')
                )
            }
        }

        allJars.get().forEach { jarInputFile ->
            val jarFile = JarFile(jarInputFile.asFile)
            jarFile.entries().iterator().forEach loop@{ jarEntry ->
                if (!jarEntry.name.endsWith(".class")) {
                    return@loop
                }
                if (jarEntry.name.contains("META-INF")) {
                    return@loop
                }
                if (jarEntry.name.endsWith(PluginImpl.className)) {
                    dataJarFile = jarInputFile.asFile
                    return@loop
                }
                findService(jarEntry.name)
                copyFile(
                    jarFile.getInputStream(jarEntry),
                    jarOutput,
                    jarEntry.name
                )
            }
            jarFile.close()
        }

        dataJarFile?.let {
            serviceInject(services, it, jarOutput)
        }

        jarOutput.close()
    }

    private fun findService(jarEntryName: String) {
        if (jarEntryName.contains(PluginImpl.servicePath)) {
            val className = jarEntryName.split("/").last()

            val service = "com.cloud.service." + className.substring(
                0,
                className.length - 6
            )
            if (!services.contains(service)) {
                services.add(service)
            }
        }
    }

    private fun serviceInject(
        services: ArrayList<String>,
        dataJarFile: File,
        jarOutput: JarOutputStream
    ) {
        val jarFile = JarFile(dataJarFile)
        jarFile.entries().iterator().forEach { jarEntry ->
            if (!jarEntry.name.endsWith(PluginImpl.className)) {
                return@forEach
            }
            copyFile(
                ServiceInject.inject(services, jarFile.getInputStream(jarEntry)).inputStream(),
                jarOutput,
                jarEntry.name
            )
        }
        jarFile.close()
    }

    private fun copyFile(jarInput: InputStream, jarOutput: JarOutputStream, jarEntryName: String) {
        jarOutput.putNextEntry(JarEntry(jarEntryName))
        jarInput.use {
            it.copyTo(jarOutput)
        }
        jarOutput.closeEntry()
    }
}