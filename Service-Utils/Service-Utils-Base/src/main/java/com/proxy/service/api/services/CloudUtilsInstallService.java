package com.proxy.service.api.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.proxy.service.api.install.CloudInstallCallback;
import com.proxy.service.api.install.CloudInstallStatusEnum;
import com.proxy.service.api.install.CloudAppInfo;
import com.proxy.service.base.BaseService;

import java.util.List;

/**
 * 安装相关
 *
 * @author: cangHX
 * on 2020/06/11  09:54
 */
public interface CloudUtilsInstallService extends BaseService {

    /**
     * 添加安装状态回调
     *
     * @param cloudInstallCallback : 安装状态回调接口(保存方式：弱引用，需要注意回收问题)
     * @param statusEnums          : 准备接收的状态类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 17:06
     */
    void addInstallCallback(@NonNull CloudInstallCallback cloudInstallCallback, @Nullable CloudInstallStatusEnum... statusEnums);

    /**
     * 对应包名的app是否安装
     *
     * @param packageName : 包名
     * @return 是否安装，true 已安装，false 未安装
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:02
     */
    boolean isInstallApp(@NonNull String packageName);

    /**
     * 添加允许通过 provider 共享的文件路径，用于获取资源 Uri 等
     * 如果不设置，默认所有路径都是安全路径，建议设置
     *
     * @param filePath : 允许共享的安全路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 13:30
     */
    void addProviderResourcePath(@NonNull String filePath);

    /**
     * 安装应用
     *
     * @param apkPath : 安装包路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:55
     */
    void installApp(@NonNull String apkPath);

    /**
     * 获取对应apk的包名
     *
     * @param apkPath : 安装包路径
     * @return apk的包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 15:11
     */
    String getPackageName(@NonNull String apkPath);

    /**
     * 卸载应用
     *
     * @param packageName : 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:56
     */
    void unInstallApp(@NonNull String packageName);

    /**
     * 获取所有已安装应用
     *
     * @return 获取到的已安装应用
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:06
     */
    @NonNull
    @WorkerThread
    List<CloudAppInfo> getAllInstallAppsInfo();

    /**
     * 打开对应包名的app
     *
     * @param packageName : 包名
     * @return 是否打开成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 13:49
     */
    boolean openApp(@NonNull String packageName);
}
