package com.proxy.service.ui.fieldcheck.node;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author: cangHX
 * on 2020/07/08  13:26
 */
public class NumberFieldCheckNode extends BaseFieldCheckNode {

    /**
     * 变量对象
     * */
    public Field field;

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


        boolean isInt = value instanceof Integer;
        boolean isLong = value instanceof Long;
        boolean isDouble = value instanceof Double;
        boolean isFloat = value instanceof Float;

        if (!isInt && !isLong && !isDouble && !isFloat) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a Number.").build());
            return true;
        }

        BigDecimal nowDecimal = new BigDecimal(String.valueOf(value));

        BigDecimal formalDecimal;

        if (maxNotSame != Double.MAX_VALUE) {
            formalDecimal = new BigDecimal(String.valueOf(max));
            if (nowDecimal.compareTo(formalDecimal) > 0) {
                callback.onError(markId, message);
                return true;
            }
        }

        formalDecimal = new BigDecimal(String.valueOf(maxNotSame));
        if (nowDecimal.compareTo(formalDecimal) >= 0) {
            callback.onError(markId, message);
            return true;
        }

        if (min != Double.MIN_VALUE) {
            formalDecimal = new BigDecimal(String.valueOf(min));
            if (nowDecimal.compareTo(formalDecimal) < 0) {
                callback.onError(markId, message);
                return true;
            }
        }

        formalDecimal = new BigDecimal(String.valueOf(minNotSame));
        if (nowDecimal.compareTo(formalDecimal) <= 0) {
            callback.onError(markId, message);
            return true;
        }

        if (scale >= 0) {
            formalDecimal = nowDecimal.setScale(scale, RoundingMode.HALF_UP);
            if (nowDecimal.compareTo(formalDecimal) != 0) {
                callback.onError(markId, message);
                return true;
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

        boolean isInt = content instanceof Integer;
        boolean isLong = content instanceof Long;
        boolean isDouble = content instanceof Double;
        boolean isFloat = content instanceof Float;

        if (!isInt && !isLong && !isDouble && !isFloat) {
            Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("The data type is error with markId : " + markId + ", this is not a Number.").build());
            return true;
        }

        BigDecimal nowDecimal = new BigDecimal(String.valueOf(content));

        BigDecimal formalDecimal;

        if (maxNotSame != Double.MAX_VALUE) {
            formalDecimal = new BigDecimal(String.valueOf(max));
            if (nowDecimal.compareTo(formalDecimal) > 0) {
                callback.onError(markId, message);
                return true;
            }
        }

        formalDecimal = new BigDecimal(String.valueOf(maxNotSame));
        if (nowDecimal.compareTo(formalDecimal) >= 0) {
            callback.onError(markId, message);
            return true;
        }

        if (min != Double.MIN_VALUE) {
            formalDecimal = new BigDecimal(String.valueOf(min));
            if (nowDecimal.compareTo(formalDecimal) < 0) {
                callback.onError(markId, message);
                return true;
            }
        }

        formalDecimal = new BigDecimal(String.valueOf(minNotSame));
        if (nowDecimal.compareTo(formalDecimal) <= 0) {
            callback.onError(markId, message);
            return true;
        }

        if (scale >= 0) {
            formalDecimal = nowDecimal.setScale(scale, RoundingMode.HALF_UP);
            if (nowDecimal.compareTo(formalDecimal) != 0) {
                callback.onError(markId, message);
                return true;
            }
        }

        return false;
    }
}
