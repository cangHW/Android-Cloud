package com.cloud.api.manager.listener;

import androidx.annotation.NonNull;

import com.cloud.base.base.BaseService;

/**
 * @author: cangHX
 * on 2020/03/04  14:51
 * <p>
 * 转换器，用于CloudSystem功能扩展
 * 泛型为符合当前转换器的数据类型
 */
public interface Converter<T extends BaseService> {

    /**
     * 数据转换
     *
     * @param t : 需要被转换的数据
     * @return 转换好的数据
     * @throws Throwable : 转换过程中的异常
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:01
     */
    @NonNull
    T converter(@NonNull T t) throws Throwable;

}
