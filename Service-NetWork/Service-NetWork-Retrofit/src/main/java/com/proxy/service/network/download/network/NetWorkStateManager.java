package com.proxy.service.network.download.network;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.net.CloudNetWorkCallback;
import com.proxy.service.api.net.CloudNetWorkType;
import com.proxy.service.api.services.CloudUtilsNetWorkService;

/**
 * @author : cangHX
 * on 2020/09/03  10:10 PM
 */
public class NetWorkStateManager implements CloudNetWorkCallback {

    private NetWorkStateManager() {
    }

    private static class Factory {
        private static final NetWorkStateManager INSTANCE = new NetWorkStateManager();
    }

    public static NetWorkStateManager getInstance() {
        return Factory.INSTANCE;
    }

    public void setNetworkStateWatchEnable(boolean networkStateWatchEnable) {
        CloudUtilsNetWorkService netWorkService = CloudSystem.getService(CloudUtilsNetWorkService.class);
        if (netWorkService == null) {
            return;
        }
        if (networkStateWatchEnable) {
            netWorkService.addNetWorkStatusCallback(this);
        } else {
            netWorkService.removeNetWorkStatusCallback(this);
        }
    }

    /**
     * 接收到消息
     *
     * @param type : 网络类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 18:04
     */
    @Override
    public void onReceive(CloudNetWorkType type) {

    }

    /**
     * 断开连接
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 5:12 PM
     */
    @Override
    public void disConnect() {

    }
}
