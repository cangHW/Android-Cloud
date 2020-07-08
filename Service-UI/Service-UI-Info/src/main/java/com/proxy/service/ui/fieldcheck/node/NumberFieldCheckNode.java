package com.proxy.service.ui.fieldcheck.node;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author: cangHX
 * on 2020/07/08  13:26
 */
public class NumberFieldCheckNode extends BaseFieldCheckNode {

    /**
     * 最大值，<=
     */
    public double max;

    /**
     * 最大值，<
     */
    public double maxNotSame;

    /**
     * 最小值，>=
     */
    public double min;

    /**
     * 最小值，>
     */
    public double minNotSame;

    /**
     * 小数长度
     */
    public int scale;

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
        boolean isInt = object instanceof Integer;
        boolean isLong = object instanceof Long;
        boolean isDouble = object instanceof Double;
        boolean isFloat = object instanceof Float;

        if (!isInt && !isLong && !isDouble && !isFloat) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a Number.").build());
            return true;
        }

        BigDecimal nowDecimal = new BigDecimal(String.valueOf(object));

        BigDecimal formalDecimal;

        if (maxNotSame != Double.MAX_VALUE) {
            formalDecimal = new BigDecimal(String.valueOf(max));
            if (nowDecimal.compareTo(formalDecimal) > 0) {
                callback.onError(message);
                return true;
            }
        }

        formalDecimal = new BigDecimal(String.valueOf(maxNotSame));
        if (nowDecimal.compareTo(formalDecimal) >= 0) {
            callback.onError(message);
            return true;
        }

        if (min != Double.MIN_VALUE) {
            formalDecimal = new BigDecimal(String.valueOf(min));
            if (nowDecimal.compareTo(formalDecimal) < 0) {
                callback.onError(message);
                return true;
            }
        }

        formalDecimal = new BigDecimal(String.valueOf(minNotSame));
        if (nowDecimal.compareTo(formalDecimal) <= 0) {
            callback.onError(message);
            return true;
        }

        if (scale >= 0) {
            formalDecimal = nowDecimal.setScale(scale, RoundingMode.HALF_UP);
            if (nowDecimal.compareTo(formalDecimal) != 0) {
                callback.onError(message);
                return true;
            }
        }

        return false;
    }
}
