package com.proxy.androidcloud.module_library.file;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.services.CloudUtilsPermissionService;
import com.proxy.service.api.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2021/04/11  3:07 PM
 */
public class FileHelper extends AbstractListHelper {

    private static final Logger M_LOGGER = Logger.create(FileHelper.class.getSimpleName());
    private final AtomicInteger integer = new AtomicInteger(0);
    private final CloudUtilsFileService service;
    private final CloudUtilsPermissionService permissionService;

    public FileHelper() {
        service = CloudSystem.getService(CloudUtilsFileService.class);
        permissionService = CloudSystem.getService(CloudUtilsPermissionService.class);
        if (permissionService == null) {
            return;
        }
        request();
    }

    private void request() {
        permissionService
                .create()
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                    for (String permission : permissions) {
                        M_LOGGER.error("获取权限 : " + permission);
                    }
                })
                .onDenied(permissions -> {
                    int count = integer.incrementAndGet();
                    //重复申请最多三次
                    if (count < 4) {
                        request();
                    }
                    for (String permission : permissions) {
                        M_LOGGER.error("拒绝权限 : " + permission);
                    }
                })
                .onRationale(permissions -> {
                    for (String permission : permissions) {
                        M_LOGGER.error("禁止弹出权限授权页面 : " + permission);
                    }
                })
                .request();
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
                        .setTitle("创建文件")
                        .build()
        );
        list.add(
                HelperItemInfo
                        .builder()
                        .setId(2)
                        .setTitle("删除文件")
                        .build()
        );
        list.add(
                HelperItemInfo
                        .builder()
                        .setId(3)
                        .setTitle("读取文件")
                        .build()
        );
        list.add(
                HelperItemInfo
                        .builder()
                        .setId(4)
                        .setTitle("写入文件")
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
        if (service == null || permissionService == null) {
            M_LOGGER.error("程序出错");
            return;
        }
        if (!permissionService.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(context, "没有权限", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (itemInfo.id) {
            case 1:
                service.createFile(new File(context.getExternalFilesDir("test"), "asd.txt").getAbsolutePath());
                M_LOGGER.error("创建完成");
                break;
            case 2:
                service.deleteFile(new File(context.getExternalFilesDir("test"), "asd.txt").getAbsolutePath());
                M_LOGGER.error("删除完成");
                break;
            case 3:
                String msg = service.read(new File(context.getExternalFilesDir("test"), "asd.txt"));
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
                }
                M_LOGGER.error("读取完成");
                break;
            case 4:
                String text = "ssss";
                service.write(new File(context.getExternalFilesDir("test"), "asd.txt"), text, false);
                M_LOGGER.error("写入完成，写入内容：" + text);
                break;
        }
    }
}
