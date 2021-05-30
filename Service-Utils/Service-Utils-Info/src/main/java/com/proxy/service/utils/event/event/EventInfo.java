package com.proxy.service.utils.event.event;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : cangHX
 * on 2021/05/25  11:09 PM
 */
public class EventInfo {

    private final HashSet<Class<?>> hashSet = new HashSet<>();

    private EventInfo() {
    }

    public static EventInfo create() {
        return new EventInfo();
    }

    public void addClasses(Set<Class<?>> set) {
        if (set == null || set.size() == 0) {
            return;
        }
        hashSet.addAll(set);
    }

    public boolean contains(Class<?> aClass) {
        for (Class<?> aClass1 : new HashSet<>(hashSet)) {
            if (aClass1.isAssignableFrom(aClass)) {
                return true;
            }
        }
        return false;
    }

}
