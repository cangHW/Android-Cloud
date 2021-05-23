package com.proxy.service.api.context.lifecycle;

import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.context.listener.CloudLifecycleListener;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author : cangHX
 * on 2021/05/20  8:36 PM
 */
public class LifecycleBean {

    private final HashMap<LifecycleState, List<WeakReference<CloudLifecycleListener>>> LIFECYCLE_MAPPER = new HashMap<>();

    public void setLifecycleListener(LifecycleState state, CloudLifecycleListener lifecycleListener) {
        List<WeakReference<CloudLifecycleListener>> list = LIFECYCLE_MAPPER.get(state);
        if (list == null) {
            list = new ArrayList<>();
            LIFECYCLE_MAPPER.put(state, list);
        }
        if (WeakReferenceUtils.checkValueIsSame(list, lifecycleListener)) {
            Logger.Debug(CloudApiError.DATA_DUPLICATION.setAbout("Repeat registration declaration cycle listening. " + lifecycleListener).build());
            return;
        }
        list.add(new WeakReference<>(lifecycleListener));
    }

    public List<WeakReference<CloudLifecycleListener>> getLifecycleListener(LifecycleState state) {
        return LIFECYCLE_MAPPER.get(state);
    }

}
