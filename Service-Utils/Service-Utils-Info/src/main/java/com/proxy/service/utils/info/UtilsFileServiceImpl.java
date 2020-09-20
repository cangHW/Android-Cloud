package com.proxy.service.utils.info;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
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
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            file.createNewFile();
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        try {
            File file = new File(path);
            file.delete();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
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
            return "";
        }
        StringBuilder result = new StringBuilder();
        FileReader reader = null;
        int length;
        char[] buffer = new char[4 * 1024];
        try {
            reader = new FileReader(file);
            while ((length = reader.read(buffer)) != -1) {
                result.append(buffer, 0, length);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return result.toString();
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
            return new ArrayList<>();
        }
        ArrayList<String> lines = new ArrayList<>();
        FileReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                if (reader != null) {
                    reader.close();
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
    public void write(File file, String data, boolean append) {
        if (file == null) {
            return;
        }
        createFile(file.getAbsolutePath());
        if (file.isDirectory()) {
            return;
        }
        if (data == null) {
            data = "";
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, append);
            writer.write(data);
            writer.flush();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
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
    public void writeLines(File file, List<String> datas, boolean append) {
        if (datas == null || datas.size() == 0) {
            write(file, "", append);
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String data : datas) {
            builder.append(data);
        }
        write(file, builder.toString(), append);
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
            Logger.Debug("oldFile or newFile can not null");
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

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(oldFile);
            outputStream = new FileOutputStream(newFile);

            byte[] bytes = new byte[4 * 1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            return true;
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
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
     * @param oldFile : 旧位置或旧名称
     * @param newFile : 新位置或新名称
     * @param append  : 是否续写
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @Override
    public void write(File oldFile, File newFile, boolean append) {
        if (!append) {
            if (newFile != null) {
                deleteFile(newFile.getAbsolutePath());
            }
            write(oldFile, newFile);
            return;
        }
        if (oldFile == null || !oldFile.exists() || oldFile.length() == 0) {
            createFile(newFile.getAbsolutePath());
            return;
        }
        if (oldFile.isDirectory()) {
            Logger.Debug("The target file does not exist. with : " + oldFile.getAbsolutePath());
            return;
        }
        String data = read(oldFile);
        write(newFile, data, true);
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
    public void write(InputStream is, File localFile, long seek, ProgressCallback callback) {
        if (is == null || localFile == null) {
            return;
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
        try {
            localFile = createFile(localFile.getAbsolutePath());
            if (localFile == null || localFile.isDirectory()) {
                return;
            }
            RandomAccessFile accessFile = new RandomAccessFile(localFile, "rw");
            accessFile.seek(seek);
            byte[] b = new byte[4 * 1024];
            long progress = seek;
            int len;
            while ((len = is.read(b)) != -1) {
                if (callback.isCancel()) {
                    return;
                }
                accessFile.write(b, 0, len);
                progress += len;
                callback.onProgress(progress);
            }
            accessFile.close();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                is.close();
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }
}
