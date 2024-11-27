package com.proxy.service.ui.fieldcheck.node;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.log.Logger;

import java.lang.reflect.Field;

/**
 * @author: cangHX
 * on 2020/07/08  15:12
 */
public class BooleanFieldCheckNode extends BaseFieldCheckNode {

    /**
     * 变量对象
     */
    public Field field;

    /**
     * 要求值
     */
    public boolean isValue;

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
                value = field.getBoolean(object);
            } catch (Throwable e) {
                Logger.Debug(e);
            }
        }

        if (value == null) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a Boolean.").build());
            return true;
        }

        boolean bool = (boolean) value;

        if (isValue != bool) {
            callback.onError(markId, message);
            return true;
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

        boolean isBoolean = content instanceof Boolean;

        if (!isBoolean) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a Boolean.").build());
            return true;
        }

        boolean bool = (boolean) content;

        if (isValue != bool) {
            callback.onError(markId, message);
            return true;
        }

        return false;
    }
}
