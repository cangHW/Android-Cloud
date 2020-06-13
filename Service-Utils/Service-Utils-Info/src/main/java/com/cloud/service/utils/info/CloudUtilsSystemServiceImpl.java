package com.cloud.service.utils.info;

import androidx.annotation.NonNull;

import com.cloud.annotations.CloudService;
import com.cloud.api.services.CloudUtilsSystemService;
import com.cloud.api.tag.CloudServiceTagUtils;

/**
 * @author: cangHX
 * on 2020/06/11  12:45
 */
@CloudService(serviceTag = CloudServiceTagUtils.UTILS_SYSTEM)
public class CloudUtilsSystemServiceImpl implements CloudUtilsSystemService {

    /**
     * 获取设备ID
     *
     * @return 设备ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:31
     */
    @Override
    public String getDeviceId() {
        return null;
    }

    /**
     * GPS定位是否开启
     *
     * @return true 开启，false 未开启，默认未开启
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:32
     */
    @Override
    public boolean isGpsEnabled() {
        return false;
    }

    /**
     * MSA，获取aaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    @Override
    public void getAAID(@NonNull AppIdsUpdater appIdsUpdater) {

    }

    /**
     * MSA，获取oaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    @Override
    public void getOAID(@NonNull AppIdsUpdater appIdsUpdater) {

    }

    /**
     * MSA，获取vaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    @Override
    public void getVAID(@NonNull AppIdsUpdater appIdsUpdater) {

    }

}
