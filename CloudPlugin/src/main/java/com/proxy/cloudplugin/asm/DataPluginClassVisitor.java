package com.proxy.cloudplugin.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.List;

/**
 * @author: cangHX
 * @data: 2024/3/15 12:56
 * @desc:
 */
class DataPluginClassVisitor extends ClassVisitor {

    private List<String> services = null;

    public DataPluginClassVisitor(ClassVisitor cv, List<String> services) {
        super(Opcodes.ASM9, cv);
        this.services = services;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ("getClasses".equals(name) && "(Ljava/util/List;)Ljava/util/List;".equals(descriptor)) {
            System.out.println("visitMethod -> access ：" + access + ", name : " + name + ", descriptor : " + descriptor + ", signature : " + signature + ", exceptions : " + Arrays.toString(exceptions));
            return new DataPluginMethodVisitor(mv, services);
        }
        return mv;
    }

}