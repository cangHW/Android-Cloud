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
            // 加载第一个方法参数 list
            mv.visitVarInsn(Opcodes.ALOAD, 1)
            // 创建 AbstractServiceCache 的实例
            mv.visitTypeInsn(Opcodes.NEW, it)
            // 复制 AbstractServiceCache 的实例以便于后面的构造函数调用
            mv.visitInsn(Opcodes.DUP)
            // 调用 AbstractServiceCache 的构造函数
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, it, "<init>", "()V", false)
            // 调用 list 的 add 方法
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
            // 弹出 add 方法返回的 boolean 结果
            mv.visitInsn(Opcodes.POP)
        }
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
    }

}