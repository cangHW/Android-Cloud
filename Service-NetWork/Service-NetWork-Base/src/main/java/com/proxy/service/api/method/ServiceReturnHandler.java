package com.proxy.service.api.method;

import com.proxy.service.api.callback.request.CallAdapter;
import com.proxy.service.api.impl.DefaultCallFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/07/29  10:41 PM
 */
public class ServiceReturnHandler {

    private static final ArrayList<CallAdapter.CallFactory> CALL_FACTORIES = new ArrayList<>();

    static {
        CALL_FACTORIES.add(new DefaultCallFactory());
    }

    private Type mType;

    public ServiceReturnHandler(Type type) {
        this.mType = type;
    }

    public CallAdapter<?, ?> getAdapter() {
        for (CallAdapter.CallFactory factory : CALL_FACTORIES) {
            CallAdapter<?, ?> adapter = factory.get(mType);
            if (adapter == null) {
                continue;
            }
            return adapter;
        }
        return null;
    }

    public static void addCallFactory(CallAdapter.CallFactory callFactory) {
        if (callFactory == null) {
            return;
        }
        CALL_FACTORIES.add(0, callFactory);
    }
}
