package com.proxy.service.utils.util;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.security.SecurityAesHelper;
import com.proxy.service.api.utils.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author : cangHX
 * on 2020/09/11  11:50 PM
 */
public class AesHelperUtils implements SecurityAesHelper {

    private SecretKeySpec mKey;
    private IvParameterSpec mSpec;
    private String mTransformation = TransFormation.ECB.getValue();

    private AesHelperUtils() {
    }

    public static AesHelperUtils create() {
        return new AesHelperUtils();
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
    @Override
    public SecurityAesHelper setSecretKeySpec(@NonNull String key) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        } else {
            mKey = new SecretKeySpec(key.getBytes(Charset.forName("utf-8")), "AES");
        }
        return this;
    }

    /**
     * 设置偏移量
     *
     * @param spec : 偏移值
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:53 PM
     */
    @Override
    public SecurityAesHelper setIvParameterSpec(@NonNull String spec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSpec = new IvParameterSpec(spec.getBytes(StandardCharsets.UTF_8));
        } else {
            mSpec = new IvParameterSpec(spec.getBytes(Charset.forName("utf-8")));
        }
        return this;
    }

    /**
     * 设置加密方案
     *
     * @param formation ; 加密方案
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Override
    public SecurityAesHelper setTransFormation(@NonNull TransFormation formation) {
        mTransformation = formation.getValue();
        return this;
    }

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
    @Override
    public String encryptString(@NonNull String data) {
        return StringUtils.parseByte2HexStr(encryptByte(data));
    }

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
    @Override
    public String encryptString(@NonNull byte[] bytes) {
        return StringUtils.parseByte2HexStr(encryptByte(bytes));
    }

    /**
     * 加密
     *
     * @param data : 待加密对象
     * @return 加密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Override
    public byte[] encryptByte(@NonNull String data) {
        byte[] bytes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bytes = data.getBytes(StandardCharsets.UTF_8);

        } else {
            bytes = data.getBytes(Charset.forName("utf-8"));
        }
        return encryptByte(bytes);
    }

    /**
     * 加密
     *
     * @param bytes : 待加密对象
     * @return 加密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Override
    public byte[] encryptByte(@NonNull byte[] bytes) {
        return encrypt(bytes);
    }

    /**
     * 解密
     *
     * @param data : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Override
    public String decryptString(@NonNull String data) {
        byte[] bytes1 = decryptByte(data);
        if (bytes1 == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new String(bytes1, StandardCharsets.UTF_8);
        } else {
            return new String(bytes1, Charset.forName("utf-8"));
        }
    }

    /**
     * 解密
     *
     * @param bytes : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Override
    public String decryptString(@NonNull byte[] bytes) {
        byte[] bytes1 = decryptByte(bytes);
        if (bytes1 == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new String(bytes1, StandardCharsets.UTF_8);
        } else {
            return new String(bytes1, Charset.forName("utf-8"));
        }
    }

    /**
     * 解密
     *
     * @param data : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Override
    public byte[] decryptByte(@NonNull String data) {
        byte[] bytes = StringUtils.parseHexStr2Byte(data);
        if (bytes == null) {
            return null;
        }
        return decryptByte(bytes);
    }

    /**
     * 解密
     *
     * @param bytes : 待解密对象
     * @return 解密后的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:55 PM
     */
    @Override
    public byte[] decryptByte(@NonNull byte[] bytes) {
        return decrypt(bytes);
    }

    private byte[] encrypt(byte[] bytes) {
        if (mKey == null) {
            Logger.Debug("Lack of key");
            return null;
        }
        if (!mTransformation.equals(TransFormation.ECB.getValue()) && mSpec == null) {
            Logger.Debug("The AesUtils need a IvParameterSpec");
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(mTransformation);
            if (mTransformation.equals(TransFormation.ECB.getValue())) {
                cipher.init(Cipher.ENCRYPT_MODE, mKey);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, mKey, mSpec);
            }
            return cipher.doFinal(bytes);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return null;
    }

    private byte[] decrypt(byte[] bytes) {
        if (mKey == null) {
            Logger.Debug("Lack of key");
            return null;
        }
        if (!mTransformation.equals(TransFormation.ECB.getValue()) && mSpec == null) {
            Logger.Debug("The AesUtils need a IvParameterSpec");
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(mTransformation);
            if (mTransformation.equals(TransFormation.ECB.getValue())) {
                cipher.init(Cipher.DECRYPT_MODE, mKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, mKey, mSpec);
            }
            return cipher.doFinal(bytes);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return null;
    }
}
