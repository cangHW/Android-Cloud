package com.proxy.service.utils.info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.services.CloudUtilsShareService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  12:42
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_SHARE)
public class UtilsShareServiceImpl implements CloudUtilsShareService {

    /**
     * 打开系统分享，文字
     *
     * @param info  : 分享文字内容
     * @param title : 分享的标题
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:14
     */
    @Override
    public void openSystemShareTxt(@Nullable String info, @Nullable String title) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        if (info == null) {
            info = "";
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);

        Intent choose = Intent.createChooser(intent, title);
        choose.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(choose);
    }

    /**
     * 打开系统分享，图片
     *
     * @param imgPath : 图片地址
     * @param title   : 分享的标题
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:15
     */
    @Override
    public void openSystemShareImg(@NonNull String imgPath, @Nullable String title) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        Uri imgUri = null;
        try {
            CloudUtilsFileService fileService = new UtilsFileServiceImpl();
            imgUri = fileService.getUriForFile(new File(imgPath));
        } catch (Throwable throwable) {
            Logger.Error(CloudApiError.DATA_ERROR.setAbout("the img is error on " + imgPath).build(), throwable);
        }

        if (imgUri == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.build());
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imgUri);
        intent.setType("image/*");

        Intent choose = Intent.createChooser(intent, title);
        choose.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(choose);
    }

    /**
     * 打开系统分享，图片
     *
     * @param imgPaths : 图片地址集合
     * @param title    : 分享的标题
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:15
     */
    @Override
    public void openSystemShareImg(@Nullable List<String> imgPaths, @Nullable String title) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        ArrayList<Uri> imageUris = new ArrayList<>();
        if (imgPaths != null) {
            CloudUtilsFileService fileService = new UtilsFileServiceImpl();
            for (String path : imgPaths) {
                try {
                    imageUris.add(fileService.getUriForFile(new File(path)));
                } catch (Throwable throwable) {
                    Logger.Error(CloudApiError.DATA_ERROR.setAbout("the img is error on " + path).build(), throwable);
                }
            }
        }

        if (imageUris.size() == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.build());
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.setType("image/*");

        Intent choose = Intent.createChooser(intent, title);
        choose.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(choose);
    }
}
