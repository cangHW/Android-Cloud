package com.proxy.service.utils.info;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.receiver.CloudReceiverInfo;
import com.proxy.service.api.receiver.CloudReceiverListener;
import com.proxy.service.api.services.CloudUtilsReceiverService;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.receiver.ReceiverInfo;
import com.proxy.service.utils.receiver.UtilsBroadcastReceiver;
import com.proxy.service.utils.receiver.UtilsLocalBroadcastReceiver;
import com.proxy.service.utils.receiver.UtilsPermissionBroadcastReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : cangHX
 * on 2020/09/29  11:42 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_RECEIVER)
public class UtilsReceiverServiceImpl implements CloudUtilsReceiverService, UtilsBroadcastReceiver.ReceiverListener, UtilsLocalBroadcastReceiver.ReceiverListener, UtilsPermissionBroadcastReceiver.ReceiverListener {

    private static final String TYPE_ALL = "all";
    private static final String TYPE_LOCAL = "local";
    private static final String TYPE_PERMISSION = "permission";

    private static final Object LOCK = new Object();

    private static final HashMap<String, List<ReceiverInfo>> ACTION_INFO_MAPPER = new HashMap<>();
    private static final SparseArray<String> ID_ACTION_MAPPER = new SparseArray<>();
    private static final SparseArray<CloudReceiverListener> ID_RECEIVER_LISTENER_MAPPER = new SparseArray<>();

    private final CloudUtilsTaskService mTaskService = new UtilsTaskServiceImpl();

    /**
     * 添加全局接收器并设置接收范围(注意 context 泄漏问题，不用时需要移除监听)，
     * 可以接收到所有符合条件的消息
     *
     * @param receiverInfo     : 接收器信息
     * @param receiverListener : 接收器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:39 PM
     */
    @Override
    public void addReceiverListener(@NonNull CloudReceiverInfo receiverInfo, @NonNull CloudReceiverListener receiverListener) {
        synchronized (LOCK) {
            ReceiverInfo info = new ReceiverInfo();
            info.setId(receiverInfo.getId());
            info.setAction(receiverInfo.getAction());
            info.setScheme(receiverInfo.getScheme());
            info.setCategories(receiverInfo.getCategories());
            info.setType(TYPE_ALL);

            if (hasErrorWithParams(info, receiverListener)) {
                return;
            }

            IntentFilter filter = new IntentFilter();
            filter.addAction(receiverInfo.getAction());
            for (String category : receiverInfo.getCategories()) {
                filter.addCategory(category);
            }
            if (!TextUtils.isEmpty(receiverInfo.getScheme())) {
                filter.addDataScheme(receiverInfo.getScheme());
            }
            UtilsBroadcastReceiver.getInstance().addIntentFilter(filter, this);
        }
    }

    /**
     * 添加全局接收器并设置接收范围(注意 context 泄漏问题，不用时需要移除监听)，
     * 可以接收到具有对应权限，且符合条件的消息，
     * 消息发送方需要注意在 manifest 中添加正确并符合规定的权限
     *
     * @param sendPermission   : 自定义发送方需要具有的权限
     * @param receiverInfo     : 接收器信息
     * @param receiverListener : 接收器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:39 PM
     */
    @Override
    public void addReceiverListener(@NonNull String sendPermission, @NonNull CloudReceiverInfo receiverInfo, @NonNull CloudReceiverListener receiverListener) {
        synchronized (LOCK) {
            ReceiverInfo info = new ReceiverInfo();
            info.setId(receiverInfo.getId());
            info.setAction(receiverInfo.getAction());
            info.setScheme(receiverInfo.getScheme());
            info.setCategories(receiverInfo.getCategories());
            info.setType(TYPE_PERMISSION);
            info.setPermission(sendPermission);

            if (hasErrorWithParams(info, receiverListener)) {
                return;
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(receiverInfo.getAction());
            for (String category : receiverInfo.getCategories()) {
                filter.addCategory(category);
            }
            if (!TextUtils.isEmpty(receiverInfo.getScheme())) {
                filter.addDataScheme(receiverInfo.getScheme());
            }
            UtilsPermissionBroadcastReceiver.getInstance().addIntentFilter(sendPermission, filter, this);
        }
    }

    /**
     * 添加本地接收器并设置接收范围(注意 context 泄漏问题，不用时需要移除监听)，
     * 只能接收到当前应用内部发出的符合条件的消息
     *
     * @param receiverInfo     : 接收器信息
     * @param receiverListener : 接收器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:39 PM
     */
    @Override
    public void addLocalReceiverListener(@NonNull CloudReceiverInfo receiverInfo, @NonNull CloudReceiverListener receiverListener) {
        synchronized (LOCK) {
            ReceiverInfo info = new ReceiverInfo();
            info.setId(receiverInfo.getId());
            info.setAction(receiverInfo.getAction());
            info.setScheme(receiverInfo.getScheme());
            info.setCategories(receiverInfo.getCategories());
            info.setType(TYPE_LOCAL);

            if (hasErrorWithParams(info, receiverListener)) {
                return;
            }
            Context context = ContextManager.getApplication();
            if (context == null) {
                Logger.Error(CloudApiError.INIT_EMPTY.build());
                return;
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(receiverInfo.getAction());
            for (String category : receiverInfo.getCategories()) {
                filter.addCategory(category);
            }
            if (!TextUtils.isEmpty(receiverInfo.getScheme())) {
                filter.addDataScheme(receiverInfo.getScheme());
            }
            UtilsLocalBroadcastReceiver.getInstance(context).registerReceiver(this, filter);
        }
    }

    /**
     * 取消接收器
     *
     * @param receiverListener : 接收器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:39 PM
     */
    @Override
    public void removeReceiverListener(@NonNull CloudReceiverListener receiverListener) {
        synchronized (LOCK) {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < ID_RECEIVER_LISTENER_MAPPER.size(); i++) {
                if (ID_RECEIVER_LISTENER_MAPPER.valueAt(i) != receiverListener) {
                    continue;
                }
                int id = ID_RECEIVER_LISTENER_MAPPER.keyAt(i);

                ids.add(id);

                String action = ID_ACTION_MAPPER.get(id);
                ID_ACTION_MAPPER.remove(id);

                List<ReceiverInfo> infoList = ACTION_INFO_MAPPER.get(action);
                if (infoList == null) {
                    continue;
                }
                for (ReceiverInfo info : new ArrayList<>(infoList)) {
                    if (info.getId() != id) {
                        continue;
                    }
                    try {
                        infoList.remove(info);
                    } catch (Throwable ignored) {
                    }
                }
            }
            for (Integer integer : ids) {
                ID_RECEIVER_LISTENER_MAPPER.remove(integer);
            }
        }
    }

    /**
     * 发送全局广播，所有应用都能接收到，需要注意安全性问题
     *
     * @param intent : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:15 PM
     */
    @Override
    public void sendBroadcast(@NonNull Intent intent) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        context.sendBroadcast(intent);
    }

