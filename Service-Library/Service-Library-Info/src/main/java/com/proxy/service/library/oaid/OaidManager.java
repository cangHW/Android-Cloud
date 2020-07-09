package com.proxy.service.library.oaid;

import androidx.annotation.NonNull;

import com.proxy.service.library.oaid.callback.OaidRequestCallback;
import com.proxy.service.library.oaid.empty.OaidRequestCallbackEmpty;
import com.proxy.service.library.oaid.hw.HwOaidRequestCallbackImpl;
import com.proxy.service.library.oaid.mi.MiOaidRequestCallbackImpl;
import com.proxy.service.library.util.BrandUtils;

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
