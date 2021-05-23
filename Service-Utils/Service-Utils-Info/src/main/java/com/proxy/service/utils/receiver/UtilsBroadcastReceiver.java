package com.proxy.service.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.utils.info.UtilsTaskServiceImpl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 广播接收中心
 *
 * @author: cangHX
 * on 2020/06/24  17:26
 */
public class UtilsBroadcastReceiver extends BroadcastReceiver {

    /**
     * 接收消息回调
     */
    public interface ReceiverListener {
        /**
         * 接收到消息
         *
         * @param context: 上下文环境
         * @param intent   : 意图
         * @version: 1.0
         * @author: cangHX
         * @date: 2020-06-24 18:04
         */
        void onReceive(Context context, Intent intent);
    }

    private final List<WeakReference<ReceiverListener>> mReceiverListeners = new ArrayList<>();
    private final CloudUtilsTaskService mTaskService;

    private static class Factory {
        private static final UtilsBroadcastReceiver M_INSTANCE = new UtilsBroadcastReceiver();
    }

    private UtilsBroadcastReceiver() {
        mTaskService = new UtilsTaskServiceImpl();
    }

    public static UtilsBroadcastReceiver getInstance() {
        return Factory.M_INSTANCE;
    }

    /**
     * 添加过滤器
     *
     * @param filter   : 意图过滤器
     * @param listener : 回调监听
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-28 20:59
     */
    public void addIntentFilter(IntentFilter filter, ReceiverListener listener) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        boolean hasSame = WeakReferenceUtils.checkValueIsSame(mReceiverListeners, listener);
        if (!hasSame) {
            this.mReceiverListeners.add(new WeakReference<>(listener));
        }

        context.registerReceiver(Factory.M_INSTANCE, filter);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        mTaskService.callUiThread(new Task<Object>() {
            @Override
            public Object call() {
                WeakReferenceUtils.checkValueIsEmpty(mReceiverListeners, new WeakReferenceUtils.Callback<ReceiverListener>() {
                    @Override
                    public void onCallback(WeakReference<ReceiverListener> weakReference, ReceiverListener receiverListener) {
                        try {
                            receiverListener.onReceive(context, intent);
                        } catch (Throwable ignored) {
                        }
                    }
                });
                return null;
            }
        });
    }
}
