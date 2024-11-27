package com.proxy.service.utils.info;

import android.Manifest;
import android.content.Context;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.album.PictureInfo;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAlbumService;
import com.proxy.service.api.services.CloudUtilsPermissionService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.log.Logger;
import com.proxy.service.utils.album.AlbumManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author : cangHX
 * on 2021/05/25  9:46 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_ALBUM)
public class UtilsAlbumServiceImpl implements CloudUtilsAlbumService {
    /**
     * 获取图片，按时间排序，从新到旧
     *
     * @param callback : 回调
     * @param num      : 获取的数量, 需要大于 0
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/25 9:36 PM
     */
    @Override
    public void findPicture(@NonNull final AlbumCallback callback, final int num) {
        if (num <= 0) {
            Logger.Error(CloudApiError.DATA_ERROR.setAbout("The num cannot less than 0. method : findPicture").build());
            callback.onFailed();
            return;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            callback.onFailed();
            return;
        }

        CloudUtilsPermissionService service = new UtilsPermissionServiceImpl();
        if (!service.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Logger.Error(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.READ_EXTERNAL_STORAGE).build());
            return;
        }

        final ArrayList<PictureInfo> pictures = new ArrayList<>(num);
        AlbumManager.INSTANCE.load(context, new AlbumManager.AlbumLoadCallback() {
            @Override
            public void onLoad(PictureInfo pictureInfo) {
                try {
                    pictures.add(pictureInfo);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
            }

            @Override
            public boolean isCancel() {
                return pictures.size() >= num;
            }

            @Override
            public void loadFailed() {
                callback.onFailed();
            }

            @Override
            public void loadEnd() {
                callback.onCall(pictures);
            }
        });
    }

    /**
     * 获取全部图片，按时间排序，从新到旧
     *
     * @param callback : 回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/25 9:36 PM
     */
    @Override
    public void findAllPicture(@NonNull final AlbumCallback callback) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            callback.onFailed();
            return;
        }

        CloudUtilsPermissionService service = new UtilsPermissionServiceImpl();
        if (!service.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Logger.Error(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.READ_EXTERNAL_STORAGE).build());
            return;
        }

        final ArrayList<PictureInfo> pictures = new ArrayList<>();
        AlbumManager.INSTANCE.load(context, new AlbumManager.AlbumLoadCallback() {
            @Override
            public void onLoad(PictureInfo pictureInfo) {
                try {
                    pictures.add(pictureInfo);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
            }

            @Override
            public boolean isCancel() {
                return false;
            }

            @Override
            public void loadFailed() {
                callback.onFailed();
            }

            @Override
            public void loadEnd() {
                callback.onCall(pictures);
            }
        });
    }

    /**
     * 以文件夹形式获取图片，按时间排序，从新到旧
     *
     * @param callback : 回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/25 9:36 PM
     */
    @Override
    public void findAllPictureToGroup(@NonNull final AlbumForGroupCallback callback) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            callback.onFailed();
            return;
        }

        CloudUtilsPermissionService service = new UtilsPermissionServiceImpl();
        if (!service.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Logger.Error(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.READ_EXTERNAL_STORAGE).build());
            return;
        }

        final HashMap<String, List<PictureInfo>> hashMap = new HashMap<>();
        AlbumManager.INSTANCE.load(context, new AlbumManager.AlbumLoadCallback() {
            @Override
            public void onLoad(PictureInfo pictureInfo) {
                try {
                    String group = pictureInfo.groupName;
                    List<PictureInfo> list = hashMap.get(group);
                    if (list == null) {
                        list = new ArrayList<>();
                        hashMap.put(group, list);
                    }
                    list.add(pictureInfo);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
            }

            @Override
            public boolean isCancel() {
                return false;
            }

            @Override
            public void loadFailed() {
                callback.onFailed();
            }

            @Override
            public void loadEnd() {
                callback.onCall(hashMap);
            }
        });
    }
}
