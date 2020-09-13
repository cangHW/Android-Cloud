package com.proxy.service.api.security;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author : cangHX
 * on 2020/09/11  11:51 PM
 */
public interface SecurityAesHelper {

    enum TransFormation {
        /***/
        CBC("AES/CBC/PKCS5Padding"),
        ECB("AES/ECB/PKCS5Padding"),
        CTR("AES/CTR/PKCS5Padding"),
        OFB("AES/OFB/PKCS5Padding"),
        CFB("AES/CFB/PKCS5Padding");

        String value;

        TransFormation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 设置密钥
     *
     * @param key : 密钥
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:52 PM
     */
    SecurityAesHelper setSecretKeySpec(@NonNull String key);

    /**
     * 设置偏移量
     *
     * @param spec : 偏移值
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:53 PM
     */
    SecurityAesHelper setIvParameterSpec(@NonNull String spec);

    /**
     * 设置加密方案
     *
     * @param formation ; 加密方案
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    SecurityAesHelper setTransFormation(@NonNull TransFormation formation);

    /**
     * 加密
     *
     * @param data : 待加密对象
     * @return 加密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    String encryptString(@NonNull String data);

    /**
     * 加密
     *
     * @param bytes : 待加密对象
     * @return 加密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    String encryptString(@NonNull byte[] bytes);

    /**
     * 加密
     *
     * @param data : 待加密对象
     * @return 加密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    byte[] encryptByte(@NonNull String data);

    /**
     * 加密
     *
     * @param bytes : 待加密对象
     * @return 加密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    byte[] encryptByte(@NonNull byte[] bytes);

    /**
     * 解密
     *
     * @param data : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    String decryptString(@NonNull String data);

    /**
     * 解密
     *
     * @param bytes : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    String decryptString(@NonNull byte[] bytes);

    /**
     * 解密
     *
     * @param data : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    byte[] decryptByte(@NonNull String data);

    /**
     * 解密
     *
     * @param bytes : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Nullable
    byte[] decryptByte(@NonNull byte[] bytes);
}
