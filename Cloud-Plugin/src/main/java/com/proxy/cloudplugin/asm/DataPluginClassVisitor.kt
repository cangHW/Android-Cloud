package com.proxy.cloudplugin.asm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author: cangHX
 * @data: 2024/3/15 12:56
 * @desc:
 */
class DataPluginClassVisitor constructor(
    cv: ClassVisitor,
    private val services: ArrayList<String>
) : ClassVisitor(Opcodes.ASM9, cv) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
       val mv :MethodVisitor  = super.visitMethod(access, name, descriptor, signature, exceptions)
        if ("getClasses" == name) {
            println("visitMethod -> access ：$access, name : $name, descriptor : $descriptor, signature : $signature, exceptions : $exceptions")
            return DataPluginMethodVisitor(mv,access, name, descriptor, services)
        }
        return mv
    }

}