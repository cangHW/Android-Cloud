package com.proxy.service.ui.fieldcheck.node;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/08  15:12
 */
public class BooleanFieldCheckNode extends BaseFieldCheckNode {

    /**
     * 要求值
     */
    public boolean isValue;

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
        boolean isBoolean = object instanceof Boolean;

        if (!isBoolean) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a Boolean.").build());
            return true;
        }

        boolean value = (boolean) object;

        if (isValue != value) {
            callback.onError(message);
            return true;
        }

        return false;
    }
}
