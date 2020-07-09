package com.proxy.service.library.info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
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
@CloudService(serviceTag = CloudServiceTagUtils.UTILS_SHARE)
public class UtilsShareServiceImpl implements CloudUtilsShareService {

    /**
     * 打开系统分享，文字
     *
     * @param info : 分享文字内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:14
     */
    @Override
    public void openSystemShareTxt(@Nullable String info) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        context.startActivity(Intent.createChooser(intent, "share"));
    }

    /**
     * 打开系统分享，图片
     *
     * @param imgPaths : 图片地址集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:15
     */
    @Override
    public void openSystemShareImg(@Nullable List<String> imgPaths) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        ArrayList<Uri> imageUris = new ArrayList<>();
        if (imgPaths != null) {
            for (String path : imgPaths) {
                try {
                    Uri imageUri = Uri.fromFile(new File(path));
                    imageUris.add(imageUri);
                } catch (Throwable throwable) {
                    Logger.Debug(CloudApiError.DATA_ERROR.append("the img is error on " + path).build(), throwable);
                }
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, "share"));
    }
}
