package com.proxy.service.ui.info;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.interfaces.IReallyUiFieldCheck;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.tag.CloudServiceTagUi;
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
     * 当前申请检测的 class
     */
    private Class<?> aClass;

    /**
     * 初始化
     *
     * @param aClass : 申请检测的 class
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 09:27
     */
    @Override
    public void init(@NonNull Class<?> aClass) {
        this.aClass = aClass;
        FieldCheckDataManager.init(aClass);
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
     * 添加一个待检测的 String 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param s      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, String s) {
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(aClass, mCallback);
        return reallyUiFieldCheck.of(markId, s);
    }

    /**
     * 添加一个待检测的 double 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param d      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, double d) {
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(aClass, mCallback);
        return reallyUiFieldCheck.of(markId, d);
    }

    /**
     * 添加一个待检测的 float 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param f      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, float f) {
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(aClass, mCallback);
        return reallyUiFieldCheck.of(markId, f);
    }

    /**
     * 添加一个待检测的 int 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param i      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, int i) {
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(aClass, mCallback);
        return reallyUiFieldCheck.of(markId, i);
    }

    /**
     * 添加一个待检测的 long 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param l      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, long l) {
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(aClass, mCallback);
        return reallyUiFieldCheck.of(markId, l);
    }

    /**
     * 添加一个待检测的 boolean 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param b      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, boolean b) {
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(aClass, mCallback);
        return reallyUiFieldCheck.of(markId, b);
    }
}
