package com.proxy.cloudplugin.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * cangHX
 * on 2024/3/14 17:14
 */
public class ServiceInject {

    public static byte[] inject(List<String> services, InputStream inputStream) {
        try {
            ClassReader cr = new ClassReader(inputStream);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            DataPluginClassVisitor classVisitor = new DataPluginClassVisitor(cw, services);
            cr.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            inputStream.close();
            return cw.toByteArray();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}