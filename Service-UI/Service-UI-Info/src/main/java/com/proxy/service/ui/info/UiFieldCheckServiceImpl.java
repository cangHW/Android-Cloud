package com.proxy.service.ui.info;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.impl.CloudUiCheckBooleanInfo;
import com.proxy.service.api.impl.CloudUiCheckNumberInfo;
import com.proxy.service.api.impl.CloudUiCheckStringInfo;
import com.proxy.service.api.interfaces.IReallyUiFieldCheck;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.tag.CloudServiceTagUi;
import com.proxy.service.api.log.Logger;
import com.proxy.service.ui.fieldcheck.FieldCheckDataManager;
import com.proxy.service.ui.fieldcheck.RealUiFieldCheckImpl;
import com.proxy.service.ui.fieldcheck.UiFieldCheckErrorCallbackDefaultImpl;
import com.proxy.service.ui.fieldcheck.node.BaseFieldCheckNode;
import com.proxy.service.ui.fieldcheck.node.BooleanFieldCheckNode;
import com.proxy.service.ui.fieldcheck.node.NumberFieldCheckNode;
import com.proxy.service.ui.fieldcheck.node.StringFieldCheckNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * 额外设置的检测条件
     */
    private SparseArray<BaseFieldCheckNode> mCheckNodes = new SparseArray<>();

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
     * 设置 boolean 检测条件
     *
     * @param id               : 同一标记下面的唯一ID，id 重复，则后一个替换前一个
     * @param checkBooleanInfo : 检测条件
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    @Override
    public void setConditions(int id, CloudUiCheckBooleanInfo checkBooleanInfo) {
        BooleanFieldCheckNode fieldCheckNode = new BooleanFieldCheckNode();
        fieldCheckNode.markId = checkBooleanInfo.getMarkId();
        fieldCheckNode.isValue = checkBooleanInfo.isValue();
        if (!TextUtils.isEmpty(checkBooleanInfo.getMessage())) {
            fieldCheckNode.message = checkBooleanInfo.getMessage();
            mCheckNodes.put(id, fieldCheckNode);
            return;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            fieldCheckNode.message = "";
        } else {
            try {
                fieldCheckNode.message = context.getString(checkBooleanInfo.getStringId());
            } catch (Throwable ignored) {
            }
        }

        mCheckNodes.put(id, fieldCheckNode);
    }

    /**
     * 设置 number 检测条件
     *
     * @param id              : 同一标记下面的唯一ID，id 重复，则后一个替换前一个
     * @param checkNumberInfo : 检测条件
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    @Override
    public void setConditions(int id, CloudUiCheckNumberInfo checkNumberInfo) {
        NumberFieldCheckNode fieldCheckNode = new NumberFieldCheckNode();
        fieldCheckNode.markId = checkNumberInfo.getMarkId();
        fieldCheckNode.max = checkNumberInfo.getMax();
        fieldCheckNode.maxNotSame = checkNumberInfo.getMaxNotSame();
        fieldCheckNode.min = checkNumberInfo.getMin();
        fieldCheckNode.minNotSame = checkNumberInfo.getMinNotSame();
        fieldCheckNode.scale = checkNumberInfo.getScale();
        if (!TextUtils.isEmpty(checkNumberInfo.getMessage())) {
            fieldCheckNode.message = checkNumberInfo.getMessage();
            mCheckNodes.put(id, fieldCheckNode);
            return;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            fieldCheckNode.message = "";
        } else {
            try {
                fieldCheckNode.message = context.getString(checkNumberInfo.getStringId());
            } catch (Throwable ignored) {
            }
        }

        mCheckNodes.put(id, fieldCheckNode);
    }

    /**
     * 设置 string 检测条件
     *
     * @param id              : 同一标记下面的唯一ID，id 重复，则后一个替换前一个
     * @param checkStringInfo : 检测条件
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    @Override
    public void setConditions(int id, CloudUiCheckStringInfo checkStringInfo) {
        StringFieldCheckNode fieldCheckNode = new StringFieldCheckNode();
        fieldCheckNode.markId = checkStringInfo.getMarkId();
        fieldCheckNode.maxLength = checkStringInfo.getMaxLength();
        fieldCheckNode.maxLengthNotSame = checkStringInfo.getMaxLengthNotSame();
        fieldCheckNode.minLength = checkStringInfo.getMinLength();
        fieldCheckNode.minLengthNotSame = checkStringInfo.getMinLengthNotSame();
        fieldCheckNode.notEmpty = checkStringInfo.isNotEmpty();
        fieldCheckNode.notBlank = checkStringInfo.isNotBlank();
        fieldCheckNode.notWithRegex = checkStringInfo.getNotWithRegex();
        fieldCheckNode.shouldWithRegex = checkStringInfo.getShouldWithRegex();
        if (!TextUtils.isEmpty(checkStringInfo.getMessage())) {
            fieldCheckNode.message = checkStringInfo.getMessage();
            mCheckNodes.put(id, fieldCheckNode);
            return;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            fieldCheckNode.message = "";
        } else {
            try {
                fieldCheckNode.message = context.getString(checkStringInfo.getStringId());
            } catch (Throwable ignored) {
            }
        }

        mCheckNodes.put(id, fieldCheckNode);
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
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(mObject, mCallback);
        reallyUiFieldCheck.setCheckNodes(mCheckNodes);
        return reallyUiFieldCheck.check(markId);
    }

    /**
     * 发起检测
     *
     * @param markId  : 标记id，标记当前检测条件
     * @param content : 待检测的内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck check(String markId, Object content) {
        RealUiFieldCheckImpl reallyUiFieldCheck = new RealUiFieldCheckImpl(mObject, mCallback);
        reallyUiFieldCheck.setCheckNodes(mCheckNodes);
        return reallyUiFieldCheck.check(markId, content);
    }
}
