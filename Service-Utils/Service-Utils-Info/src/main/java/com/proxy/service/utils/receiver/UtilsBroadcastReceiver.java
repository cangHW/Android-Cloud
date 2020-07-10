package com.proxy.service.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.utils.util.HandleUtils;

import java.util.HashMap;
import java.util.Map;

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

    private static final Object EMPTY_OBJECT = new Object();
    private Map<ReceiverListener, Object> mReceiverListeners = new HashMap<>();

    private static class Factory {
        private static UtilsBroadcastReceiver mInstance = new UtilsBroadcastReceiver();
    }

    private UtilsBroadcastReceiver() {
    }

    public static UtilsBroadcastReceiver getInstance() {
        return Factory.mInstance;
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
