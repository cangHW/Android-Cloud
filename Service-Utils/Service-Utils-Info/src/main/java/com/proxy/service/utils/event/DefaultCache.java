package com.proxy.service.utils.event;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.event.CloudMainThreadEventCallback;
import com.proxy.service.api.event.CloudWorkThreadEventCallback;
import com.proxy.service.api.event.Event;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.utils.thread.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author : cangHX
 * on 2021/05/19  7:21 PM
 */
public enum DefaultCache {

    INSTANCE;

    private static final WeakHashMap<Event, HashSet<Class<?>>> CLASS_MAPPER = new WeakHashMap<>();
    private static final HashMap<Class<?>, ArrayList<WeakReference<Event>>> EVENT_MAPPER = new HashMap<>();

    public void addEventCallback(Set<Class<?>> set, Event event) {
        if (set == null || set.size() == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout(event + " : Set<Class<?>>").build());
            return;
        }
        HashSet<Class<?>> hashSet = CLASS_MAPPER.get(event);
        if (hashSet == null) {
            hashSet = new HashSet<>();
            CLASS_MAPPER.put(event, hashSet);
        }
        hashSet.addAll(set);

        for (Class<?> aClass : new HashSet<>(set)) {
            ArrayList<WeakReference<Event>> events = EVENT_MAPPER.get(aClass);
            if (events == null) {
                events = new ArrayList<>();
                EVENT_MAPPER.put(aClass, events);
            }
            if (WeakReferenceUtils.checkValueIsSame(events, event)) {
                Logger.Warning(CloudApiError.DATA_DUPLICATION.setMsg("We already have an " + event + " here: CloudUtilsEventService").build());
                continue;
            }
            events.add(new WeakReference<>(event));
            refreshList(events);
        }
    }

    public void send(final Object object) {
        ArrayList<WeakReference<Event>> events = EVENT_MAPPER.get(object.getClass());
        WeakReferenceUtils.checkValueIsEmpty(events, new WeakReferenceUtils.Callback<Event>() {
            @Override
            public void onCallback(WeakReference<Event> weakReference, final Event event) {
                if (event instanceof CloudMainThreadEventCallback) {
                    ThreadManager.postMain(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((CloudMainThreadEventCallback) event).onMainEvent(object);
                            } catch (Throwable throwable) {
                                Logger.Debug(throwable);
                            }
                        }
                    });
                } else if (event instanceof CloudWorkThreadEventCallback) {
                    ThreadManager.postWork(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((CloudWorkThreadEventCallback) event).onWorkEvent(object);
                            } catch (Throwable throwable) {
                                Logger.Debug(throwable);
                            }
                        }
                    });
                }
            }
        });
    }

    public void remove(final Event event) {
        if (event == null) {
            return;
        }
        HashSet<Class<?>> hashSet = CLASS_MAPPER.get(event);
        if (hashSet == null || hashSet.size() == 0) {
            return;
        }
        CLASS_MAPPER.remove(event);
        for (Class<?> aClass : new HashSet<>(hashSet)) {
            final ArrayList<WeakReference<Event>> events = EVENT_MAPPER.get(aClass);
            if (events == null) {
                EVENT_MAPPER.remove(aClass);
                continue;
            }
            if (events.size() == 0) {
                continue;
            }
            WeakReferenceUtils.checkValueIsEmpty(events, new WeakReferenceUtils.Callback<Event>() {
                @Override
                public void onCallback(WeakReference<Event> weakReference, Event e) {
                    if (e != event) {
                        return;
                    }
                    events.remove(weakReference);
                }
            });
        }
    }

    private void refreshList(ArrayList<WeakReference<Event>> events) {
        for (WeakReference<Event> weakReference : new ArrayList<>(events)) {
            if (weakReference == null) {
                events.remove(null);
                continue;
            }
            Event event = weakReference.get();
            if (event == null) {
                events.remove(weakReference);
            }
        }
    }

}
