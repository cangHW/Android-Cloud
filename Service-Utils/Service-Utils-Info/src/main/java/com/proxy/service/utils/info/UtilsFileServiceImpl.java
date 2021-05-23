package com.proxy.service.utils.info;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

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

/**
 * @author : cangHX
 * on 2020/09/11  9:13 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_FILE)
public class UtilsFileServiceImpl implements CloudUtilsFileService {
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
            return file;
        }
        try {
            File parentFile = file.getParentFile();
            if (parentFile == null) {
                return null;
            }
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    Logger.Error("Directory create failed. : " + path);
                    return null;
                }
            }
            if (!file.createNewFile()) {
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
     * 删除文件
     *
     * @param path : 文件地址
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
     * 移动文件位置与修改文件名称
     *
     * @param oldFile : 旧位置或旧名称
     * @param newFile : 新位置或新名称
     * @return true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @Override
    public boolean write(File oldFile, File newFile) {
        if (oldFile == null || newFile == null) {
            Logger.Debug("oldFile or newFile can not be null");
            return false;
        }
        if (!oldFile.exists() || oldFile.isDirectory()) {
            Logger.Debug("The target file does not exist. with : " + oldFile.getAbsolutePath());
            return false;
        }
        if (oldFile.length() == 0) {
            deleteFile(oldFile.getAbsolutePath());
            File file = createFile(newFile.getAbsolutePath());
            return file != null;
        }
        if (newFile.isDirectory()) {
            newFile = new File(newFile, oldFile.getName());
        }
        if (newFile.exists() && newFile.length() > 0) {
            Logger.Debug("The destination location file already exists. with : " + newFile.getAbsolutePath());
            return false;
        }
        try {
            if (oldFile.renameTo(newFile)) {
                return true;
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }

        FileInputStream fileInputStream = null;
        FileChannel fileInputChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileOutputChannel = null;
        try {
            fileInputStream = new FileInputStream(oldFile);
            fileInputChannel = fileInputStream.getChannel();
            fileOutputStream = new FileOutputStream(newFile);
            fileOutputChannel = fileOutputStream.getChannel();

            fileOutputChannel.transferFrom(fileInputChannel, 0, fileInputChannel.size());
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
}
