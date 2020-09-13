package com.proxy.service.utils.info;

import android.os.Build;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.security.SecurityAesHelper;
import com.proxy.service.api.services.CloudUtilsSecurityService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.util.AesHelperUtils;
import com.proxy.service.utils.util.StringUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * @author : cangHX
 * on 2020/09/11  9:15 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_SECURITY)
public class UtilsSecurityServiceImpl implements CloudUtilsSecurityService {
    /**
     * md5 加密
     *
     * @param stream : 准备加密的流
     * @return 加密后的字符串
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:15 PM
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public String md5Encode(InputStream stream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            DigestInputStream digestInputStream = new DigestInputStream(stream, digest);
            byte[] buffer = new byte[8 * 1024];
            while (digestInputStream.read(buffer) > 0) {
            }
            MessageDigest messageDigest = digestInputStream.getMessageDigest();
            return StringUtils.parseByte2HexStr(messageDigest.digest()).toLowerCase();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return null;
    }

    /**
     * md5 加密
     *
     * @param string : 准备加密的字符串
     * @return 加密后的字符串
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:15 PM
     */
    @Override
    public String md5Encode(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                digest.update(string.getBytes(StandardCharsets.UTF_8));
            } else {
                digest.update(string.getBytes(Charset.forName("utf-8")));
            }
            return StringUtils.parseByte2HexStr(digest.digest()).toLowerCase();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return null;
    }

    /**
     * Aes 加密
     *
     * @return aes 加密辅助类
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 12:18 PM
     */
    @Override
    public SecurityAesHelper aes() {
        return AesHelperUtils.create();
    }

}
