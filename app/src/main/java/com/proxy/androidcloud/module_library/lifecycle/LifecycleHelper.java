package com.proxy.androidcloud.module_library.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.lifecycle.CloudActivityLifecycleListener;
import com.proxy.service.api.services.CloudUtilsLifecycleService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2021/05/25  8:42 PM
 */
public class LifecycleHelper extends AbstractListHelper implements CloudActivityLifecycleListener {

    private final CloudUtilsLifecycleService service;

    public LifecycleHelper() {
        service = CloudSystem.getService(CloudUtilsLifecycleService.class);
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
                        .setTitle("绑定 Activity 生命周期")
                        .build()
        );

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(2)
                        .setTitle("解绑 Activity 生命周期")
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
        switch (itemInfo.id) {
            case 1:
                if (context instanceof Activity) {
                    service.bind((Activity) context, this
                            , LifecycleState.LIFECYCLE_RESUME
                            , LifecycleState.LIFECYCLE_PAUSE
                    );
                } else {
                    Toast.makeText(context, "context 错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                service.remove(this);
                break;
        }
    }

    /**
     * 回调生命周期
     *
     * @param activity       : 准备观察生命周期的对象
     * @param lifecycleState : 生命周期状态回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/15  2:11 PM
     */
    @Override
    public void onLifecycleChanged(Activity activity, LifecycleState lifecycleState) {
        Toast.makeText(activity, lifecycleState.state(), Toast.LENGTH_SHORT).show();
    }
}
