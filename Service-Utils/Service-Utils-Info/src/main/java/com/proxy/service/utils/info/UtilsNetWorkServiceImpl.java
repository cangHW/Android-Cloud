package com.proxy.service.utils.info;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.net.CloudNetWorkCallback;
import com.proxy.service.api.net.CloudNetWorkType;
import com.proxy.service.api.services.CloudUtilsNetWorkService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.network.NetWorkReceiverListenerManager;
import com.proxy.service.utils.receiver.UtilsBroadcastReceiver;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/31  9:19 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_NET_WORK)
public class UtilsNetWorkServiceImpl implements CloudUtilsNetWorkService {
    /**
     * 是否有网络
     *
     * @return true 有，false 无
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:13 PM
     */
    @Override
    public boolean isConnected() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return false;
        }
        UtilsPermissionServiceImpl permissionService = new UtilsPermissionServiceImpl();
        if (!permissionService.selfPermissionGranted(Manifest.permission.ACCESS_NETWORK_STATE)) {
            Logger.Debug(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.ACCESS_NETWORK_STATE).build());
            return false;
        }
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                return false;
            }
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return false;
    }

    /**
     * 获取网络类型
     *
     * @return 网络类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 6:15 PM
     */
    @NonNull
    @SuppressWarnings("ConstantConditions")
    @Override
    public CloudNetWorkType getNetworkType() {
        if (!isConnected()) {
            return CloudNetWorkType.ERROR;
        }
        NetworkInfo networkInfo = null;
        try {
            ConnectivityManager manager = (ConnectivityManager) ContextManager.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = manager.getActiveNetworkInfo();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        if (networkInfo == null) {
            return CloudNetWorkType.ERROR;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return CloudNetWorkType.WIFI;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = networkInfo.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return CloudNetWorkType.UNKNOWN;
                case TelephonyManager.NETWORK_TYPE_GSM:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return CloudNetWorkType.MOBILE_2G;
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return CloudNetWorkType.MOBILE_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                case TelephonyManager.NETWORK_TYPE_IWLAN:
                    return CloudNetWorkType.MOBILE_4G;
                case TelephonyManager.NETWORK_TYPE_NR:
                    return CloudNetWorkType.MOBILE_5G;
                default:
                    String strSubTypeName = networkInfo.getSubtypeName();
                    if ("WCDMA".equalsIgnoreCase(strSubTypeName) || "CDMA2000".equalsIgnoreCase(strSubTypeName)) {
                        return CloudNetWorkType.MOBILE_3G;
                    } else {
                        return CloudNetWorkType.UNKNOWN;
                    }
            }
        }
        return CloudNetWorkType.UNKNOWN;
    }

    /**
     * 获取当前 wifi 的站点名称
     *
     * @return 当前 wifi 的站点名称
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:32 PM
     */
    @NonNull
    @Override
    public String getWifiBssId() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return "";
        }
        UtilsPermissionServiceImpl permissionService = new UtilsPermissionServiceImpl();
        if (!permissionService.selfPermissionGranted(Manifest.permission.ACCESS_WIFI_STATE)) {
            Logger.Debug(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.ACCESS_WIFI_STATE).build());
            return "";
        }
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (manager == null) {
                return "";
            }
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getBSSID())) {
                String bssId = wifiInfo.getBSSID();
                return bssId == null ? "" : bssId;
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return "";
    }

    /**
     * 获取当前 wifi 的信号强度
     * 默认为 -1
     *
     * @return 当前 wifi 的信号强度
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:32 PM
     */
    @Override
    public int getWifiRssI() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return -1;
        }
        UtilsPermissionServiceImpl permissionService = new UtilsPermissionServiceImpl();
        if (!permissionService.selfPermissionGranted(Manifest.permission.ACCESS_WIFI_STATE)) {
            Logger.Debug(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.ACCESS_WIFI_STATE).build());
            return -1;
        }
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (manager == null) {
                return -1;
            }
            WifiInfo wifiInfo = manager.getConnectionInfo();
            return wifiInfo.getRssi();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return -1;
    }

    /**
     * 获取当前 wifi 的名称
     *
     * @return 当前 wifi 的名称
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:32 PM
     */
    @NonNull
    @Override
    public String getWifiSsId() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return "";
        }
        UtilsPermissionServiceImpl permissionService = new UtilsPermissionServiceImpl();
        if (!permissionService.selfPermissionGranted(Manifest.permission.ACCESS_WIFI_STATE)) {
            Logger.Debug(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.ACCESS_WIFI_STATE).build());
            return "";
        }
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (manager == null) {
                return "";
            }
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getSSID())) {
                return wifiInfo.getSSID();
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return "";
    }

    /**
     * 获取扫描到的 Wi-Fi 信息列表
     *
     * @return 扫描到的 Wi-Fi 信息列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:17 PM
     */
    @NonNull
    @Override
    public List<ScanResult> getScanWifiInfoList() {
        List<ScanResult> infoList = new ArrayList<>();

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return infoList;
        }
        UtilsPermissionServiceImpl permissionService = new UtilsPermissionServiceImpl();
        if (!permissionService.selfPermissionGranted(Manifest.permission.ACCESS_WIFI_STATE)) {
            Logger.Debug(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.ACCESS_WIFI_STATE).build());
            return infoList;
        }
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (manager == null) {
                return infoList;
            }
            List<ScanResult> scanResults = manager.getScanResults();
            if (scanResults != null) {
                infoList.addAll(scanResults);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return infoList;
    }

    /**
     * 获取 ipv6 地址
     *
     * @return ipv6 地址列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 3:02 PM
     */
    @NonNull
    @Override
    public List<String> getIpv6Address() {
        List<String> ipv6s = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface networkInterface = en.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    boolean isLoopbackAddress = inetAddress.isLoopbackAddress();
                    // 不带%的地址
                    boolean isLinkLocalAddress = inetAddress.isLinkLocalAddress();
                    boolean isInet6Address = inetAddress instanceof Inet6Address;
                    if (isLoopbackAddress || isLinkLocalAddress || !isInet6Address) {
                        continue;
                    }
                    Inet6Address addr = (Inet6Address) inetAddress;
                    String ipv6 = addr.getHostAddress();
                    ipv6s.add(ipv6);
                }
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return ipv6s;
    }

    /**
     * 添加网络状态变化回调(注意 context 泄漏问题，不用时需要移除监听)
     *
     * @param callback : 网络状态变化回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:10 PM
     */
    @Override
    public void addNetWorkStatusCallback(@NonNull CloudNetWorkCallback callback) {
        NetWorkReceiverListenerManager.getInstance().addNetWorkCallback(callback);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        UtilsBroadcastReceiver.getInstance().addIntentFilter(intentFilter, NetWorkReceiverListenerManager.getInstance());
    }

    /**
     * 移除网络状态变化回调
     *
     * @param callback : 网络状态变化回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/28 7:10 PM
     */
    @Override
    public void removeNetWorkStatusCallback(@NonNull CloudNetWorkCallback callback) {
        NetWorkReceiverListenerManager.getInstance().removeNetWorkCallback(callback);
    }
}
