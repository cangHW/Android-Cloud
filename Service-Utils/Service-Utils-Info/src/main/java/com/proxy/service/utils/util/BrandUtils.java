package com.proxy.service.utils.util;

import com.proxy.service.api.services.CloudUtilsSystemInfoService;
import com.proxy.service.utils.info.CloudUtilsSystemInfoServiceImpl;

/**
 * @author: cangHX
 * on 2020/06/19  19:02
 */
public class BrandUtils {

    /**
     * 华为
     */
    public static final String HW = "Huawei";
    /**
     * 荣耀
     */
    public static final String HO = "Honor";

    /**
     * 当前设备是否是华为
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 19:05
     */
    public static boolean isHw() {
        CloudUtilsSystemInfoService service = new CloudUtilsSystemInfoServiceImpl();
        String brand = service.getBrand();

        return HW.equalsIgnoreCase(brand) || HO.equalsIgnoreCase(brand);
    }


}
