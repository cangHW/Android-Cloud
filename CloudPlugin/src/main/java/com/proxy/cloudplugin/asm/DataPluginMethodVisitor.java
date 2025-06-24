package com.proxy.cloudplugin.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * @author: cangHX
 * @data: 2024/3/15 12:59
 * @desc:
 */
class DataPluginMethodVisitor extends MethodVisitor {

    private final List<String> services;

    public DataPluginMethodVisitor(MethodVisitor methodVisitor, List<String> services) {
        super(Opcodes.ASM9, methodVisitor);
        this.services = services;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("visitCode");
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
//        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            services.forEach(it -> {
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitLdcInsn(it);
                mv.visitMethodInsn(
                        Opcodes.INVOKEINTERFACE,
                        "java/util/List",
                        "add",
                        "(Ljava/lang/Object;)Z",
                        true
                );
                mv.visitInsn(Opcodes.POP);

                System.out.println("DataPlugin -> 插入：" + it);
            });
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        System.out.println("visitMaxs -> maxStack ：" + maxStack + ", maxLocals ：" + maxLocals);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        System.out.println("visitEnd");
    }
}