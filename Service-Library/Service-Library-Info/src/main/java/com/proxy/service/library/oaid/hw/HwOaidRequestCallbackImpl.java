package com.proxy.service.library.oaid.hw;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.api.context.cache.ActivityStack;
import com.proxy.service.api.services.CloudUtilsSystemInfoService;
import com.proxy.service.library.oaid.callback.OaidRequestCallback;

/**
 * @author: cangHX
 * on 2020/06/19  18:18
 * <p>
 * 华为设备
 */
public class HwOaidRequestCallbackImpl implements OaidRequestCallback {
    /**
     * 功能是否兼容当前设备
     *
     * @return true 兼容，false 不兼容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:01
     */
    @Override
    public boolean isSupported() {
        return true;
    }

    /**
     * 请求oaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:17
     */
    @Override
    public void request(@NonNull final CloudUtilsSystemInfoService.AppIdsUpdater appIdsUpdater) {
        HwOaidAidlUtil oaidAidlUtil = new HwOaidAidlUtil(ActivityStack.getApplication());
        oaidAidlUtil.getOaid(new HwOaidCallback() {
            @Override
            public void onSuccuss(String oaid, boolean isOaidTrackLimited) {
                if (TextUtils.isEmpty(oaid)) {
                    appIdsUpdater.onIdsAvalId("");
                } else {
                    appIdsUpdater.onIdsAvalId(oaid);
                }
            }

            @Override
            public void onFail(String errMsg) {
                appIdsUpdater.onIdsAvalId("");
            }
        });
    }
}
