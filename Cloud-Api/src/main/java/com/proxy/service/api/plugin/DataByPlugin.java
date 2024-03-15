package com.proxy.service.api.plugin;

import com.proxy.service.base.AbstractServiceCache;

import java.util.List;

/**
 * @author : cangHX
 * on 2021/04/27  8:19 PM
 */
public class DataByPlugin {

    private static class Factory {
        private static final DataByPlugin INSTANCE = new DataByPlugin();
    }

    public static DataByPlugin getInstance() {
        return Factory.INSTANCE;
    }

    public List<AbstractServiceCache> getClasses(List<AbstractServiceCache> list) {
        return list;
    }

}
