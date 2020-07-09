package com.proxy.service.library.util;

import com.proxy.service.api.annotations.BRAND;
import com.proxy.service.api.services.CloudUtilsSystemInfoService;
import com.proxy.service.library.info.UtilsSystemInfoServiceImpl;

/**
 * @author: cangHX
 * on 2020/06/19  19:02
 */
public class BrandUtils {

    /**
     * 当前设备是否是华为
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 19:05
     */
    public static boolean isHw() {
        CloudUtilsSystemInfoService service = new UtilsSystemInfoServiceImpl();
        String brand = service.getBrand();
        return BRAND.HUAWEI.equalsIgnoreCase(brand) || BRAND.HONOR.equalsIgnoreCase(brand);
    }

    /**
     * 当前设备是否是小米
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 19:05
     */
    public static boolean isMi() {
        CloudUtilsSystemInfoService service = new UtilsSystemInfoServiceImpl();
        String brand = service.getBrand();
        return BRAND.XIAOMI.equalsIgnoreCase(brand) || BRAND.REDMI.equalsIgnoreCase(brand);
    }
}
