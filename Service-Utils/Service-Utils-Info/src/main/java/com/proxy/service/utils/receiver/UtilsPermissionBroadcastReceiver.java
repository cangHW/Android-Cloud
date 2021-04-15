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
import java.util.HashMap;
import java.util.List;

/**
 * 广播接收中心
 *
 * @author: cangHX
 * on 2020/06/24  17:26
 */
public class UtilsPermissionBroadcastReceiver {

    /**
     * 接收消息回调
     */
    public interface ReceiverListener {
        /**
         * 接收到消息
         *
         * @param context    : 上下文环境
         * @param intent     : 意图
         * @param permission : 权限
         * @version: 1.0
         * @author: cangHX
         * @date: 2020-06-24 18:04
         */
        void onReceiveWithPermission(Context context, Intent intent, String permission);
    }

    private final HashMap<String, PermissionBroadcastReceiver> mReceiverMapper = new HashMap<>();
    private final List<WeakReference<ReceiverListener>> mReceiverListeners = new ArrayList<>();

    private final CloudUtilsTaskService mTaskService;


    private static class Factory {
        private static final UtilsPermissionBroadcastReceiver M_INSTANCE = new UtilsPermissionBroadcastReceiver();
    }

    private UtilsPermissionBroadcastReceiver() {
        mTaskService = new UtilsTaskServiceImpl();
    }

    public static UtilsPermissionBroadcastReceiver getInstance() {
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
    public void addIntentFilter(String permission, IntentFilter filter, ReceiverListener listener) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        PermissionBroadcastReceiver receiver = mReceiverMapper.get(permission);
        if (receiver == null) {
            receiver = new PermissionBroadcastReceiver(permission, receiverListener);
            mReceiverMapper.put(permission, receiver);
        }
        if (!WeakReferenceUtils.checkValueIsSame(mReceiverListeners, listener)) {
            mReceiverListeners.add(new WeakReference<>(listener));
        }
        context.registerReceiver(receiver, filter, permission, null);
    }

    private final ReceiverListener receiverListener = new ReceiverListener() {
        @Override
        public void onReceiveWithPermission(final Context context, final Intent intent, final String permission) {
            mTaskService.callUiThread(new Task<Object>() {
                @Override
                public Object call() {
                    WeakReferenceUtils.checkValueIsEmpty(mReceiverListeners, new WeakReferenceUtils.Callback<ReceiverListener>() {
                        @Override
                        public void onCallback(ReceiverListener receiverListener) {
                            try {
                                receiverListener.onReceiveWithPermission(context, intent, permission);
                            } catch (Throwable ignored) {
                            }
                        }
                    });
                    return null;
                }
            });
        }
    };

    private static class PermissionBroadcastReceiver extends BroadcastReceiver {

        private final String mPermission;
        private final WeakReference<ReceiverListener> mWeakReference;

        private PermissionBroadcastReceiver(String permission, ReceiverListener receiver) {
            this.mPermission = permission;
            this.mWeakReference = new WeakReference<>(receiver);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            WeakReferenceUtils.checkValueIsEmpty(mWeakReference, new WeakReferenceUtils.Callback<ReceiverListener>() {
                @Override
                public void onCallback(ReceiverListener receiverListener) {
                    try {
                        receiverListener.onReceiveWithPermission(context, intent, mPermission);
                    } catch (Throwable ignored) {
                    }
                }
            });
        }
    }

}
