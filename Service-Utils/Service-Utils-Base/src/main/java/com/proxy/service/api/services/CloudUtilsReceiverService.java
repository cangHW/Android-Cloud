package com.proxy.service.api.services;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.proxy.service.api.receiver.CloudReceiverInfo;
import com.proxy.service.api.receiver.CloudReceiverListener;
import com.proxy.service.base.BaseService;

/**
 * @author : cangHX
 * on 2020/09/28  10:52 PM
 */
public interface CloudUtilsReceiverService extends BaseService {

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
    void addReceiverListener(@NonNull CloudReceiverInfo receiverInfo, @NonNull CloudReceiverListener receiverListener);

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
    void addReceiverListener(@NonNull String sendPermission, @NonNull CloudReceiverInfo receiverInfo, @NonNull CloudReceiverListener receiverListener);

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
    void addLocalReceiverListener(@NonNull CloudReceiverInfo receiverInfo, @NonNull CloudReceiverListener receiverListener);

    /**
     * 取消接收器
     *
     * @param receiverListener : 接收器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:39 PM
     */
    void removeReceiverListener(@NonNull CloudReceiverListener receiverListener);

    /**
     * 发送全局广播，所有应用都能接收到，需要注意安全性问题
     *
     * @param intent : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:15 PM
     */
    void sendBroadcast(@NonNull Intent intent);

    /**
     * 发送全局广播并对接收方权限进行校验，
     * 消息接收方需要注意在 manifest 中添加正确并符合规定的权限
     *
     * @param receiverPermission : 自定义接收方需要具有的权限
     * @param intent             : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:15 PM
     */
    void sendBroadcast(@NonNull String receiverPermission, @NonNull Intent intent);

    /**
     * 发送本地广播，只有当前应用可以接收到
     *
     * @param intent : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 11:15 PM
     */
    void sendLocalBroadcast(@NonNull Intent intent);
}
