package com.proxy.cloudplugin.gradle_7.utils

import com.proxy.cloudplugin.PluginImpl
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * @author: cangHX
 * @data: 2024/3/19 17:21
 * @desc:
 */
object JarUtils {

    private val DIGITS_LOWER: CharArray =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    fun write(src: ByteArray, dest: File) {
        if (!dest.exists()){
            dest.parentFile.mkdirs()
            dest.createNewFile()
        }
        val fileOutputStream = FileOutputStream(dest)
        fileOutputStream.write(src)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

    fun refreshJar(file: File) {
        val tempFile = File(file.parent, file.name + ".temp")
        if (tempFile.exists()) {
            tempFile.delete()
        }
        val outputStream = JarOutputStream(FileOutputStream(tempFile))

        val jarFile = JarFile(file)
        jarFile.entries().iterator().forEach loop@{ jarEntry ->
            val zipEntry = ZipEntry(jarEntry.name)
            outputStream.putNextEntry(zipEntry)
            if (jarEntry.name.endsWith(PluginImpl.className)) {
                val dataFile = File(tempFile.parent, PluginImpl.className)
                val inputStream = FileInputStream(dataFile)
                outputStream.write(inputStream.readAllBytes())
                inputStream.close()
                dataFile.delete()
            } else {
                val inputStream = jarFile.getInputStream(jarEntry)
                outputStream.write(inputStream.readAllBytes())
                inputStream.close()
            }
            outputStream.closeEntry()
        }
        outputStream.close()
        jarFile.close()
        if (file.exists()) {
            file.delete()
        }
        tempFile.renameTo(file)
    }

    fun md5Hex(data: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val bytes = data.toByteArray(StandardCharsets.UTF_8)
        return String(encodeHex(digest.digest(bytes)))
    }

    private fun encodeHex(data: ByteArray): CharArray {
        val l = data.size
        val out = CharArray(l shl 1)

        var i = 0
        var j = 0
        while (i < 0 + l) {
            out[j++] = DIGITS_LOWER[240 and data[i].toInt() ushr 4]
            out[j++] = DIGITS_LOWER[15 and data[i].toInt()]
            ++i
        }

        return out
    }

}