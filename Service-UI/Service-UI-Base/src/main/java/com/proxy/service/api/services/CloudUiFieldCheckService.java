package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.impl.CloudUiCheckBooleanInfo;
import com.proxy.service.api.impl.CloudUiCheckNumberInfo;
import com.proxy.service.api.impl.CloudUiCheckStringInfo;
import com.proxy.service.api.interfaces.IUiFieldCheck;

/**
 * 提供变量信息检查以及提示的服务
 *
 * @author: cangHX
 * on 2020/07/07  16:57
 */
public interface CloudUiFieldCheckService extends IUiFieldCheck {

    /**
     * 初始化
     *
     * @param object : 申请检测的类对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 09:27
     */
    void init(@NonNull Object object);

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

    /**
     * 设置 boolean 检测条件
     *
     * @param id               : 同一标记下面的唯一ID，id 重复，则后一个替换前一个
     * @param checkBooleanInfo : 检测条件
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    void setConditions(int id, CloudUiCheckBooleanInfo checkBooleanInfo);

    /**
     * 设置 number 检测条件
     *
     * @param id              : 同一标记下面的唯一ID，id 重复，则后一个替换前一个
     * @param checkNumberInfo : 检测条件
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    void setConditions(int id, CloudUiCheckNumberInfo checkNumberInfo);

    /**
     * 设置 string 检测条件
     *
     * @param id              : 同一标记下面的唯一ID，id 重复，则后一个替换前一个
     * @param checkStringInfo : 检测条件
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    void setConditions(int id, CloudUiCheckStringInfo checkStringInfo);
}
