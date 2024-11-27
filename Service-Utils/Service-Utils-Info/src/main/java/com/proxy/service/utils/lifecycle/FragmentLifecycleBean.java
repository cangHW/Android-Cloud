package com.proxy.service.utils.lifecycle;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.lifecycle.CloudFragmentLifecycleListener;
import com.proxy.service.api.lifecycle.FragmentLifecycleState;
import com.proxy.service.api.log.Logger;
import com.proxy.service.api.log.WeakReferenceUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author : cangHX
 * on 2021/05/20  8:36 PM
 */
public class FragmentLifecycleBean {

    private final HashMap<FragmentLifecycleState, List<WeakReference<CloudFragmentLifecycleListener>>> LIFECYCLE_MAPPER = new HashMap<>();

    public void setLifecycleListener(FragmentLifecycleState state, CloudFragmentLifecycleListener lifecycleListener) {
        List<WeakReference<CloudFragmentLifecycleListener>> list = LIFECYCLE_MAPPER.get(state);
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

    public List<WeakReference<CloudFragmentLifecycleListener>> getLifecycleListener(FragmentLifecycleState state) {
        return LIFECYCLE_MAPPER.get(state);
    }

    public void remove(CloudFragmentLifecycleListener lifecycleListener) {
        for (Map.Entry<FragmentLifecycleState, List<WeakReference<CloudFragmentLifecycleListener>>> lifecycleStateListEntry : new HashSet<>(LIFECYCLE_MAPPER.entrySet())) {
            List<WeakReference<CloudFragmentLifecycleListener>> list = lifecycleStateListEntry.getValue();
            WeakReferenceUtils.removeValue(list, lifecycleListener);
        }
    }

}
