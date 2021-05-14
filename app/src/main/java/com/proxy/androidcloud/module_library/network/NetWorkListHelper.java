package com.proxy.androidcloud.module_library.network;

import android.content.Context;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.net.CloudNetWorkCallback;
import com.proxy.service.api.net.CloudNetWorkType;
import com.proxy.service.api.services.CloudUtilsNetWorkService;
import com.proxy.service.api.tag.CloudServiceTagUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2021/05/10  11:00 PM
 */
public class NetWorkListHelper extends AbstractListHelper implements CloudNetWorkCallback {

    private final CloudUtilsNetWorkService mNetWorkService;
    private Context mContext;

    public NetWorkListHelper() {
        mNetWorkService = CloudSystem.getService(CloudServiceTagUtils.UTILS_NET_WORK);
    }

    /**
     * 创建 item 信息
     *
     * @return item 信息集合
     */
    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> list = new ArrayList<>();

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(0)
                        .setTitle("是否有网络")
                        .build()
        );
        list.add(
                HelperItemInfo
                        .builder()
                        .setId(1)
                        .setTitle("网络状态监听")
                        .build()
        );
        list.add(
                HelperItemInfo
                        .builder()
                        .setId(2)
                        .setTitle("关闭网络状态监听")
                        .build()
        );

        return list;
    }

    /**
     * item 点击
     *
     * @param context  : 上下文
     * @param itemInfo : item 信息
     * @param button   : button位置
     *                 1、{@link HelperItemInfo#BUTTON_TITLE},
     *                 2、{@link HelperItemInfo#BUTTON_CENTER},
     *                 3、{@link HelperItemInfo#BUTTON_LEFT},
     *                 4、{@link HelperItemInfo#BUTTON_RIGHT}
     */
    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (mNetWorkService == null) {
            return;
        }
        this.mContext = context;
        switch (itemInfo.id) {
            case 0:
                if (mNetWorkService.isConnected()) {
                    Toast.makeText(context, "有网络", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                mNetWorkService.addNetWorkStatusCallback(this);
                break;
            case 2:
                mNetWorkService.removeNetWorkStatusCallback(this);
                break;
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
        switch (type) {
            case WIFI:
                Toast.makeText(mContext, "WI-FI 已连接", Toast.LENGTH_SHORT).show();
                break;
            case MOBILE:
            case MOBILE_2G:
            case MOBILE_3G:
            case MOBILE_4G:
            case MOBILE_5G:
                Toast.makeText(mContext, "蜂窝移动已连接", Toast.LENGTH_SHORT).show();
                break;
            case UNKNOWN:
                Toast.makeText(mContext, "未知网络已连接", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                break;
        }
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
        Toast.makeText(mContext, "网络已断开", Toast.LENGTH_SHORT).show();
    }
}
