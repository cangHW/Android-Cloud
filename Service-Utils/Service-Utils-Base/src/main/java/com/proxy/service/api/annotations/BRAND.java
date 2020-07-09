package com.proxy.service.api.annotations;

import androidx.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 设备厂商
 *
 * @author: cangHX
 * on 2020/06/20  12:55
 */
@StringDef({
        BRAND.HUAWEI,
        BRAND.HONOR,
        BRAND.XIAOMI,
        BRAND.REDMI,
        BRAND.VIVO,
        BRAND.OPPO,
        BRAND.MEIZU,
        BRAND.SAMSUNG,
        BRAND.QIHU
})
@Documented
@Retention(value = RetentionPolicy.SOURCE)
public @interface BRAND {
    /**
     * 华为
     */
    String HUAWEI = "huawei";
    /**
     * 荣耀
     */
    String HONOR = "honor";
    /**
     * 小米
     */
    String XIAOMI = "xiaomi";
    /**
     * 红米
     */
    String REDMI = "redmi";
    /**
     * VIVO
     */
    String VIVO = "vivo";
    /**
     * OPPO
     */
    String OPPO = "oppo";
    /**
     * 魅族
     */
    String MEIZU = "meizu";
    /**
     * 三星
     */
    String SAMSUNG = "samsung";
    /**
     * 360
     */
    String QIHU = "360";
}
