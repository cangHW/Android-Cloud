package com.proxy.androidcloud.module_network;

import android.content.Context;
import android.os.Environment;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.upload.CloudUploadCallback;
import com.proxy.service.api.services.CloudNetWorkUploadService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;
//import com.proxy.service.network.upload.UploadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/11/16  2:45 PM
 */
public class UploadListHelper extends AbstractListHelper {

    private Logger logger = Logger.create("upload");
    private CloudNetWorkUploadService uploadService;

    public UploadListHelper() {
        uploadService = CloudSystem.getService(CloudServiceTagNetWork.NET_WORK_UPLOAD);
        if (uploadService == null) {
            return;
        }
        uploadService.setGlobalUploadCallback(uploadCallback);
    }

    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> list = new ArrayList<>();

        list.add(HelperItemInfo.builder()
                .setId(0)
                .setTitle("上传")
                .build());

        return list;
    }

    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (uploadService == null) {
            return;
        }
        switch (itemInfo.id){
            case 0:
//                UploadManager.getInstance().ss();
                break;
        }

    }

    private CloudUploadCallback uploadCallback = new CloudUploadCallback() {
    };
}
