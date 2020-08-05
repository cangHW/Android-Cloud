package com.proxy.service.api.cache;

import com.proxy.service.api.base.CloudNetWorkMock;

import java.util.ArrayList;

/**
 * mock 缓存
 *
 * @author : cangHX
 * on 2020/08/04  9:38 PM
 */
public class MockCache {

    private static final ArrayList<CloudNetWorkMock> MOCKS_MAPPER = new ArrayList<>();

    public static ArrayList<CloudNetWorkMock> getMocks() {
        return new ArrayList<>(MOCKS_MAPPER);
    }

    public static void putMock(CloudNetWorkMock mock) {
        if (mock == null) {
            return;
        }
        MOCKS_MAPPER.add(mock);
    }

}
