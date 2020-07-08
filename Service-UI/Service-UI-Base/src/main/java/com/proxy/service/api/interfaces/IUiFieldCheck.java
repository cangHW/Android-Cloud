package com.proxy.service.api.interfaces;

import androidx.annotation.NonNull;

import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/07/08  09:24
 */
public interface IUiFieldCheck extends BaseService {

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
    IReallyUiFieldCheck of(String markId, String s);

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
    IReallyUiFieldCheck of(String markId, double d);

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
    IReallyUiFieldCheck of(String markId, float f);

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
    IReallyUiFieldCheck of(String markId, int i);

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
    IReallyUiFieldCheck of(String markId, long l);

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
    IReallyUiFieldCheck of(String markId, boolean b);

}
