package com.proxy.cloudplugin.inject

import com.proxy.cloudplugin.asm.DataPluginClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2024/3/14 17:14
 * @desc:
 */
object ServiceInject {

    fun inject(services: ArrayList<String>, inputStream: InputStream): ByteArray {
        val cr = ClassReader(inputStream)
        val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        val classVisitor = DataPluginClassVisitor(cw, services)
        cr.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

}