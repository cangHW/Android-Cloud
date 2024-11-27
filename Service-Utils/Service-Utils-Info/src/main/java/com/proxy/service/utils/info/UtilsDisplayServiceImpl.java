package com.proxy.service.utils.info;

import android.content.Context;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsDisplayService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.log.Logger;

/**
 * @author: cangHX
 * on 2020/06/11  12:12
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_DISPLAY)
public class UtilsDisplayServiceImpl implements CloudUtilsDisplayService {

    /**
     * px转dp
     *
     * @param pxValue : 需要转化的px数值
     * @return 转化后的dp数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:24
     */
    @Override
    public int px2dp(float pxValue) {
        int value = 0;
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return value;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        value = (int) (pxValue / scale + 0.5f);
        return value;
    }

    /**
     * dp转px
     *
     * @param dpValue : 需要转化的dp数值
     * @return 转化后的px数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:26
     */
    @Override
    public int dp2px(float dpValue) {
        int value = 0;
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return value;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        value = (int) (dpValue * scale + 0.5f);
        return value;
    }

    /**
     * px转sp
     *
     * @param pxValue : 需要转化的px数值
     * @return 转化后的sp数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:27
     */
    @Override
    public int px2sp(float pxValue) {
        int value = 0;
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return value;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        value = (int) (pxValue / fontScale + 0.5f);
        return value;
    }

    /**
     * sp转px
     *
     * @param spValue : 需要转化的sp数值
     * @return 转化后的px数值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:28
     */
    @Override
    public int sp2px(float spValue) {
        int value = 0;
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return value;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        value = (int) (spValue * fontScale + 0.5f);
        return value;
    }
}
