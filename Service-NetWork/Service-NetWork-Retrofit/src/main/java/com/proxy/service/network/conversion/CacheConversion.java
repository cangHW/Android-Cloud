package com.proxy.service.network.conversion;

import com.proxy.service.api.request.base.CloudNetWorkCache;

import okhttp3.Cache;

/**
 * @author : cangHX
 * on 2020/7/23 9:14 PM
 */
public final class CacheConversion {

    public static Cache create(CloudNetWorkCache cache) {
        return new Cache(cache.getDirectory(), cache.getMaxSize());
    }

}
