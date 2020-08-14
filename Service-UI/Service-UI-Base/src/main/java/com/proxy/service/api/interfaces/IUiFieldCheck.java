package com.proxy.service.api.interfaces;

import androidx.annotation.NonNull;

import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/07/08  09:24
 */
public interface IUiFieldCheck extends BaseService {

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
    IReallyUiFieldCheck check(String markId);
}