    /**
     * 发送全局广播并对接收方权限进行校验
     * 消息接收方需要注意在 manifest 中添加正确并符合规定的权限
     *
     * @param receiverPermission : 自定义接收方需要具有的权限
     * @param intent             : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:15 PM
     */
    @Override
    public void sendBroadcast(@NonNull String receiverPermission, @NonNull Intent intent) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        context.sendBroadcast(intent, receiverPermission);
    }

    /**
     * 发送本地广播，只有当前应用可以接收到
     *
     * @param intent : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:15 PM
     */
    @Override
    public void sendLocalBroadcast(@NonNull Intent intent) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        UtilsLocalBroadcastReceiver.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 接收到消息
     *
     * @param context : 上下文环境
     * @param intent  : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 18:04
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        sendMessage(context, intent, TYPE_ALL, "");
    }

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
    @Override
    public void onReceiveWithPermission(Context context, Intent intent, String permission) {
        sendMessage(context, intent, TYPE_PERMISSION, permission);
    }

    /**
     * 接收到消息
     *
     * @param context : 上下文环境
     * @param intent  : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 18:04
     */
    @Override
    public void onLocalReceive(Context context, Intent intent) {
        sendMessage(context, intent, TYPE_LOCAL, "");
    }

    private boolean hasErrorWithParams(ReceiverInfo receiverInfo, CloudReceiverListener receiverListener) {
        if (receiverInfo == null) {
            Logger.Error(CloudApiError.DATA_ERROR.setMsg("The CloudReceiverInfo is missing").build());
            return true;
        }
        if (receiverListener == null) {
            Logger.Error(CloudApiError.DATA_ERROR.setMsg("The CloudReceiverListener is missing").build());
            return true;
        }
        if ("".equals(receiverInfo.getAction())) {
            Logger.Error(CloudApiError.DATA_ERROR.setMsg("The action is missing").build());
            return true;
        }
        List<ReceiverInfo> receiverInfoList = ACTION_INFO_MAPPER.get(receiverInfo.getAction());
        if (receiverInfoList == null) {
            receiverInfoList = new ArrayList<>();
            ACTION_INFO_MAPPER.put(receiverInfo.getAction(), receiverInfoList);
        }
        for (ReceiverInfo info : receiverInfoList) {
            if (info.equals(receiverInfo)) {
                CloudReceiverListener listener = ID_RECEIVER_LISTENER_MAPPER.get(info.getId());
                if (listener == receiverListener) {
                    Logger.Error(CloudApiError.DATA_DUPLICATION.setAbout(" with : " + receiverInfo.toString()).build());
                    return true;
                }
            }
        }
        receiverInfoList.add(receiverInfo);
        ID_ACTION_MAPPER.put(receiverInfo.getId(), receiverInfo.getAction());
        ID_RECEIVER_LISTENER_MAPPER.put(receiverInfo.getId(), receiverListener);
        return false;
    }

    private void sendMessage(final Context context, final Intent intent, String type, String permission) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        List<ReceiverInfo> infoList = ACTION_INFO_MAPPER.get(action);
        if (infoList == null || infoList.size() == 0) {
            return;
        }

        final List<Integer> ids = new ArrayList<>();
        Set<String> categories = intent.getCategories();
        if (categories == null) {
            categories = new HashSet<>();
        }
        String scheme = intent.getScheme();
        if (scheme == null) {
            scheme = "";
        }

        for (ReceiverInfo info : infoList) {
            if (!info.getType().equals(type)) {
                continue;
            }
            if (!info.getPermission().equals(permission)) {
                continue;
            }

            List<String> list = info.getCategories();
            if (list.size() != categories.size() || !list.containsAll(categories)) {
                continue;
            }

            if (!info.getScheme().equals(scheme)) {
                continue;
            }

            ids.add(info.getId());
        }

        mTaskService.callUiThread(new Task<Object>() {
            @Override
            public Object call() {
                for (Integer integer : ids) {
                    CloudReceiverListener listener = ID_RECEIVER_LISTENER_MAPPER.get(integer);
                    if (listener == null) {
                        continue;
                    }
                    try {
                        listener.onReceive(context, intent);
                    } catch (Throwable throwable) {
                        Logger.Debug(throwable);
                    }
                }
                return null;
            }
        });
    }

}
