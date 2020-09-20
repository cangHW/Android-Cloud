package com.proxy.service.network.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.network.download.info.DownloadInfo;
import com.proxy.service.network.download.services.DownloadService;
import com.proxy.service.network.download.services.TaskProcessService;
import com.proxy.service.network.retrofit.DownloadSeverInterface;

/**
 * @author : cangHX
 * on 2020/09/04  10:46 PM
 */
public class ServiceUtils {

    public interface StartCallback {
        /**
         * 成功
         *
         * @param severInterface : 接口对象
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/9/4 10:48 PM
         */
        void onSuccess(DownloadSeverInterface severInterface);

        /**
         * 失败
         *
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/9/4 10:48 PM
         */
        void onFailed();

        /**
         * 断开链接
         *
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/9/4 11:07 PM
         */
        void onDisconnected();
    }

    public static void startTaskProcessService(DownloadInfo info, final StartCallback callback) {
        Context application = ContextManager.getApplication();
        if (application == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            callback.onFailed();
            return;
        }
        Intent intent = new Intent(application, TaskProcessService.class);
        intent.putExtra(DownloadService.KEY, info);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            application.startForegroundService(intent);
        } else {
            application.startService(intent);
        }
        application.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    DownloadSeverInterface asInterface = DownloadSeverInterface.Stub.asInterface(service);
                    callback.onSuccess(asInterface);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                    callback.onFailed();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                callback.onDisconnected();
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
