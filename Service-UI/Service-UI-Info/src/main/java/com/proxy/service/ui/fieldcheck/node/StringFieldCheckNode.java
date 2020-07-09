package com.proxy.service.ui.fieldcheck.node;

import android.text.TextUtils;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/08  15:12
 */
public class StringFieldCheckNode extends BaseFieldCheckNode {

    /**
     * 最大长度，<=
     */
    public int maxLength;

    /**
     * 最大长度，<
     */
    public int maxLengthNotSame;

    /**
     * 最小值，>=
     */
    public int minLength;

    /**
     * 最小值，>
     */
    public int minLengthNotSame;

    /**
     * 不能为 null 或 空
     */
    public boolean notEmpty;

    /**
     * 不能为空格
     */
    public boolean notBlank;

    /**
     * 检测是否符合要求
     *
     * @param object   : 待检测数据
     * @param callback : 变量信息检查回调接口
     * @return true 有错误，false 没有错误
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 15:26
     */
    @Override
    public boolean isHasError(Object object, CloudUiFieldCheckErrorCallback callback) {
        boolean isString = object instanceof String;

        if (object != null && !isString) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a String.").build());
            return true;
        }

        String string = (String) object;

        if (notEmpty) {
            if (TextUtils.isEmpty(string)) {
                callback.onError(message);
                return true;
            }
        }

        if (notBlank) {
            if (TextUtils.isEmpty(string)) {
                callback.onError(message);
                return true;
            }

            string = string.trim();

            if (TextUtils.isEmpty(string)) {
                callback.onError(message);
                return true;
            }
        }

        if (maxLength > -1) {
            if (string == null || string.length() > maxLength) {
                callback.onError(message);
                return true;
            }
        }

        if (maxLengthNotSame > -1) {
            if (string == null || string.length() >= maxLengthNotSame) {
                callback.onError(message);
                return true;
            }
        }

        if (minLength > -1) {
            if (string == null || string.length() < minLength) {
                callback.onError(message);
                return true;
            }
        }

        if (minLengthNotSame > -1) {
            if (string == null || string.length() <= minLengthNotSame) {
                callback.onError(message);
                return true;
            }
        }

        return false;
    }
}
