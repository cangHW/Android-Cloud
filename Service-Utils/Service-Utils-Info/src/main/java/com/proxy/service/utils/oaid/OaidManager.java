package com.proxy.service.utils.oaid;

import androidx.annotation.NonNull;

import com.proxy.service.utils.oaid.callback.OaidRequestCallback;
import com.proxy.service.utils.oaid.empty.OaidRequestCallbackEmpty;
import com.proxy.service.utils.oaid.hw.HwOaidRequestCallbackImpl;
import com.proxy.service.utils.oaid.mi.MiOaidRequestCallbackImpl;
import com.proxy.service.utils.util.BrandUtils;

/**
 * 获取oaid管理类
 *
 * @author: cangHX
 * on 2020/06/19  17:56
 */
public class OaidManager {

    @NonNull
    public static OaidRequestCallback getOaidRequestCallback() {
        if (BrandUtils.isHw()) {
            return new HwOaidRequestCallbackImpl();
        }
        if (BrandUtils.isMi()) {
            return new MiOaidRequestCallbackImpl();
        }
        //TODO oaid相关   MSA    小米

        return new OaidRequestCallbackEmpty();
    }


}
