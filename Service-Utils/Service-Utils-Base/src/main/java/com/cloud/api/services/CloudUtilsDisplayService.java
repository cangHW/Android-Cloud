package com.cloud.api.services;

import com.cloud.base.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/11  10:23
 * <p>
 * 像素转换相关
 */
public interface CloudUtilsDisplayService extends BaseService {

    /**
     * px转dp
     *
     * @param pxValue : 需要转化的px数值
     * @return 转化后的dp数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:24
     */
    int px2dp(float pxValue);

    /**
     * dp转px
     *
     * @param dpValue : 需要转化的dp数值
     * @return 转化后的px数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:26
     */
    int dp2px(float dpValue);

    /**
     * px转sp
     *
     * @param pxValue : 需要转化的px数值
     * @return 转化后的sp数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:27
     */
    int px2sp(float pxValue);

    /**
     * sp转px
     *
     * @param spValue : 需要转化的sp数值
     * @return 转化后的px数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:28
     */
    int sp2px(float spValue);
}
