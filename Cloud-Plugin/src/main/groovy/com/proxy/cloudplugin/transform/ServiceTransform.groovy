package com.proxy.cloudplugin.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.proxy.cloudplugin.inject.ServiceInject
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class ServiceTransform extends Transform {

    private Project mProject

    ServiceTransform(Project project) {
        this.mProject = project
    }

    @Override
    String getName() {
        return "ServiceTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        def transformOutputProvider = transformInvocation.getOutputProvider()
        def dataPath = "com/proxy/service/api/plugin/DataByPlugin.class"
        def classPath = "com/cloud/service/\$\$Cloud\$\$Service\$\$Cache"
        def directory = new ArrayList<QualifiedContent>()
        def jar = new ArrayList<QualifiedContent>()
        def services = new ArrayList()
        def data = new HashSet()

        transformInvocation.inputs.each {
            it.directoryInputs.each { dirInput ->
                dirInput.file.traverse { file ->
                    if (file.absolutePath.contains(classPath)) {
                        services.add("com.cloud.service." + file.name.substring(0, file.name.length() - 6))
                    }
                    if (file.absolutePath.endsWith(dataPath)) {
                        data.add(dirInput.file.absolutePath)
                    }
                }
                directory.add(dirInput)
            }
            it.jarInputs.each { jarInput ->
                mProject.zipTree(jarInput.file.absolutePath).files.each { file ->
                    if (file.absolutePath.contains(classPath)) {
                        services.add("com.cloud.service." + file.name.substring(0, file.name.length() - 6))
                    }
                    if (file.absolutePath.endsWith(dataPath)) {
                        data.add(jarInput.file.absolutePath)
                    }
                }
                jar.add(jarInput)
            }
        }

        try {
            ServiceInject.inject(services, data, mProject)
        } catch (Throwable throwable) {
            throwable.printStackTrace()
        }

        directory.each {
            def outPath = transformOutputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
            FileUtils.copyDirectory(it.file, outPath)
        }

        jar.each {
            def jarName = it.name
            def md5Name = DigestUtils.md5Hex(it.file.absolutePath)
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            def outPath = transformOutputProvider.getContentLocation(jarName + md5Name, it.contentTypes, it.scopes, Format.JAR)
            FileUtils.copyFile(it.file, outPath)
        }
    }

}