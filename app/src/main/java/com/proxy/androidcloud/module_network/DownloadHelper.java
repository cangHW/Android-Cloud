package com.proxy.androidcloud.module_network;

import android.content.Context;

import com.proxy.androidcloud.helper.AbstractHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.download.CloudDownloadCallback;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.download.CloudNetWorkNotificationInfo;
import com.proxy.service.api.services.CloudNetWorkDownloadService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/09/10  10:13 PM
 */
public class DownloadHelper extends AbstractHelper {

    private Logger logger = Logger.create("download");
    private CloudNetWorkDownloadService service;

    public DownloadHelper() {
        service = CloudSystem.getService(CloudServiceTagNetWork.NET_WORK_DOWNLOAD);
        if (service == null) {
            return;
        }

        service.setGlobalNotificationBuilder(
                CloudNetWorkNotificationInfo.builder()
                        .setChannelId("1222")
                        .setChannelName("下载")
                        .setChannelLevel(CloudNetWorkNotificationInfo.ChannelLevel.LOW)
                        .build()
        ).setGlobalMultiProcessEnable(true);
    }

    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> list = new ArrayList<>();
        list.add(HelperItemInfo.builder()
                .setId(0)
                .setTitle("开始下载")
                .build());

        return list;
    }

    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (service == null) {
            return;
        }
        switch (itemInfo.id) {
            case 0:
                CloudNetWorkDownloadInfo.Builder builder = CloudNetWorkDownloadInfo.builder()
                        .setDownloadCallback(downloadCallback)
                        .setNotificationEnable(true)
                        .setFileName("app-develop-release.apk")
                        .setFileUrl("https://test-mclient.e.360.cn/app-develop-release.apk");
                service.start(builder.build());
                break;
            default:
        }
    }


    private CloudDownloadCallback downloadCallback = new CloudDownloadCallback() {
        @Override
        public void onStart(CloudNetWorkDownloadInfo info) {
            logger.debug("onStart");
        }

        @Override
        public void onProgress(CloudNetWorkDownloadInfo info, long progress, long total) {
            logger.debug("onProgress with : progress = " + progress + " total = " + total);
        }

        @Override
        public void onPause(CloudNetWorkDownloadInfo info) {
            logger.debug("onPause");
        }

        @Override
        public void onContinue(CloudNetWorkDownloadInfo info) {
            logger.debug("onContinue");
        }

        @Override
        public void onWarning(String errorMsg) {
            logger.debug("onWarning with : " + errorMsg);
        }

        @Override
        public void onSuccess(CloudNetWorkDownloadInfo info) {
            logger.debug("onSuccess. with : " + info.getFileDir() + info.getFileName());
        }

        @Override
        public void onFailed(String errorMsg) {
            logger.debug("onFailed with : " + errorMsg);
        }
    };
}
