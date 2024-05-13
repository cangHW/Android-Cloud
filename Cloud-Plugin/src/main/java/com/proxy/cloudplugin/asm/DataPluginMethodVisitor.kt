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
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String,
        desc: String,
        itf: Boolean
    ) {
        println("visitMethodInsn -> opcode ：$opcode, owner : $owner, name : $name, desc : $desc, itf : $itf")

        if (opcode == Opcodes.INVOKEINTERFACE && "add" == name && "(Ljava/lang/Object;)Z" == desc) {
            return;
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf)
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        println("onMethodExit -> opcode ：$opcode")

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

}