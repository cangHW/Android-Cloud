package com.proxy.cloudplugin.asm

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author: cangHX
 * @data: 2024/3/15 12:59
 * @desc:
 */
class DataPluginMethodVisitor(
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?,
    private val services: ArrayList<String>
) : AdviceAdapter(
    Opcodes.ASM9,
    methodVisitor,
    access,
    name,
    descriptor
) {

    override fun onMethodEnter() {
        super.onMethodEnter()

        services.forEach {
            mv.visitVarInsn(ALOAD, 1)
            mv.visitLdcInsn(it)
            mv.visitMethodInsn(
                INVOKEINTERFACE,
                "java/util/List",
                "add",
                "(Ljava/lang/Object;)Z",
                true
            )
            mv.visitInsn(POP)

            println("DataPlugin -> 插入：$it")
        }
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
    }

}