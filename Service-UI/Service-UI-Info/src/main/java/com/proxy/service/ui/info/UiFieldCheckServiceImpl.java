package com.proxy.service.ui.info;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.interfaces.IReallyUiFieldCheck;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.tag.CloudServiceTagUi;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.fieldcheck.FieldCheckDataManager;
import com.proxy.service.ui.fieldcheck.RealUiFieldCheckImpl;
import com.proxy.service.ui.fieldcheck.UiFieldCheckErrorCallbackDefaultImpl;

/**
 * @author: cangHX
 * on 2020/07/07  19:12
 */
@CloudApiNewInstance()
@CloudApiService(serviceTag = CloudServiceTagUi.UI_FIELD_CHECK)
public class UiFieldCheckServiceImpl implements CloudUiFieldCheckService {

    /**
     * 全局回调
     */
    private volatile static CloudUiFieldCheckErrorCallback mGlobalCallback = new UiFieldCheckErrorCallbackDefaultImpl();

    /**
     * 当前回调
     */
    private CloudUiFieldCheckErrorCallback mCallback = mGlobalCallback;

    /**
     * 当前申请检测的类对象
     */
    private Object mObject;

    /**
     * 初始化
     *
     * @param object : 申请检测的类对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 09:27
     */
    @Override
    public void init(@NonNull Object object) {
        this.mObject = object;
        if (object instanceof Class<?>) {
            Logger.Error("Class cannot be used in CloudUiFieldCheckService. with : " + ((Class<?>) object).getCanonicalName());
        } else {
            FieldCheckDataManager.init(object.getClass());
        }
    }

    /**
     * 设置全局检查失败回调
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    @Override
    public void setGlobalErrorToastCallback(@NonNull CloudUiFieldCheckErrorCallback callback) {
        mGlobalCallback = callback;
    }

    /**
     * 设置临时检查失败回调
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    @Override
    public void setErrorToastCallback(@NonNull CloudUiFieldCheckErrorCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 发起检测
     *
     * @param markId : 标记id，标记当前检测条件
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck check(String markId) {
        IReallyUiFieldCheck reallyUiFieldCheck = new RealUiFieldCheckImpl(mObject, mCallback);
        return reallyUiFieldCheck.check(markId);
    }
}
