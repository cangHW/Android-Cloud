package com.proxy.cloudplugin.gradle_7.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * cangHX
 * on 2024/3/19 18:14
 */
public class FileUtils {

    private static CopyOption[] addCopyAttributes(CopyOption... copyOptions) {
        CopyOption[] actual = Arrays.copyOf(copyOptions, copyOptions.length + 1);
        Arrays.sort(actual, 0, copyOptions.length);
        if (Arrays.binarySearch(copyOptions, 0, copyOptions.length, StandardCopyOption.COPY_ATTRIBUTES) >= 0) {
            return Arrays.copyOf(copyOptions, copyOptions.length + 1);
        } else {
            actual[actual.length - 1] = StandardCopyOption.COPY_ATTRIBUTES;
            return actual;
        }
    }

    public static void copyDirectory(File srcDir, File destDir) {
        try {
            copyDirectory(srcDir, destDir, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
        copyDirectory(
                srcDir,
                destDir,
                filter,
                preserveFileDate,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    public static void copyDirectory(
            File srcDir,
            File destDir,
            FileFilter fileFilter,
            boolean preserveFileDate,
            CopyOption... copyOptions
    ) throws IOException {
        requireFileCopy(srcDir, destDir);
        requireDirectory(srcDir, "srcDir");
        requireCanonicalPathsNotEquals(srcDir, destDir);
        ArrayList<String> exclusionList = null;
        String srcDirCanonicalPath = srcDir.getCanonicalPath();
        String destDirCanonicalPath = destDir.getCanonicalPath();
        if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
            File[] srcFiles = listFiles(srcDir, fileFilter);
            if (srcFiles.length > 0) {
                exclusionList = new ArrayList<>(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }
        if (preserveFileDate) {
            doCopyDirectory(
                    srcDir,
                    destDir,
                    fileFilter,
                    exclusionList,
                    preserveFileDate,
                    addCopyAttributes(copyOptions)
            );
        } else {
            doCopyDirectory(
                    srcDir,
                    destDir,
                    fileFilter,
                    exclusionList,
                    preserveFileDate,
                    copyOptions
            );
        }

    }

    public static void copyFile(File srcFile, File destFile) {
        try {
            copyFile(
                    srcFile,
                    destFile,
                    StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyFile(File srcFile, File destFile, CopyOption... copyOptions) throws IOException {
        requireFileCopy(srcFile, destFile);
        requireFile(srcFile, "srcFile");
        requireCanonicalPathsNotEquals(srcFile, destFile);
        createParentDirectories(destFile);
        requireFileIfExists(destFile, "destFile");
        if (destFile.exists()) {
            requireCanWrite(destFile, "destFile");
        }
        Files.copy(srcFile.toPath(), destFile.toPath(), copyOptions);
        requireEqualSizes(srcFile, destFile, srcFile.length(), destFile.length());
    }

    private static File createParentDirectories(File file) throws IOException {
        return mkdirs(getParentFile(file));
    }

    private static void doCopyDirectory(
            File srcDir,
            File destDir,
            FileFilter fileFilter,
            List<String> exclusionList,
            boolean preserveDirDate,
            CopyOption... copyOptions
    ) throws IOException {
        File[] srcFiles = listFiles(srcDir, fileFilter);
        requireDirectoryIfExists(destDir, "destDir");
        mkdirs(destDir);
        requireCanWrite(destDir, "destDir");

        for (File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(
                            srcFile,
                            dstFile,
                            fileFilter,
                            exclusionList,
                            preserveDirDate,
                            copyOptions
                    );
                } else {
                    copyFile(srcFile, dstFile, copyOptions);
                }
            }
        }

        if (preserveDirDate) {
            setLastModified(srcDir, destDir);
        }
    }

    private static File getParentFile(File file) {
        return file.getParentFile();
    }

    public static Long lastModified(File file) throws IOException {
        return Files.getLastModifiedTime(Objects.requireNonNull(file.toPath(), "file"))
                .toMillis();
    }

    private static File[] listFiles(File directory, FileFilter fileFilter) throws IOException {
        requireDirectoryExists(directory, "directory");
        File[] files = null;
        if (fileFilter == null) {
            files = directory.listFiles();
        } else {
            files = directory.listFiles(fileFilter);
        }
        if (files == null) {
            throw new IOException("Unknown I/O error listing contents of directory: " + directory);
        }
        return files;
    }

    private static File mkdirs(File directory) throws IOException {
        if (directory != null && !directory.mkdirs() && !directory.isDirectory()) {
            throw new IOException("Cannot create directory '" + directory + "'.");
        }
        return directory;
    }

    private static void requireCanonicalPathsNotEquals(File file1, File file2) throws IOException {
        String canonicalPath = file1.getCanonicalPath();
        if (canonicalPath.equals(file2.getCanonicalPath())) {
            String msg = String.format(
                    "File canonical paths are equal: '%s' (file1='%s', file2='%s')",
                    canonicalPath,
                    file1,
                    file2
            );
            throw new IllegalArgumentException(msg);
        }
    }

    private static void requireCanWrite(File file, String name) {
        Objects.requireNonNull(file, "file");
        if (!file.canWrite()) {
            throw new IllegalArgumentException("File parameter '" + name + "' is not writable: '" + file + "'");
        }
    }

    private static File requireDirectory(File directory, String name) {
        Objects.requireNonNull(directory, name);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
        }
        return directory;
    }

    private static File requireDirectoryExists(File directory, String name) {
        requireExists(directory, name);
        requireDirectory(directory, name);
        return directory;
    }

    private static File requireDirectoryIfExists(File directory, String name) {
        Objects.requireNonNull(directory, name);
        if (directory.exists()) {
            requireDirectory(directory, name);
        }
        return directory;
    }

    private static void requireEqualSizes(File srcFile, File destFile, Long srcLen, Long dstLen) throws IOException {
        if (!Objects.equals(srcLen, dstLen)) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
        }
    }

    private static File requireExists(File file, String fileParamName) {
        Objects.requireNonNull(file, fileParamName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        }
        return file;
    }

    private static File requireExistsChecked(File file, String fileParamName) throws FileNotFoundException {
        Objects.requireNonNull(file, fileParamName);
        if (!file.exists()) {
            throw new FileNotFoundException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        }
        return file;
    }

    private static File requireFile(File file, String name) {
        Objects.requireNonNull(file, name);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a file: " + file);
        }
        return file;
    }

    private static void requireFileCopy(File source, File destination) throws FileNotFoundException {
        requireExistsChecked(source, "source");
        Objects.requireNonNull(destination, "destination");
    }

    private static File requireFileIfExists(File file, String name) {
        Objects.requireNonNull(file, name);
        if (file.exists()) {
            requireFile(file, name);
        }
        return file;
    }

    private static void setLastModified(File sourceFile, File targetFile) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        setLastModified(targetFile, lastModified(sourceFile));
    }

    private static void setLastModified(File file, long timeMillis) throws IOException {
        Objects.requireNonNull(file, "file");
        if (!file.setLastModified(timeMillis)) {
            throw new IOException(String.format("Failed setLastModified(%s) on '%s'", timeMillis, file));
        }
    }

}