package com.proxy.service.ui.fieldcheck.node;

import android.text.TextUtils;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * @author: cangHX
 * on 2020/07/08  15:12
 */
public class StringFieldCheckNode extends BaseFieldCheckNode {

    /**
     * 变量对象
     */
    public Field field;

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
     * 不允许含有的格式，正则表达式
     */
    public String notWithRegex;

    /**
     * 必须含有的格式，正则表达式
     */
    public String shouldWithRegex;

    /**
     * 检测是否符合要求
     *
     * @param object   : 数据包装对象
     * @param callback : 变量信息检查回调接口
     * @return true 有错误，false 没有错误
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 15:26
     */
    @Override
    public boolean isHasError(Object object, CloudUiFieldCheckErrorCallback callback) {
        Object value = null;

        if (field != null) {
            field.setAccessible(true);
            try {
                value = field.get(object);
            } catch (Throwable e) {
                Logger.Debug(e);
            }
        }


        boolean isString = value instanceof String;

        if (value != null && !isString) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a String.").build());
            return true;
        }

        String string = (String) value;

        if (notEmpty) {
            if (TextUtils.isEmpty(string)) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (notBlank) {
            if (TextUtils.isEmpty(string)) {
                callback.onError(markId, message);
                return true;
            }

            string = string.trim();

            if (TextUtils.isEmpty(string)) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (maxLength > -1) {
            if (string == null || string.length() > maxLength) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (maxLengthNotSame > -1) {
            if (string == null || string.length() >= maxLengthNotSame) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (minLength > -1) {
            if (string == null || string.length() < minLength) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (minLengthNotSame > -1) {
            if (string == null || string.length() <= minLengthNotSame) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (!TextUtils.isEmpty(notWithRegex)) {
            try {
                Pattern pattern = Pattern.compile(notWithRegex);
                if (string == null || pattern.matcher(string).find()) {
                    callback.onError(markId, message);
                    return true;
                }
            } catch (Throwable throwable) {
                Logger.Error(CloudApiError.DATA_ERROR.setMsg("Regular expression format error. notWithRegex : " + notWithRegex).build(), throwable);
            }
        }

        if (!TextUtils.isEmpty(shouldWithRegex)) {
            try {
                Pattern pattern = Pattern.compile(shouldWithRegex);
                if (string == null || !pattern.matcher(string).find()) {
                    callback.onError(markId, message);
                    return true;
                }
            } catch (Throwable throwable) {
                Logger.Error(CloudApiError.DATA_ERROR.setMsg("Regular expression format error. shouldWithRegex : " + shouldWithRegex).build(), throwable);
            }
        }

        return false;
    }

    /**
     * 检测是否符合要求
     *
     * @param content  : 数据内容
     * @param callback : 变量信息检查回调接口
     * @return true 有错误，false 没有错误
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 15:26
     */
    @Override
    public boolean isHasErrorWithContent(Object content, CloudUiFieldCheckErrorCallback callback) {
        boolean isString = content instanceof String;

        if (!isString) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a String.").build());
            return true;
        }

        String string = (String) content;

        if (notEmpty) {
            if (TextUtils.isEmpty(string)) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (notBlank) {
            if (TextUtils.isEmpty(string)) {
                callback.onError(markId, message);
                return true;
            }

            string = string.trim();

            if (TextUtils.isEmpty(string)) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (maxLength > -1) {
            if (string.length() > maxLength) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (maxLengthNotSame > -1) {
            if (string.length() >= maxLengthNotSame) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (minLength > -1) {
            if (string.length() < minLength) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (minLengthNotSame > -1) {
            if (string.length() <= minLengthNotSame) {
                callback.onError(markId, message);
                return true;
            }
        }

        if (!TextUtils.isEmpty(notWithRegex)) {
            try {
                Pattern pattern = Pattern.compile(notWithRegex);
                if (pattern.matcher(string).find()) {
                    callback.onError(markId, message);
                    return true;
                }
            } catch (Throwable throwable) {
                Logger.Error(CloudApiError.DATA_ERROR.setMsg("Regular expression format error. notWithRegex : " + notWithRegex).build(), throwable);
            }
        }

        if (!TextUtils.isEmpty(shouldWithRegex)) {
            try {
                Pattern pattern = Pattern.compile(shouldWithRegex);
                if (!pattern.matcher(string).find()) {
                    callback.onError(markId, message);
                    return true;
                }
            } catch (Throwable throwable) {
                Logger.Error(CloudApiError.DATA_ERROR.setMsg("Regular expression format error. shouldWithRegex : " + shouldWithRegex).build(), throwable);
            }
        }

        return false;
    }
}
