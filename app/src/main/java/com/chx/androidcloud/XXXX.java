package com.chx.androidcloud;

import com.cloud.annotations.CloudService;
import com.cloud.api.services.CloudUtilsDeviceService;

/**
 * @author: cangHX
 * on 2020/06/05  18:36
 */
@CloudService(serviceTag = "sss")
public class XXXX implements CloudUtilsDeviceService {
    @Override
    public String getImel() {
        return "qqqq";
    }
}
