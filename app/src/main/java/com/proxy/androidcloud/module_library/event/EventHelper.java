package com.proxy.androidcloud.module_library.event;

import android.content.Context;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.event.CloudMainThreadEventCallback;
import com.proxy.service.api.services.CloudUtilsEventService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author : cangHX
 * on 2021/05/25  8:04 PM
 */
public class EventHelper extends AbstractListHelper implements CloudMainThreadEventCallback {

    private final CloudUtilsEventService service;
    private Context context;

    public EventHelper() {
        service = CloudSystem.getService(CloudUtilsEventService.class);
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
                        .setId(1)
                        .setTitle("注册回调")
                        .build()
        );

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(2)
                        .setTitle("移除回调")
                        .build()
        );

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(3)
                        .setTitle("分发事件")
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
        if (service == null) {
            return;
        }
        if (this.context == null) {
            this.context = context;
        }
        switch (itemInfo.id) {
            case 1:
                service.bind(this);
                break;
            case 2:
                service.remove(this);
                break;
            case 3:
                service.postOrSet("123");
                break;
        }
    }

    /**
     * 接收消息
     *
     * @param object : 透传自定义消息
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 7:51 PM
     */
    @Override
    public void onMainEvent(Object object) {
        Toast.makeText(this.context, (String) object, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置接收消息的类型
     *
     * @return 接收消息的类型集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 7:51 PM
     */
    @Override
    public Set<Class<?>> getMainEventTypes() {
        return Collections.singleton(String.class);
    }
}
