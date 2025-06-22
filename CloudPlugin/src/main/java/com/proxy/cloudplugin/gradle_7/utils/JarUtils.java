package com.proxy.cloudplugin.gradle_7.utils;

import com.proxy.cloudplugin.PluginImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * cangHX
 * on 2024/3/19 17:21
 */
public class JarUtils {

    private static final char[] DIGITS_LOWER =new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static void write(byte[] src, File dest) {
        try {
            if (!dest.exists()){
                dest.getParentFile().mkdirs();
                dest.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(dest);
            fileOutputStream.write(src);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Throwable throwable){
            throw new RuntimeException(throwable);
        }
    }

    public static void refreshJar(File file) throws IOException {
        File tempFile = new File(file.getParent(), file.getName() + ".temp");

        if (tempFile.exists()) {
            tempFile.delete();
        }

        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(tempFile));
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            ZipEntry zipEntry = new ZipEntry(jarEntry.getName());
            outputStream.putNextEntry(zipEntry);

            if (jarEntry.getName().endsWith(PluginImpl.className)) {
                File dataFile = new File(tempFile.getParent(), PluginImpl.className);
                FileInputStream inputStream = new FileInputStream(dataFile);
                outputStream.write(inputStream.readAllBytes());
                inputStream.close();
                dataFile.delete();
            } else {
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                outputStream.write(inputStream.readAllBytes());
                inputStream.close();
            }

            outputStream.closeEntry();
        }

        outputStream.close();
        jarFile.close();

        if (file.exists()) {
            file.delete();
        }
        tempFile.renameTo(file);
    }

    public static String md5Hex(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            return new String(encodeHex(digest.digest(bytes)));
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];

        int i = 0;
        int j = 0;
        while (i < l) {
            out[j++] = DIGITS_LOWER[(240 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[15 & data[i]];
            i++;
        }

        return out;
    }

}