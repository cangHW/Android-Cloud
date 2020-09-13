package com.proxy.service.api.services;

import com.proxy.service.api.security.SecurityAesHelper;
import com.proxy.service.base.BaseService;

import java.io.InputStream;

/**
 * @author : cangHX
 * on 2020/09/10  11:11 PM
 */
public interface CloudUtilsSecurityService extends BaseService {

    /**
     * md5 加密
     *
     * @param stream : 准备加密的流
     * @return 加密后的字符串
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:15 PM
     */
    String md5Encode(InputStream stream);

    /**
     * md5 加密
     *
     * @param string : 准备加密的字符串
     * @return 加密后的字符串
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:15 PM
     */
    String md5Encode(String string);

    /**
     * Aes 加密
     *
     * @return aes 加密辅助类
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 12:18 PM
     */
    SecurityAesHelper aes();
}
