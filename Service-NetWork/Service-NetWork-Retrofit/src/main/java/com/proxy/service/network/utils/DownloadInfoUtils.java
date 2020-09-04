package com.proxy.service.network.utils;

import com.proxy.service.api.download.CloudNetWorkDownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/09/04  7:26 PM
 */
public class DownloadInfoUtils {

    public interface CheckedCallback {
        /**
         * 发现符合要求的数据对象
         *
         * @param info : 符合要求的数据对象
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/9/4 7:28 PM
         */
        void onFound(CloudNetWorkDownloadInfo info);
    }

    public static void findValueById(List<CloudNetWorkDownloadInfo> list, int downloadId, CheckedCallback callback) {
        for (CloudNetWorkDownloadInfo info : new ArrayList<>(list)) {
            if (info.getDownloadId() != downloadId) {
                continue;
            }
            callback.onFound(info);
        }
    }

    public static boolean isHasValue(List<CloudNetWorkDownloadInfo> list, CloudNetWorkDownloadInfo info) {
        for (CloudNetWorkDownloadInfo downloadInfo : new ArrayList<>(list)) {
            if (downloadInfo.getDownloadId() != info.getDownloadId()) {
                continue;
            }
            return true;
        }
        return false;
    }

}
