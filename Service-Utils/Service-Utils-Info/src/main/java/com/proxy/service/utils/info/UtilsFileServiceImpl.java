package com.proxy.service.utils.info;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

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
            return false;
        }
        if (newFile.exists() && newFile.length() > 0) {
            Logger.Debug("The destination location file already exists. with : " + newFile.getAbsolutePath());
            return false;
        }
        if (!oldFile.exists()) {
            Logger.Debug("The target file does not exist. with : " + oldFile.getAbsolutePath());
            return false;
        }
        if (oldFile.length() == 0) {
            deleteFile(oldFile.getAbsolutePath());
            File file = createFile(newFile.getAbsolutePath());
            return file != null;
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
                if (is != null) {
                    is.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }
}
