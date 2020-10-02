package com.proxy.androidcloud.module_library.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.receiver.CloudReceiverInfo;
import com.proxy.service.api.receiver.CloudReceiverListener;
import com.proxy.service.api.services.CloudUtilsReceiverService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/09/30  10:42 PM
 */
public class ReceiverListHelper extends AbstractListHelper implements CloudReceiverListener {

    private static final String ACTION_CLICK = "click";
    private static final String PERMISSION = "com.proxy.androidcloud.receiverListHelper";

    private static final Logger M_LOGGER = Logger.create(ReceiverListHelper.class.getSimpleName());

    private static final String DATA = "data";

    CloudUtilsReceiverService receiverService;

    public ReceiverListHelper() {
        receiverService = CloudSystem.getService(CloudServiceTagUtils.UTILS_RECEIVER);
    }

    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> infos = new ArrayList<>();
        infos.add(
                HelperItemInfo
                        .builder()
                        .setId(0)
                        .setTitle("注册全局广播")
                        .build()
        );
        infos.add(
                HelperItemInfo
                        .builder()
                        .setId(1)
                        .setTitle("注册带权限全局广播")
                        .build()
        );
        infos.add(
                HelperItemInfo
                        .builder()
                        .setId(2)
                        .setTitle("注册本地广播")
                        .build()
        );
        infos.add(
                HelperItemInfo
                        .builder()
                        .setId(3)
                        .setTitle("移除广播")
                        .build()
        );
        infos.add(
                HelperItemInfo
                        .builder()
                        .setId(4)
                        .setTitle("发送全局广播")
                        .build()
        );
        infos.add(
                HelperItemInfo
                        .builder()
                        .setId(5)
                        .setTitle("发送带权限全局广播")
                        .build()
        );
        infos.add(
                HelperItemInfo
                        .builder()
                        .setId(6)
                        .setTitle("发送本地广播")
                        .build()
        );
        return infos;
    }

    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (receiverService == null) {
            return;
        }
        switch (itemInfo.id) {
            case 0:
                receiverService.addReceiverListener(
                        CloudReceiverInfo
                                .builder()
                                .setAction(ACTION_CLICK)
                                .build(),
                        this);
                break;
            case 1:
                receiverService.addReceiverListener(
                        PERMISSION,
                        CloudReceiverInfo
                                .builder()
                                .setAction(ACTION_CLICK)
                                .build(),
                        this);
                break;
            case 2:
                receiverService.addLocalReceiverListener(
                        CloudReceiverInfo
                                .builder()
                                .setAction(ACTION_CLICK)
                                .build(),
                        this);
                break;
            case 3:
                receiverService.removeReceiverListener(this);
                break;
            case 4:
                Intent all = new Intent(ACTION_CLICK);
                all.putExtra(DATA, "全局广播通知");
                receiverService.sendBroadcast(all);
                break;
            case 5:
                Intent permission = new Intent(ACTION_CLICK);
                permission.putExtra(DATA, "带权限全局广播通知");
                receiverService.sendBroadcast(PERMISSION, permission);
                break;
            case 6:
                Intent local = new Intent(ACTION_CLICK);
                local.putExtra(DATA, "本地广播通知");
                receiverService.sendLocalBroadcast(local);
                break;
            default:
        }
    }

    /**
     * 接收到消息
     *
     * @param context : 上下文环境
     * @param intent  : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/09/29  11:38 PM
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra(DATA);
        M_LOGGER.error("onReceive data : " + data);
        if (!TextUtils.isEmpty(data)) {
            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
        }
    }
}
