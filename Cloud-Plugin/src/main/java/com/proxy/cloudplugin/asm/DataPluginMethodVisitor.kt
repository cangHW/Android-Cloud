package com.proxy.cloudplugin.asm

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author: cangHX
 * @data: 2024/3/15 12:59
 * @desc:
 */
class DataPluginMethodVisitor(
    methodVisitor: MethodVisitor?,
    private val services: ArrayList<String>
) : MethodVisitor(Opcodes.ASM9, methodVisitor) {

    override fun visitInsn(opcode: Int) {
        println("visitInsn -> opcode ：$opcode")
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            services.forEach {
                mv.visitVarInsn(Opcodes.ALOAD, 1)
                mv.visitLdcInsn(it)
                mv.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    "java/util/List",
                    "add",
                    "(Ljava/lang/Object;)Z",
                    true
                )
                mv.visitInsn(Opcodes.POP)

                println("DataPlugin -> 插入：$it")
            }
        }
        super.visitInsn(opcode)
    }

}