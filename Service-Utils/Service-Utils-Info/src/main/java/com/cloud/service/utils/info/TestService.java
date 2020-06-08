package com.cloud.service.utils.info;

import com.cloud.annotations.CloudNewInstance;
import com.cloud.annotations.CloudService;
import com.cloud.api.services.CloudUtilsDeviceService;

/**
 * @author: cangHX
 * on 2020/06/05  15:13
 */
@CloudService(serviceTag = "sss")
@CloudNewInstance()
public class TestService implements CloudUtilsDeviceService {

    @Override
    public String getImel() {
        return "asd";
    }
}
