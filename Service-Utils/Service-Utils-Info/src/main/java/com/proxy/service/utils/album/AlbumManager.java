package com.proxy.service.utils.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.proxy.service.api.album.PictureInfo;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.task.TaskCallableOnce;
import com.proxy.service.api.log.Logger;
import com.proxy.service.utils.info.UtilsTaskServiceImpl;

import java.io.File;

/**
 * @author : cangHX
 * on 2021/05/25  9:53 PM
 */
public enum AlbumManager {

    INSTANCE;

    private static final String MIME_type = MediaStore.Images.Media.MIME_TYPE;
    private static final String SORTORDER = MediaStore.Images.Media.DATE_MODIFIED;
    private static final String SELECTION = MIME_type + "=? or " + MIME_type + "=? or " + MIME_type + "=?";

    public interface AlbumLoadCallback {
        void onLoad(PictureInfo pictureInfo);

        boolean isCancel();

        void loadFailed();

        void loadEnd();
    }

    public void load(final Context context, final AlbumLoadCallback albumLoadCallback) {
        CloudUtilsTaskService service = new UtilsTaskServiceImpl();
        service.callWorkThread(new Task<Boolean>() {
            @Override
            public Boolean call() {
                return loadImg(context, albumLoadCallback);
            }
        }).mainThread().call(new TaskCallableOnce<Boolean, Object>() {
            @Override
            public Object then(ITask<Boolean> iTask) {
                if (iTask.isSuccess()) {
                    if (iTask.getResponse()) {
                        albumLoadCallback.loadEnd();
                    }
                } else {
                    albumLoadCallback.loadFailed();
                }
                return null;
            }
        });
    }

    private boolean loadImg(Context context, AlbumLoadCallback albumLoadCallback) {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] SELECTION_ARGS = new String[]{"image/jpg", "image/jpeg", "image/png"};
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            albumLoadCallback.loadFailed();
            return false;
        }
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(imageUri, null, SELECTION, SELECTION_ARGS, SORTORDER);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }

        if (cursor == null) {
            albumLoadCallback.loadFailed();
            return false;
        }

        int path_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int name_index = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
        int size_index = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
        int width_index = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
        int height_index = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
        int create_index = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
        int desc_index = cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
        while (cursor.moveToNext()) {

            if (albumLoadCallback.isCancel()) {
                break;
            }

            try {
                String path = cursor.getString(path_index);
                File file = new File(path);
                if (!file.exists()) {
                    continue;
                }

                File parentFile = file.getParentFile();
                if (parentFile == null) {
                    continue;
                }

                PictureInfo pictureInfo = new PictureInfo();
                pictureInfo.path = path;
                pictureInfo.groupName = parentFile.getName();
                pictureInfo.pictureName = cursor.getString(name_index);
                pictureInfo.size = cursor.getLong(size_index);
                pictureInfo.width = cursor.getInt(width_index);
                pictureInfo.height = cursor.getInt(height_index);
                pictureInfo.createTime = cursor.getLong(create_index);
                pictureInfo.desc = cursor.getString(desc_index);
                albumLoadCallback.onLoad(pictureInfo);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        cursor.close();
        return true;
    }

}
