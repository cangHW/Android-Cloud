package com.proxy.service.api.request.cache;

import com.proxy.service.api.callback.request.CloudNetWorkCallAdapter;
import com.proxy.service.api.request.impl.DefaultCallAdapterFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/08/05  8:55 PM
 */
public class CallFactoryCache {

    private static final ArrayList<CloudNetWorkCallAdapter.Factory> CALL_ADAPTER_FACTORIES = new ArrayList<>();

    static {
        CALL_ADAPTER_FACTORIES.add(new DefaultCallAdapterFactory());
    }

    public static CloudNetWorkCallAdapter<?, ?> getAdapter(Type type) {
        for (CloudNetWorkCallAdapter.Factory factory : CALL_ADAPTER_FACTORIES) {
            CloudNetWorkCallAdapter<?, ?> adapter = factory.get(type);
            if (adapter == null) {
                continue;
            }
            return adapter;
        }
        return null;
    }

    public static void addCallFactory(CloudNetWorkCallAdapter.Factory factory) {
        if (factory == null) {
            return;
        }
        CALL_ADAPTER_FACTORIES.add(0, factory);
    }

}
