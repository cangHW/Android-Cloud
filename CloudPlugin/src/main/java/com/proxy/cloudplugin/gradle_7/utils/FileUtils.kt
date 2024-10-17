package com.proxy.cloudplugin.gradle_7.utils

import java.io.File
import java.io.FileFilter
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.Arrays
import java.util.Objects


/**
 * @author: cangHX
 * @data: 2024/3/19 18:14
 * @desc:
 */
object FileUtils {

    private fun addCopyAttributes(vararg copyOptions: CopyOption): Array<CopyOption> {
        val actual = Arrays.copyOf(copyOptions, copyOptions.size + 1) as? Array<CopyOption>
        Arrays.sort(actual, 0, copyOptions.size)
        if (Arrays.binarySearch(copyOptions, 0, copyOptions.size, StandardCopyOption.COPY_ATTRIBUTES) >= 0) {
            return Arrays.copyOf(copyOptions, copyOptions.size + 1) as Array<CopyOption>
        } else {
            actual?.set(actual.size - 1, StandardCopyOption.COPY_ATTRIBUTES);
            return actual!!
        }
    }

    @Throws(IOException::class)
    fun copyDirectory(srcDir: File, destDir: File) {
        copyDirectory(srcDir, destDir, true)
    }

    @Throws(IOException::class)
    fun copyDirectory(srcDir: File, destDir: File, preserveFileDate: Boolean) {
        copyDirectory(srcDir, destDir, null as FileFilter?, preserveFileDate)
    }

