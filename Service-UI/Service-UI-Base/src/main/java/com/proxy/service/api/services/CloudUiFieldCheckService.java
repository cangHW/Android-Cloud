package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.interfaces.IUiFieldCheck;

/**
 * @author: cangHX
 * on 2020/07/07  16:57
 * <p>
 * 提供变量信息检查以及提示的服务
 */
public interface CloudUiFieldCheckService extends IUiFieldCheck {

    /**
     * 初始化
     *
     * @param aClass : 申请检测的 class
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 09:27
     */
    void init(@NonNull Class<?> aClass);

    /**
     * 设置全局检查失败回调，强引用，需要注意 context 泄漏
     * 最好在 application 等长生命周期类中设置
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    void setGlobalErrorToastCallback(@NonNull CloudUiFieldCheckErrorCallback callback);

    /**
     * 设置临时检查失败回调
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    void setErrorToastCallback(@NonNull CloudUiFieldCheckErrorCallback callback);

}
