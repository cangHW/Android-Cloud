package com.proxy.service.api.services;

import android.net.wifi.ScanResult;

import androidx.annotation.NonNull;

import com.proxy.service.api.net.CloudNetWorkCallback;
import com.proxy.service.api.net.CloudNetWorkType;
import com.proxy.service.base.BaseService;

import java.util.List;

/**
 * 网络相关
 *
 * @author : cangHX
 * on 2020/08/28  7:05 PM
 */
public interface CloudUtilsNetWorkService extends BaseService {

    /**
     * 是否有网络
     *
     * @return true 有，false 无
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:13 PM
     */
    boolean isConnected();

    /**
     * 获取网络类型
     *
     * @return 网络类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 6:15 PM
     */
    @NonNull
    CloudNetWorkType getNetworkType();

    /**
     * 获取当前 wifi 的站点名称
     *
     * @return 当前 wifi 的站点名称
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:32 PM
     */
    @NonNull
    String getWifiBssId();

    /**
     * 获取当前 wifi 的信号强度
     * 默认为 -1
     *
     * @return 当前 wifi 的信号强度
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:32 PM
     */
    int getWifiRssI();

    /**
     * 获取当前 wifi 的名称
     *
     * @return 当前 wifi 的名称
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:32 PM
     */
    @NonNull
    String getWifiSsId();

    /**
     * 获取扫描到的 Wi-Fi 信息列表
     *
     * @return 扫描到的 Wi-Fi 信息列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:17 PM
     */
    @NonNull
    List<ScanResult> getScanWifiInfoList();

    /**
     * 获取 ipv6 地址
     *
     * @return ipv6 地址列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 3:02 PM
     */
    @NonNull
    List<String> getIpv6Address();

    /**
     * 添加网络状态变化回调
     *
     * @param callback : 网络状态变化回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:10 PM
     */
    void addNetWorkStatusCallback(@NonNull CloudNetWorkCallback callback);

    /**
     * 移除网络状态变化回调
     *
     * @param callback : 网络状态变化回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:10 PM
     */
    void removeNetWorkStatusCallback(@NonNull CloudNetWorkCallback callback);

}
