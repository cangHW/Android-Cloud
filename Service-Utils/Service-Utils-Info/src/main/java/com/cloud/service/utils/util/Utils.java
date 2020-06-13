package com.cloud.service.utils.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

import com.cloud.api.context.ContextManager;
import com.cloud.api.error.CloudApiError;
import com.cloud.api.utils.Logger;
import com.cloud.service.utils.cache.Cache;
import com.cloud.service.utils.info.CloudUtilsAppServiceImpl;

/**
 * @author: cangHX
 * on 2020/06/12  16:36
 */
public class Utils {

    /**
     * 获取authorities，供安装使用
     *
     * @param classUri 需要获取Meta Data的组件路径
     * @param suffix   Authorities后缀
     * @return Provider Authorities
     */
    public static String getProviderAuthoritiesFromManifest(String classUri, String suffix) {
        String authorities = "";
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return authorities;
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return authorities;
        }
        CloudUtilsAppServiceImpl service = new CloudUtilsAppServiceImpl();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(service.getPackageName(), PackageManager.GET_PROVIDERS);
            ProviderInfo[] providers = packageInfo.providers;
            if (providers == null) {
                return authorities;
            }
            for (ProviderInfo providerInfo : providers) {
                if (!classUri.equals(providerInfo.name)) {
                    continue;
                }
                if (providerInfo.authority.endsWith(suffix)) {
                    authorities = providerInfo.authority;
                }
            }
        } catch (Throwable t) {
            Logger.Debug(t.getMessage());
        }
        return authorities;
    }

}
