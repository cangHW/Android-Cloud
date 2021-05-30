package com.proxy.androidcloud.helper;

import com.proxy.androidcloud.module_library.event.EventHelper;
import com.proxy.androidcloud.module_library.file.FileHelper;
import com.proxy.androidcloud.module_library.install.AppHelper;
import com.proxy.androidcloud.module_library.lifecycle.LifecycleHelper;
import com.proxy.androidcloud.module_library.network.NetWorkListHelper;
import com.proxy.androidcloud.module_library.receiver.ReceiverListHelper;
import com.proxy.androidcloud.module_library.share.ShareListHelper;
import com.proxy.androidcloud.module_library.thread.ThreadPoolListHelper;
import com.proxy.androidcloud.module_library.bitmap.BitmapListHelper;
import com.proxy.androidcloud.module_network.DownloadListHelper;
import com.proxy.androidcloud.module_network.UploadListHelper;
import com.proxy.service.api.services.CloudNetWorkDownloadService;
import com.proxy.service.api.services.CloudNetWorkUploadService;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.services.CloudUtilsEventService;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.api.services.CloudUtilsLifecycleService;
import com.proxy.service.api.services.CloudUtilsNetWorkService;
import com.proxy.service.api.services.CloudUtilsReceiverService;
import com.proxy.service.api.services.CloudUtilsShareService;
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
     * 上传
     */
    UPLOAD {
        @Override
        public String serviceName() {
            return CloudNetWorkUploadService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new UploadListHelper();
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
     * 图片
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
    },

    /**
     * 应用管理
     */
    APP {
        @Override
        public String serviceName() {
            return CloudUtilsInstallService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new AppHelper();
        }
    },

    /**
     * 网络相关
     */
    NET_WORK {
        @Override
        public String serviceName() {
            return CloudUtilsNetWorkService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new NetWorkListHelper();
        }
    },

    /**
     * 分享相关
     */
    SHARE {
        @Override
        public String serviceName() {
            return CloudUtilsShareService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new ShareListHelper();
        }
    },

    /**
     * 生命周期相关
     */
    LIFECYCLE {
        @Override
        public String serviceName() {
            return CloudUtilsLifecycleService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new LifecycleHelper();
        }
    },

    /**
     * event 事件分发相关
     */
    EVENT {
        @Override
        public String serviceName() {
            return CloudUtilsEventService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new EventHelper();
        }
    },

    /**
     * 文件读写
     */
    FILE {
        @Override
        public String serviceName() {
            return CloudUtilsFileService.class.getSimpleName();
        }

        @Override
        public AbstractListHelper create() {
            return new FileHelper();
        }
    };

    public abstract String serviceName();

    public abstract AbstractListHelper create();
}
