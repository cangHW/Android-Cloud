package com.proxy.service.utils.event;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.event.CloudMainThreadEventCallback;
import com.proxy.service.api.event.CloudWorkThreadEventCallback;
import com.proxy.service.api.event.Event;
import com.proxy.service.api.log.Logger;
import com.proxy.service.utils.event.event.EventInfo;
import com.proxy.service.utils.thread.ThreadManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author : cangHX
 * on 2021/05/19  7:21 PM
 */
public enum DefaultCache {

    INSTANCE;

    private static final WeakHashMap<Event, EventInfo> EVENT_MAPPER = new WeakHashMap<>();

    public void addEventCallback(Set<Class<?>> set, Event event) {
        if (set == null || set.size() == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout(event + " : Set<Class<?>>").build());
            return;
        }
        EventInfo eventInfo = EVENT_MAPPER.get(event);
        if (eventInfo == null) {
            eventInfo = EventInfo.create();
            EVENT_MAPPER.put(event, eventInfo);
        }
        eventInfo.addClasses(set);
    }

    public void send(final Object object) {
        for (Map.Entry<Event, EventInfo> eventEventInfoEntry : new HashSet<>(EVENT_MAPPER.entrySet())) {
            EventInfo eventInfo = eventEventInfoEntry.getValue();
            if (eventInfo.contains(object.getClass())) {
                final Event event = eventEventInfoEntry.getKey();
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
        }
    }

    public void remove(final Event event) {
        if (event == null) {
            return;
        }
        EVENT_MAPPER.remove(event);
    }
}
