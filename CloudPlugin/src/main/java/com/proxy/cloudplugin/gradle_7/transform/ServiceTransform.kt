package com.proxy.cloudplugin.gradle_7.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.proxy.cloudplugin.PluginImpl
import com.proxy.cloudplugin.asm.ServiceInject
import com.proxy.cloudplugin.gradle_7.utils.FileUtils
import com.proxy.cloudplugin.gradle_7.utils.JarUtils
import org.gradle.api.Project
import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * @author: cangHX
 * @data: 2024/3/19 15:11
 * @desc:
 */
class ServiceTransform constructor(private val mProject: Project) : Transform() {

    override fun getName(): String {
        return "ServiceTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        val set = HashSet<QualifiedContent.ContentType>()
        set.add(QualifiedContent.DefaultContentType.CLASSES)
        return set
    }

    override fun getScopes(): MutableSet<QualifiedContent.Scope> {
        val set = HashSet<QualifiedContent.Scope>()
        set.add(QualifiedContent.Scope.PROJECT)
        set.add(QualifiedContent.Scope.SUB_PROJECTS)
        set.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        return set
    }

    override fun isIncremental(): Boolean {
        return false
    }

    private val services = ArrayList<String>()

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        services.clear()

        var dataJarFile: JarInput? = null

        val output = transformInvocation.outputProvider
        output.deleteAll()

        transformInvocation.inputs.forEach { input ->
            input.directoryInputs.forEach { dir ->

                dir.file.walk().forEach loop@{ file ->
                    if (!file.isFile) {
                        return@loop
                    }
                    findService(file.absolutePath)
                }
                val outPath = output.getContentLocation(
                    dir.name,
                    dir.contentTypes,
                    dir.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectory(dir.file, outPath)
            }
            input.jarInputs.forEach { jar ->

                var isDataJarFile = false

                checkJar(jar) check@{ jarFile, jarEntry ->
                    if (jarEntry.name.endsWith(PluginImpl.className)) {
                        isDataJarFile = true
                        dataJarFile = jar
                        return@check
                    }
                    findService(jarEntry.name)
                }

                if (!isDataJarFile) {
                    copyJar(jar, output)
                }
            }
        }

        dataJarFile?.let {
            checkJar(it) check@{ jarFile, jarEntry ->
                if (jarEntry.name.endsWith(PluginImpl.className)) {
                    val inputStream = jarFile.getInputStream(jarEntry)
                    JarUtils.write(
                        ServiceInject.inject(services, inputStream),
                        File(it.file.parentFile, PluginImpl.className)
                    )
                    inputStream.close()
                    return@check
                }
            }
            JarUtils.refreshJar(it.file)
            copyJar(it, output)
        }
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

    private fun copyJar(jar: JarInput, output: TransformOutputProvider) {
        var jarName = jar.name

        val md5Name = JarUtils.md5Hex(jar.file.absolutePath)
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length - 4)
        }
        val outPath =
            output.getContentLocation(jarName + md5Name, jar.contentTypes, jar.scopes, Format.JAR)
        FileUtils.copyFile(jar.file, outPath)
    }

    private fun checkJar(
        jarInput: JarInput,
        callback: (jarFile: JarFile, jarEntry: JarEntry) -> Unit
    ) {
        val jarFile = JarFile(jarInput.file)
        jarFile.entries().iterator().forEach loop@{ jarEntry ->
            if (!jarEntry.name.endsWith(".class")) {
                return@loop
            }
            if (jarEntry.name.contains("META-INF")) {
                return@loop
            }
            callback(jarFile, jarEntry)
        }
        jarFile.close()
    }




}