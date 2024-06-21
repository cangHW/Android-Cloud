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

    override fun visitCode() {
        super.visitCode()
        println("visitCode")
    }

    override fun visitVarInsn(opcode: Int, varIndex: Int) {
        super.visitVarInsn(opcode, varIndex)
//        println("visitVarInsn -> opcode ：$opcode, varIndex ：$varIndex")
    }

    override fun visitLdcInsn(value: Any?) {
        super.visitLdcInsn(value)
//        println("visitLdcInsn -> value ：$value")
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
//        println(
//            "visitMethodInsn -> " +
//                    "opcode ：$opcode, " +
//                    "owner ：$owner, " +
//                    "name ：$name, " +
//                    "descriptor ：$descriptor, " +
//                    "isInterface ：$isInterface"
//        )
//        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

    override fun visitInsn(opcode: Int) {
//        println("visitInsn -> opcode ：$opcode")
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

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        super.visitMaxs(maxStack, maxLocals)
        println("visitMaxs -> maxStack ：$maxStack, maxLocals ：$maxLocals")
    }

    override fun visitEnd() {
        super.visitEnd()
        println("visitEnd")
    }

}