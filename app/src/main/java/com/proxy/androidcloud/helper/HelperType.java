package com.proxy.androidcloud.helper;

import com.proxy.androidcloud.module_library.ThreadPoolHelper;

/**
 * @author : cangHX
 * on 2020/08/13  10:06 PM
 */
public enum HelperType {

    /**
     * 线程池
     */
    THREAD_POOL {
        @Override
        public String serviceName() {
            return "CloudUtilsTaskService";
        }

        @Override
        public AbstractHelper create() {
            return new ThreadPoolHelper();
        }
    };

    public abstract String serviceName();

    public abstract AbstractHelper create();
}
