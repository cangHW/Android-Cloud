package com.proxy.cloudplugin.inject

import com.android.build.api.transform.QualifiedContent
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

class ServiceInject {

    static void inject(ArrayList<QualifiedContent> directory, ArrayList<QualifiedContent> jar, ArrayList<String> services, HashSet<String> paths, Project project) {
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
        ClassPool classPool = ClassPool.getDefault()
        def path = paths[0]

        directory.each {
            classPool.appendClassPath(it.file.absolutePath)
        }
        jar.each {
            classPool.appendClassPath(it.file.absolutePath)
        }
        classPool.appendClassPath(project.android.bootClasspath[0].toString())
        classPool.importPackage("java.util.List")

        CtClass ctClass = classPool.getOrNull("com.proxy.service.api.plugin.DataByPlugin")
        if (ctClass != null) {
            if (ctClass.isFrozen()) {
                ctClass.defrost()
            }

            CtMethod ctMethod = ctClass.getDeclaredMethod("getClasses")

            services.each { it ->
                ctMethod.insertAfter("list.add(\"" + it + "\");")
            }

            ctClass.writeFile(path)
            ctClass.detach()
        }

        classPool.clearImportedPackages()
    }

}