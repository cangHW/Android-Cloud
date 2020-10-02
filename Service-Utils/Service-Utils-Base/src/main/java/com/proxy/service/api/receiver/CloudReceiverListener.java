package com.proxy.service.api.receiver;

import android.content.Context;
import android.content.Intent;

/**
 * @author : cangHX
 * on 2020/09/29  11:38 PM
 */
public interface CloudReceiverListener {
    /**
     * 接收到消息
     *
     * @param context: 上下文环境
     * @param intent   : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/09/29  11:38 PM
     */
    void onReceive(Context context, Intent intent);
}
