package com.proxy.androidcloud.helper;

import com.proxy.androidcloud.module_library.ThreadPoolHelper;
import com.proxy.androidcloud.module_network.DownloadHelper;

/**
 * @author : cangHX
 * on 2020/08/13  10:06 PM
 */
public enum HelperType {

    /**
     * 线程池
     */
    THREAD_POOL {
        @Override
        public String serviceName() {
            return "CloudUtilsTaskService";
        }

        @Override
        public AbstractHelper create() {
            return new ThreadPoolHelper();
        }
    },

    /**
     * 下载
     */
    DOWNLOAD {
        @Override
        public String serviceName() {
            return "CloudNetWorkDownloadService";
        }

        @Override
        public AbstractHelper create() {
            return new DownloadHelper();
        }
    };

    public abstract String serviceName();

    public abstract AbstractHelper create();
}
