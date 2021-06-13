package com.proxy.service.utils.info;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.provider.UtilsProvider;
import com.proxy.service.utils.util.ProviderUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author : cangHX
 * on 2020/09/11  9:13 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_FILE)
public class UtilsFileServiceImpl implements CloudUtilsFileService {

    /**
     * 添加允许通过 provider 共享的文件路径，用于获取资源 Uri 等
     * 如果不设置，默认所有路径都是安全路径，建议设置
     *
     * @param filePath : 允许共享的安全路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 13:30
     */
    @Override
    public void addProviderResourcePath(@NonNull String filePath) {
        UtilsProvider.addSecurityPaths(filePath);
        Logger.Info("add Security Path. : " + filePath);
    }

    /**
     * 获取 uri
     *
     * @param file : 文件流
     * @return uri
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/27 10:18 PM
     */
    @Nullable
    @Override
    public Uri getUriForFile(@Nullable File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        }

        String authority = ProviderUtils.getProviderAuthoritiesFromManifest(UtilsProvider.class.getName(), "proxy_service_provider");
        return UtilsProvider.getUriForFile(authority, file);
    }

    /**
     * 创建 file，自动创建相关文件夹与文件
     *
     * @param path : 文件地址
     * @return 创建好的 file 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:03 PM
     */
    @Nullable
    @Override
    public File createFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
            Logger.Debug(CloudApiError.DATA_DUPLICATION.setAbout(path).build());
            return file;
        }
        try {
            File parentFile = file.getParentFile();
            if (parentFile == null) {
                return null;
            }
            if (!parentFile.exists()) {
                parentFile.mkdirs();
                if (!parentFile.exists()) {
                    Logger.Error("Directory create failed. : " + path);
                    return null;
                }
            }
            file.createNewFile();
            if (!file.exists()) {
                Logger.Error("file create failed. : " + path);
                return null;
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        if (file.exists()) {
            return file;
        }
        return null;
    }

    /**
     * 删除文件或文件夹
     *
     * @param path : 文件或文件夹地址
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:03 PM
     */
    @Override
    public boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            Logger.Debug("path can not be null");
            return false;
        }
        try {
            File file = new File(path);
            if (!file.exists()) {
                return true;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File file1 : files) {
                        deleteFile(file1.getPath());
                    }
                }
            }
            return file.delete();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return false;
    }

    /**
     * 读文件，同步执行
     *
     * @param file : 文件位置
     * @return 文件全部内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @NonNull
    @Override
    public String read(File file) {
        if (file == null || !file.exists() || file.isDirectory() || file.length() == 0) {
            Logger.Debug("Please check the file first");
            return "";
        }
        String result = "";
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] bytes = new byte[bufferedInputStream.available()];
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                result = new String(bytes, 0, len);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return result;
    }

    /**
     * 读文件，同步执行
     *
     * @param file : 文件位置
     * @return 文件每行内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @NonNull
    @Override
    public List<String> readLines(File file) {
        if (file == null || !file.exists() || file.isDirectory() || file.length() == 0) {
            Logger.Debug("Please check the file first");
            return new ArrayList<>();
        }
        ArrayList<String> lines = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return lines;
    }

    /**
     * 写文件，同步执行
     *
     * @param file   : 文件位置
     * @param data   : 内容
     * @param append : 是否续写
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @Override
    public boolean write(File file, String data, boolean append) {
        if (file == null) {
            Logger.Debug("file can not be null");
            return false;
        }
        createFile(file.getAbsolutePath());
        if (file.isDirectory()) {
            Logger.Debug("file can not be directory");
            return false;
        }
        if (data == null) {
            data = "";
        }
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
            fileChannel = fileOutputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(data.getBytes());
            fileChannel.write(byteBuffer);
            return true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 写文件，同步执行
     *
     * @param file   : 文件位置
     * @param datas  : 内容
     * @param append : 是否续写
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @Override
    public boolean writeLines(File file, List<String> datas, boolean append) {
        if (datas == null || datas.size() == 0) {
            return write(file, "", append);
        }
        StringBuilder builder = new StringBuilder();
        for (String data : datas) {
            builder.append(data).append("\n");
        }
        return write(file, builder.toString(), append);
    }

    /**
     * 写文件，同步执行
     *
     * @param src         : 源文件(可以是文件或文件夹)
     * @param dest        : 目标文件(可以是文件或文件夹)
     * @param isDeleteSrc : 源文件是否删除
     * @return true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @Override
    public boolean write(File src, File dest, boolean isDeleteSrc) {
        if (src == null || dest == null) {
            Logger.Debug(CloudApiError.DATA_EMPTY.setAbout("oldFile or newFile can not be null").build());
            return false;
        }
        if (!src.exists()) {
            Logger.Debug(CloudApiError.DATA_EMPTY.setAbout("The target file does not exist. with : " + src.getAbsolutePath()).build());
            return false;
        }

        if (dest.exists() && dest.length() > 0) {
            Logger.Debug("The destination location file already exists. with : " + dest.getAbsolutePath());
            return false;
        }

        if (dest.isDirectory()) {
            if (src.isDirectory()) {
                File[] files = src.listFiles();
                if (files == null) {
                    Logger.Debug(CloudApiError.DATA_EMPTY.setAbout("The directory file does not empty. with : " + src.getAbsolutePath()).build());
                    return false;
                }
                for (File file : files) {
                    if (file.isDirectory()) {
                        String name = file.getName();
                        File newFile = new File(dest, name);
                        write(file, newFile, isDeleteSrc);
                    } else {
                        writeFile(file, dest, isDeleteSrc);
                    }
                }
                return true;
            } else {
                dest = new File(dest, src.getName());
                return writeFile(src, dest, isDeleteSrc);
            }
        } else {
            if (src.isDirectory()) {
                Logger.Debug(CloudApiError.DATA_TYPE_ERROR.setAbout("Src is different from dest. src = " + src + " ; dest = " + dest).build());
                return false;
            } else {
                return writeFile(src, dest, isDeleteSrc);
            }
        }
    }

    /**
     * 写文件
     */
    private boolean writeFile(File src, File dest, boolean isDeleteSrc) {
        if (src.length() == 0) {
            if (isDeleteSrc) {
                deleteFile(src.getAbsolutePath());
            }
            File file = createFile(dest.getAbsolutePath());
            return file != null;
        }

        File parent = dest.getParentFile();
        if (parent != null) {
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }

        if (isDeleteSrc) {
            try {
                if (src.renameTo(dest)) {
                    return true;
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }

        FileInputStream fileInputStream = null;
        FileChannel fileInputChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileOutputChannel = null;
        try {
            fileInputStream = new FileInputStream(src);
            fileInputChannel = fileInputStream.getChannel();
            fileOutputStream = new FileOutputStream(dest);
            fileOutputChannel = fileOutputStream.getChannel();

            fileOutputChannel.transferFrom(fileInputChannel, 0, fileInputChannel.size());

            if (isDeleteSrc) {
                src.delete();
            }
            return true;
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                if (fileInputChannel != null) {
                    fileInputChannel.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                if (fileOutputChannel != null) {
                    fileOutputChannel.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return false;
    }

    /**
     * 写文件，同步执行
     *
     * @param is        : 文件流
     * @param localFile : 本地文件
     * @param seek      : 偏移位置
     * @param callback  : 进度回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 10:47 PM
     */
    @Override
    public boolean write(InputStream is, File localFile, long seek, ProgressCallback callback) {
        if (is == null || localFile == null) {
            Logger.Debug("inputStream or localFile can not be null");
            return false;
        }
        if (localFile.isDirectory()) {
            Logger.Debug("localFile can not be directory");
            return false;
        }
        if (callback == null) {
            callback = new ProgressCallback() {
                @Override
                public void onProgress(long progress) {
                }

                @Override
                public boolean isCancel() {
                    return false;
                }
            };
        }

        if (seek < 0) {
            seek = 0;
        }

        int size = 8 * 1024;

        RandomAccessFile accessFile = null;
        FileChannel fileChannel = null;
        try {
            accessFile = new RandomAccessFile(localFile, "rw");
            accessFile.seek(seek);
            fileChannel = accessFile.getChannel();

            byte[] b = new byte[size];
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            long progress = seek;
            int len;
            while ((len = is.read(b)) != -1) {
                if (callback.isCancel()) {
                    Logger.Debug("cancel");
                    return false;
                }
                byteBuffer.put(b, 0, len);
                byteBuffer.flip();

                fileChannel.write(byteBuffer);

                byteBuffer.clear();

                progress += len;
                callback.onProgress(progress);
            }
            return true;
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            try {
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                is.close();
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return false;
    }

    /**
     * 压缩
     *
     * @param in      : 准备压缩的文件或文件夹
     * @param outDir  : 压缩包路径
     * @param zipName : 压缩包名称
     * @return 是否压缩成功, true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/10 9:51 PM
     */
    @Override
    public boolean zip(File in, String outDir, String zipName) {
        if (in == null) {
            Logger.Debug(CloudApiError.DATA_EMPTY.setAbout("in can not be null").build());
            return false;
        }

        if (TextUtils.isEmpty(outDir) || TextUtils.isEmpty(zipName)) {
            Logger.Debug(CloudApiError.DATA_EMPTY.setAbout("outDir or zipName can not be empty").build());
            return false;
        }

        File parent = new File(outDir);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        ZipOutputStream outputStream = null;
        try {
            File file = new File(parent, zipName + ".zip");
            outputStream = new ZipOutputStream(new FileOutputStream(file));
            zip(in, outputStream, zipName);
            return file.exists();
        } catch (Throwable e) {
            Logger.Debug(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.finish();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return false;
    }

    /**
     * 生成压缩包
     */
    private void zip(File in, ZipOutputStream out, String fileName) throws Throwable {
        if (in.isDirectory()) {
            out.putNextEntry(new ZipEntry(fileName + File.separator));
            File[] files = in.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                zip(file, out, fileName + File.separator + file.getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(fileName));
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(in));
            byte[] bytes = new byte[8 * 1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
//            out.flush();
            inputStream.close();
        }
    }

}
