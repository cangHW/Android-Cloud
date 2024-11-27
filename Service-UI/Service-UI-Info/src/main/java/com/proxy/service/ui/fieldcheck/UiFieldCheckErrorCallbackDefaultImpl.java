package com.proxy.service.ui.fieldcheck;

import android.content.Context;
import android.widget.Toast;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.log.Logger;

/**
 * @author: cangHX
 * on 2020/07/08  09:47
 */
public class UiFieldCheckErrorCallbackDefaultImpl implements CloudUiFieldCheckErrorCallback {

    /**
     * 发现错误回调
     *
     * @param markId : 标记id
     * @param msg    : 错误信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 15:41
     */
    @Override
    public void onError(String markId, String msg) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
