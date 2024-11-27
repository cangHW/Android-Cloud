package com.proxy.service.utils.info;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContentResolverCompat;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.services.CloudUtilsSystemService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.log.Logger;
import com.proxy.service.utils.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * @author: cangHX
 * on 2020/06/18  13:49
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_SYSTEM)
public class UtilsSystemServiceImpl implements CloudUtilsSystemService {

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    @Override
    public void openAppSetting(@Nullable String packageName) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        if (TextUtils.isEmpty(packageName)) {
            CloudUtilsAppService service = new UtilsAppServiceImpl();
            packageName = service.getPackageName();
        }

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 打开应用通知设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @param uid         : 应用的uid，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    @Override
    public void openNotificationSetting(@Nullable String packageName, @Nullable String uid) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            openAppSetting(packageName);
            return;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        CloudUtilsAppService service = new UtilsAppServiceImpl();
        if (TextUtils.isEmpty(packageName)) {
            packageName = service.getPackageName();
        }
        if (TextUtils.isEmpty(uid)) {
            uid = service.getUid();
        }

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
        intent.putExtra("app_package", packageName);
        intent.putExtra("app_uid", uid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param phoneNumber : 准备拨打的电话号码
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-18 14:26
     */
    @Override
    public void openCall(@Nullable String phoneNumber) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        Intent intent = new Intent();
        if (TextUtils.isEmpty(phoneNumber)) {
            intent.setAction(Intent.ACTION_CALL_BUTTON);
        } else {
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 展示文件在相册中，会把文件复制到相册对应的文件夹下面
     *
     * @param folderName : 文件夹名称，可以为空
     * @param fileName   : 文件名称，可以为空，默认从路径中获取
     * @param mimeType   : 文件格式，可以为空
     * @param filePath   : 文件路径，不能为空
     * @return: 是否成功
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/19 22:14
     */
    @WorkerThread
    @Override
    public boolean showFileInAlbum(@Nullable String folderName, @Nullable String fileName, @Nullable String mimeType, @NonNull String filePath) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return false;
        }
        String dirFolderName = "DCIM";
        if (TextUtils.isEmpty(fileName)) {
            fileName = new File(filePath).getName();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            StringBuilder destPath = new StringBuilder(Environment.getExternalStoragePublicDirectory(dirFolderName).getAbsolutePath());
            if (!TextUtils.isEmpty(folderName)) {
                destPath.append(File.separator).append(folderName);
            }
            destPath.append(File.separator).append(fileName);
            CloudUtilsFileService fileService = new UtilsFileServiceImpl();
            boolean isSuccess = fileService.write(new File(filePath), new File(destPath.toString()), false);
            if (!isSuccess) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(new File(filePath)));
            context.sendBroadcast(intent);
            return true;
        }

        return writeToAlbum(context, dirFolderName, folderName, fileName, mimeType, filePath);
    }

    /**
     * 兼容 Android 10，写文件到相册
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean writeToAlbum(Context context, String dirFolderName, String folderName, String fileName, String mimeType, String filePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        if (TextUtils.isEmpty(mimeType)) {
            values.put(MediaStore.Images.Media.MIME_TYPE, FileUtils.getExtension(filePath));
        } else {
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        }
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.RELATIVE_PATH, dirFolderName + File.separator + folderName);
        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        Uri insertUri = resolver.insert(external, values);
        if (insertUri == null) {
            return false;
        }
        OutputStream os = null;
        try {
            os = resolver.openOutputStream(insertUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (os == null) {
            return false;
        }
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            byte[] bytes = new byte[8 * 1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            return true;
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                os.flush();
                os.close();
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return false;
    }

}
