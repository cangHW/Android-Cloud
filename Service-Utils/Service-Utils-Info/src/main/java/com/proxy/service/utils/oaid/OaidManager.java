package com.proxy.service.utils.oaid;

import androidx.annotation.NonNull;

import com.proxy.service.utils.oaid.callback.OaidRequestCallback;
import com.proxy.service.utils.oaid.empty.OaidRequestCallbackEmpty;
import com.proxy.service.utils.oaid.hw.HwOaidRequestCallbackImpl;
import com.proxy.service.utils.util.BrandUtils;

/**
 * @author: cangHX
 * on 2020/06/19  17:56
 */
public class OaidManager {

    @NonNull
    public static OaidRequestCallback getOaidRequestCallback() {
        if (BrandUtils.isHw()) {
            return new HwOaidRequestCallbackImpl();
        }


        return new OaidRequestCallbackEmpty();
    }


}
