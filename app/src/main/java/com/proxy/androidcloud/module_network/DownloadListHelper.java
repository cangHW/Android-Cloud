package com.proxy.androidcloud.module_network;

import android.content.Context;
import android.widget.Toast;

import com.proxy.androidcloud.base.BaseApplication;
import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.download.CloudDownloadCallback;
import com.proxy.service.api.callback.download.CloudNotificationCallback;
import com.proxy.service.api.download.CloudDownloadState;
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
public class DownloadListHelper extends AbstractListHelper {

    private Logger logger = Logger.create("download");
    private CloudNetWorkDownloadService service;
    private int mDownloadId = -1;

    public DownloadListHelper() {
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
        ).setGlobalMultiProcessEnable(false);
    }

    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> list = new ArrayList<>();
        list.add(HelperItemInfo.builder()
                .setId(0)
                .setTitle("开始下载")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(1)
                .setTitle("暂停下载")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(2)
                .setTitle("继续下载")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(3)
                .setTitle("取消下载")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(4)
                .setTitle("删除下载")
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
                        .setNotificationCallback(notificationCallback)
                        .setTaskName("手助")
                        .setFileName("app-develop-release.apk")
                        .setFileUrl("https://app.api.sj.360.cn/url/download/id/4050514/from/web_detail");
                mDownloadId = service.start(builder.build());
                break;
            case 1:
                service.pause(mDownloadId);
                break;
            case 2:
                service.continues(mDownloadId);
                break;
            case 3:
                service.cancel(mDownloadId);
                break;
            case 4:
//                service.delete(mDownloadId);
                service.getDownloadState(mDownloadId);
                break;
            default:
        }
    }

    private CloudNotificationCallback notificationCallback = new CloudNotificationCallback() {
        @Override
        public void onNotificationClick(int downloadId) {
            Toast.makeText(BaseApplication.getInstance(), "通知点击", Toast.LENGTH_SHORT).show();
            int downloadState = service.getDownloadState(downloadId);
            switch (downloadState) {
                case CloudDownloadState.START:
                case CloudDownloadState.CONTINUES:
                case CloudDownloadState.LOADING:
                    service.pause(downloadId);
                    break;
                case CloudDownloadState.PAUSE:
                    service.continues(downloadId);
                    break;
                default:
            }
        }
    };

    private CloudDownloadCallback downloadCallback = new CloudDownloadCallback() {
        @Override
        public void onStart(CloudNetWorkDownloadInfo info) {
            Toast.makeText(BaseApplication.getInstance(), "开始下载", Toast.LENGTH_SHORT).show();
            logger.debug("onStart");
        }

        @Override
        public void onProgress(CloudNetWorkDownloadInfo info, long progress, long total) {
            logger.debug("onProgress with : progress = " + progress + " total = " + total);
        }

        @Override
        public void onPause(CloudNetWorkDownloadInfo info) {
            Toast.makeText(BaseApplication.getInstance(), "下载暂停", Toast.LENGTH_SHORT).show();
            logger.debug("onPause");
        }

        @Override
        public void onContinue(CloudNetWorkDownloadInfo info) {
            Toast.makeText(BaseApplication.getInstance(), "下载继续", Toast.LENGTH_SHORT).show();
            logger.debug("onContinue");
        }

        @Override
        public void onWarning(String errorMsg) {
            logger.debug("onWarning with : " + errorMsg);
        }

        @Override
        public void onSuccess(CloudNetWorkDownloadInfo info) {
            Toast.makeText(BaseApplication.getInstance(), "下载完成", Toast.LENGTH_SHORT).show();
            logger.debug("onSuccess. with : " + info.getFileDir() + info.getFileName());
        }

        @Override
        public void onFailed(String errorMsg) {
            Toast.makeText(BaseApplication.getInstance(), "下载失败", Toast.LENGTH_SHORT).show();
            logger.debug("onFailed with : " + errorMsg);
        }
    };
}
