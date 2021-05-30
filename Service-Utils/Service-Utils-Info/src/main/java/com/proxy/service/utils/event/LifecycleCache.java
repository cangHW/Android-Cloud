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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author : cangHX
 * on 2021/05/20  10:55 PM
 */
public enum LifecycleCache {

    INSTANCE;

    private static final WeakHashMap<Event, Lifecycle> LIFECYCLE_MAPPER = new WeakHashMap<>();

    public void addCallback(Activity activity, Set<Class<?>> set, Event event) {
        if (set == null || set.size() == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout(event + " : Set<Class<?>>").build());
            return;
        }
        Lifecycle lifecycle = LIFECYCLE_MAPPER.get(event);
        if (lifecycle != null) {
            Logger.Error(CloudApiError.DATA_DUPLICATION.setMsg("We already have an " + event + " here: CloudUtilsEventService").build());
            return;
        }
        lifecycle = new LifecycleHolder(event);
        UtilsLifecycleServiceImpl service = new UtilsLifecycleServiceImpl();
        service.bind(activity, (LifecycleHolder) lifecycle
                , LifecycleState.LIFECYCLE_PAUSE
                , LifecycleState.LIFECYCLE_RESUME
                , LifecycleState.LIFECYCLE_DESTROY
        );
        LIFECYCLE_MAPPER.put(event, lifecycle);
        lifecycle.addClasses(set);
    }

    public void addCallback(Fragment fragment, Set<Class<?>> set, Event event) {
        if (set == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout(event + " : Set<Class<?>>").build());
            return;
        }

        Lifecycle lifecycle = LIFECYCLE_MAPPER.get(event);
        if (lifecycle != null) {
            Logger.Error(CloudApiError.DATA_DUPLICATION.setMsg("We already have an " + event + " here: CloudUtilsEventService").build());
            return;
        }
        lifecycle = new LifecycleHolder(event);
        UtilsLifecycleServiceImpl service = new UtilsLifecycleServiceImpl();
        service.bind(fragment, (LifecycleHolder) lifecycle
                , FragmentLifecycleState.LIFECYCLE_RESUME
                , FragmentLifecycleState.LIFECYCLE_HIDE
                , FragmentLifecycleState.LIFECYCLE_SHOW
                , FragmentLifecycleState.LIFECYCLE_PAUSE
                , FragmentLifecycleState.LIFECYCLE_DESTROY
        );
        LIFECYCLE_MAPPER.put(event, lifecycle);
        lifecycle.addClasses(set);
    }

    public void remove(Event event) {
        if (event == null) {
            return;
        }
        Lifecycle lifecycle = LIFECYCLE_MAPPER.get(event);
        if (lifecycle != null) {
            lifecycle.destroy();
        }
        LIFECYCLE_MAPPER.remove(event);
    }

    public void send(Object object, boolean isPost) {
        for (Map.Entry<Event, Lifecycle> eventLifecycleEntry : new HashSet<>(LIFECYCLE_MAPPER.entrySet())) {
            Lifecycle lifecycle = eventLifecycleEntry.getValue();
            if (lifecycle.isDestroy()) {
                LIFECYCLE_MAPPER.remove(eventLifecycleEntry.getKey());
                continue;
            }
            if (lifecycle.contains(object.getClass())) {
                if (isPost) {
                    lifecycle.post(object);
                } else {
                    lifecycle.set(object);
                }
            }
        }
    }
}
