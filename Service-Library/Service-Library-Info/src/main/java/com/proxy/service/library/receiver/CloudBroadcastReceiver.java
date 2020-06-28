package com.proxy.service.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.library.util.HandleUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: cangHX
 * on 2020/06/24  17:26
 * <p>
 * 广播接收中心
 */
public class CloudBroadcastReceiver extends BroadcastReceiver {

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

    private static final Object EMPTY_OBJECT = new Object();
    private Map<ReceiverListener, Object> mReceiverListeners = new HashMap<>();

    private static class Factory {
        private static CloudBroadcastReceiver mInstance = new CloudBroadcastReceiver();
    }

    private CloudBroadcastReceiver() {
    }

    public static CloudBroadcastReceiver getInstance() {
        return Factory.mInstance;
    }

    public void addIntentFilter(IntentFilter filter, ReceiverListener listener) {
        this.mReceiverListeners.put(listener, EMPTY_OBJECT);
        Context context = ContextManager.getApplication();
        context.registerReceiver(Factory.mInstance, filter);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        HandleUtils.postBg(new Runnable() {
            @Override
            public void run() {
                for (ReceiverListener listener : mReceiverListeners.keySet()) {
                    try {
                        listener.onReceive(context, intent);
                    } catch (Throwable ignored) {
                    }
                }
            }
        });
    }
}