    @Throws(IOException::class)
    fun copyDirectory(srcDir: File, destDir: File, filter: FileFilter?, preserveFileDate: Boolean) {
        copyDirectory(
            srcDir,
            destDir,
            filter,
            preserveFileDate,
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    @Throws(IOException::class)
    fun copyDirectory(
        srcDir: File,
        destDir: File,
        fileFilter: FileFilter?,
        preserveFileDate: Boolean,
        vararg copyOptions: CopyOption
    ) {
        requireFileCopy(srcDir, destDir)
        requireDirectory(srcDir, "srcDir")
        requireCanonicalPathsNotEquals(srcDir, destDir)
        var exclusionList: MutableList<String?>? = null
        val srcDirCanonicalPath = srcDir.canonicalPath
        val destDirCanonicalPath = destDir.canonicalPath
        if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
            val srcFiles = listFiles(srcDir, fileFilter)
            if (srcFiles.size > 0) {
                exclusionList = ArrayList(srcFiles.size)
                val var10 = srcFiles.size
                for (var11 in 0 until var10) {
                    val srcFile = srcFiles[var11]
                    val copiedFile = File(destDir, srcFile.name)
                    exclusionList.add(copiedFile.canonicalPath)
                }
            }
        }
        doCopyDirectory(
            srcDir,
            destDir,
            fileFilter,
            exclusionList,
            preserveFileDate,
            *if (preserveFileDate) addCopyAttributes(*copyOptions) else copyOptions
        )
    }

    @Throws(IOException::class)
    fun copyFile(srcFile: File, destFile: File) {
        copyFile(
            srcFile,
            destFile,
            StandardCopyOption.COPY_ATTRIBUTES,
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    @Throws(IOException::class)
    private fun copyFile(srcFile: File, destFile: File, vararg copyOptions: CopyOption?) {
        requireFileCopy(srcFile, destFile)
        requireFile(srcFile, "srcFile")
        requireCanonicalPathsNotEquals(srcFile, destFile)
        createParentDirectories(destFile)
        requireFileIfExists(destFile, "destFile")
        if (destFile.exists()) {
            requireCanWrite(destFile, "destFile")
        }
        Files.copy(srcFile.toPath(), destFile.toPath(), *copyOptions)
        requireEqualSizes(srcFile, destFile, srcFile.length(), destFile.length())
    }

    @Throws(IOException::class)
    private fun createParentDirectories(file: File?): File? {
        return mkdirs(getParentFile(file))
    }

    @Throws(IOException::class)
    private fun doCopyDirectory(
        srcDir: File,
        destDir: File,
        fileFilter: FileFilter?,
        exclusionList: List<String?>?,
        preserveDirDate: Boolean,
        vararg copyOptions: CopyOption
    ) {
        val srcFiles = listFiles(srcDir, fileFilter)
        requireDirectoryIfExists(destDir, "destDir")
        mkdirs(destDir)
        requireCanWrite(destDir, "destDir")
        val var8 = srcFiles.size
        for (var9 in 0 until var8) {
            val srcFile = srcFiles[var9]
            val dstFile = File(destDir, srcFile.name)
            if (exclusionList == null || !exclusionList.contains(srcFile.canonicalPath)) {
                if (srcFile.isDirectory) {
                    doCopyDirectory(
                        srcFile,
                        dstFile,
                        fileFilter,
                        exclusionList,
                        preserveDirDate,
                        *copyOptions
                    )
                } else {
                    copyFile(srcFile, dstFile, *copyOptions)
                }
            }
        }
        if (preserveDirDate) {
            setLastModified(srcDir, destDir)
        }
    }

    private fun getParentFile(file: File?): File? {
        return file?.parentFile
    }

    @Throws(IOException::class)
    fun lastModified(file: File): Long {
        return Files.getLastModifiedTime(Objects.requireNonNull(file.toPath(), "file") as Path)
            .toMillis()
    }

    @Throws(IOException::class)
    private fun listFiles(directory: File, fileFilter: FileFilter?): Array<File> {
        requireDirectoryExists(directory, "directory")
        val files =
            if (fileFilter == null) directory.listFiles() else directory.listFiles(fileFilter)
        return files
            ?: throw IOException("Unknown I/O error listing contents of directory: $directory")
    }

    @Throws(IOException::class)
    private fun mkdirs(directory: File?): File? {
        return if (directory != null && !directory.mkdirs() && !directory.isDirectory) {
            throw IOException("Cannot create directory '$directory'.")
        } else {
            directory
        }
    }

    @Throws(IOException::class)
    private fun requireCanonicalPathsNotEquals(file1: File, file2: File) {
        val canonicalPath = file1.canonicalPath
        require(canonicalPath != file2.canonicalPath) {
            String.format(
                "File canonical paths are equal: '%s' (file1='%s', file2='%s')",
                canonicalPath,
                file1,
                file2
            )
        }
    }

    private fun requireCanWrite(file: File, name: String) {
        Objects.requireNonNull(file, "file")
        require(file.canWrite()) { "File parameter '$name is not writable: '$file'" }
    }

    private fun requireDirectory(directory: File, name: String): File? {
        Objects.requireNonNull(directory, name)
        return if (!directory.isDirectory) {
            throw IllegalArgumentException("Parameter '$name' is not a directory: '$directory'")
        } else {
            directory
        }
    }

    private fun requireDirectoryExists(directory: File, name: String): File {
        requireExists(directory, name)
        requireDirectory(directory, name)
        return directory
    }

    private fun requireDirectoryIfExists(directory: File, name: String): File? {
        Objects.requireNonNull(directory, name)
        if (directory.exists()) {
            requireDirectory(directory, name)
        }
        return directory
    }

    @Throws(IOException::class)
    private fun requireEqualSizes(srcFile: File, destFile: File, srcLen: Long, dstLen: Long) {
        if (srcLen != dstLen) {
            throw IOException("Failed to copy full contents from '$srcFile' to '$destFile' Expected length: $srcLen Actual: $dstLen")
        }
    }

    private fun requireExists(file: File, fileParamName: String): File? {
        Objects.requireNonNull(file, fileParamName)
        return if (!file.exists()) {
            throw IllegalArgumentException("File system element for parameter '$fileParamName' does not exist: '$file'")
        } else {
            file
        }
    }

    @Throws(FileNotFoundException::class)
    private fun requireExistsChecked(file: File, fileParamName: String): File? {
        Objects.requireNonNull(file, fileParamName)
        return if (!file.exists()) {
            throw FileNotFoundException("File system element for parameter '$fileParamName' does not exist: '$file'")
        } else {
            file
        }
    }

    private fun requireFile(file: File, name: String): File? {
        Objects.requireNonNull(file, name)
        return if (!file.isFile) {
            throw IllegalArgumentException("Parameter '$name' is not a file: $file")
        } else {
            file
        }
    }

    @Throws(FileNotFoundException::class)
    private fun requireFileCopy(source: File, destination: File) {
        requireExistsChecked(source, "source")
        Objects.requireNonNull(destination, "destination")
    }

    private fun requireFileIfExists(file: File, name: String): File? {
        Objects.requireNonNull(file, name)
        return if (file.exists()) requireFile(file, name) else file
    }

    @Throws(IOException::class)
    private fun setLastModified(sourceFile: File, targetFile: File) {
        Objects.requireNonNull(sourceFile, "sourceFile")
        setLastModified(targetFile, lastModified(sourceFile))
    }

    @Throws(IOException::class)
    private fun setLastModified(file: File, timeMillis: Long) {
        Objects.requireNonNull(file, "file")
        if (!file.setLastModified(timeMillis)) {
            throw IOException(String.format("Failed setLastModified(%s) on '%s'", timeMillis, file))
        }
    }

}