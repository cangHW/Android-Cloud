package com.proxy.cloudplugin.inject

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class ServiceInject {

    static void inject(ArrayList<String> services, HashSet<String> paths, Project project) {
        if (paths.size() == 0) {
            System.err.println("There are some mistakes here")
            System.err.println("Do you import Cloud-Api ?")
            return
        }
        if (paths.size() > 1) {
            System.err.println("There are some mistakes here")
            paths.each {
                System.err.println(it)
            }
            return
        }
        def path = paths[0]

        ClassPool classPool = ClassPool.getDefault()
        classPool.appendClassPath(path)
        classPool.appendClassPath(project.android.bootClasspath[0].toString())
        classPool.importPackage("java.util.List")

        def file = new File(path)

        if (file.isDirectory()) {
            dirPath(classPool, services, path)
        } else {
            dirPath(classPool, services, new File(path).getParent())
            jarPath(path)
        }
    }

    private static void dirPath(ClassPool classPool, ArrayList<String> services, String outPath) {
        CtClass ctClass = classPool.getOrNull("com.proxy.service.api.plugin.DataByPlugin")
        if (ctClass != null) {
            if (ctClass.isFrozen()) {
                ctClass.defrost()
            }

            CtMethod ctMethod = ctClass.getDeclaredMethod("getClasses")

            services.each { it ->
                ctMethod.insertAfter("list.add(\"" + it + "\");")
            }

            ctClass.writeFile(outPath)
            ctClass.detach()
        }

        classPool.clearImportedPackages()
    }

    private static void jarPath(String jarPath) {
        def className = 'com/proxy/service/api/plugin/DataByPlugin.class'

        def file = new File(jarPath)
        JarFile jarFile = new JarFile(file)

        File tempFile = new File(file.getParent(), file.name + ".temp")
        if (tempFile.exists()) {
            tempFile.delete()
        }
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(tempFile))

        Enumeration<JarEntry> enumeration = jarFile.entries()

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement()
            ZipEntry zipEntry = new ZipEntry(jarEntry.name)
            outputStream.putNextEntry(zipEntry)

            if (jarEntry.name == className) {
                def dataFile = new File(new File(jarPath).getParent(), className)
                InputStream inputStream = new FileInputStream(dataFile)
                outputStream.write(IOUtils.toByteArray(inputStream))
                inputStream.close()
                dataFile.delete()
            } else {
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                outputStream.write(IOUtils.toByteArray(inputStream))
                inputStream.close()
            }
            outputStream.closeEntry()
        }
        outputStream.close()
        jarFile.close()
        if (file.exists()) {
            file.delete()
        }
        tempFile.renameTo(file)
    }
}