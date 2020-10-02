package com.proxy.androidcloud.helper;

import com.proxy.androidcloud.module_library.receiver.ReceiverListHelper;
import com.proxy.androidcloud.module_library.thread.ThreadPoolListHelper;
import com.proxy.androidcloud.module_library.bitmap.BitmapListHelper;
import com.proxy.androidcloud.module_network.DownloadListHelper;
import com.proxy.service.api.services.CloudNetWorkDownloadService;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.services.CloudUtilsReceiverService;
import com.proxy.service.api.services.CloudUtilsTaskService;

/**
 * @author : cangHX
 * on 2020/08/13  10:06 PM
 */
public enum ListHelperType {

    /**
     * 线程池
     */
    THREAD_POOL {
        @Override
        public String serviceName() {
            return CloudUtilsTaskService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new ThreadPoolListHelper();
        }
    },

    /**
     * 下载
     */
    DOWNLOAD {
        @Override
        public String serviceName() {
            return CloudNetWorkDownloadService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new DownloadListHelper();
        }
    },

    /**
     * 广播
     */
    RECEIVER {
        @Override
        public String serviceName() {
            return CloudUtilsReceiverService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new ReceiverListHelper();
        }
    },

    /**
     * 下载
     */
    BITMAP {
        @Override
        public String serviceName() {
            return CloudUtilsBitmapService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new BitmapListHelper();
        }
    };

    public abstract String serviceName();

    public abstract AbstractListHelper create();
}
