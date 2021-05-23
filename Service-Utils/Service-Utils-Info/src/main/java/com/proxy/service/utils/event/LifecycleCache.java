package com.proxy.service.utils.event;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.event.Event;
import com.proxy.service.api.lifecycle.FragmentLifecycleState;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.event.lifecycle.LifecycleHolder;
import com.proxy.service.utils.event.lifecycle.Lifecycle;
import com.proxy.service.utils.info.UtilsLifecycleServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author : cangHX
 * on 2021/05/20  10:55 PM
 */
public enum LifecycleCache {

    INSTANCE;

    private static final WeakHashMap<Event, HashSet<Class<?>>> CLASS_MAPPER = new WeakHashMap<>();
    private static final HashMap<Class<?>, ArrayList<Lifecycle>> LIFECYCLE_MAPPER = new HashMap<>();

    public void addCallback(Activity activity, Set<Class<?>> set, Event event) {
        if (set == null || set.size() == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout(event + " : Set<Class<?>>").build());
            return;
        }
        for (Class<?> aClass : new HashSet<>(set)) {
            ArrayList<Lifecycle> lifecycles = LIFECYCLE_MAPPER.get(aClass);
            if (lifecycles == null) {
                lifecycles = new ArrayList<>();
                LIFECYCLE_MAPPER.put(aClass, lifecycles);
            }
            if (getLifecycleByEvent(lifecycles, event) != null) {
                Logger.Warning(CloudApiError.DATA_DUPLICATION.setMsg("We already have an " + event + " here: CloudUtilsEventService").build());
                continue;
            }

            LifecycleHolder holder = new LifecycleHolder(event);
            UtilsLifecycleServiceImpl service = new UtilsLifecycleServiceImpl();
            service.bind(activity, holder
                    , LifecycleState.LIFECYCLE_PAUSE
                    , LifecycleState.LIFECYCLE_RESUME
                    , LifecycleState.LIFECYCLE_DESTROY
            );
            lifecycles.add(holder);
        }
    }

    public void addCallback(Fragment fragment, Set<Class<?>> set, Event event) {
        if (set == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout(event + " : Set<Class<?>>").build());
            return;
        }
        for (Class<?> aClass : new HashSet<>(set)) {
            ArrayList<Lifecycle> lifecycles = LIFECYCLE_MAPPER.get(aClass);
            if (lifecycles == null) {
                lifecycles = new ArrayList<>();
                LIFECYCLE_MAPPER.put(aClass, lifecycles);
            }
            if (getLifecycleByEvent(lifecycles, event) != null) {
                Logger.Warning(CloudApiError.DATA_DUPLICATION.setMsg("We already have an " + event + " here: CloudUtilsEventService").build());
                continue;
            }
            LifecycleHolder holder = new LifecycleHolder(event);
            UtilsLifecycleServiceImpl service = new UtilsLifecycleServiceImpl();
            service.bind(fragment, holder
                    , FragmentLifecycleState.LIFECYCLE_RESUME
                    , FragmentLifecycleState.LIFECYCLE_HIDE
                    , FragmentLifecycleState.LIFECYCLE_SHOW
                    , FragmentLifecycleState.LIFECYCLE_PAUSE
                    , FragmentLifecycleState.LIFECYCLE_DESTROY
            );
            lifecycles.add(holder);
        }
    }

    public void remove(Event event) {
        if (event == null) {
            return;
        }
        HashSet<Class<?>> hashSet = CLASS_MAPPER.get(event);
        if (hashSet == null || hashSet.size() == 0) {
            return;
        }
        CLASS_MAPPER.remove(event);
        for (Class<?> aClass : new HashSet<>(hashSet)) {
            ArrayList<Lifecycle> arrayList = LIFECYCLE_MAPPER.get(aClass);
            if (arrayList == null) {
                LIFECYCLE_MAPPER.remove(aClass);
                continue;
            }
            if (arrayList.size() == 0) {
                continue;
            }
            Lifecycle lifecycle = getLifecycleByEvent(arrayList, event);
            if (lifecycle == null) {
                continue;
            }
            arrayList.remove(lifecycle);
        }
    }

    public void send(Object object, boolean isPost) {
        ArrayList<Lifecycle> arrayList = LIFECYCLE_MAPPER.get(object.getClass());
        if (arrayList == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("Missing callback to receive " + object.getClass()).build());
            return;
        }
        for (Lifecycle lifecycle : new ArrayList<>(arrayList)) {
            if (lifecycle.isDestroy()) {
                arrayList.remove(lifecycle);
                continue;
            }
            if (isPost) {
                lifecycle.post(object);
            } else {
                lifecycle.set(object);
            }
        }
    }

    private Lifecycle getLifecycleByEvent(ArrayList<Lifecycle> lifecycles, Event event) {
        for (Lifecycle lifecycle : new ArrayList<>(lifecycles)) {
            if (lifecycle.isDestroy()) {
                lifecycles.remove(lifecycle);
                continue;
            }
            if (lifecycle.isSame(event)) {
                return lifecycle;
            }
        }
        return null;
    }
}
