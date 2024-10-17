package com.proxy.service.api.plugin;

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

    public List<String> getClasses(List<String> list) {
        return list;
    }

}
