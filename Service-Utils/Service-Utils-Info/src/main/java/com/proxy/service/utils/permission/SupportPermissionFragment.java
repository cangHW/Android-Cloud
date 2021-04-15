package com.proxy.service.utils.permission;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author : cangHX
 * on 2021/04/11  3:18 PM
 */
@SuppressWarnings("ALL")
public class SupportPermissionFragment extends Fragment implements IPermissionFragment {

    private final ArrayList<PermissionInfo> INFOS = new ArrayList<>();
    private final HashMap<Integer, PermissionInfo> PERMISSION_MAPPER = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * 添加准备申请的权限信息
     *
     * @param callback    : 权限回调
     * @param permissions : 权限信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 3:34 PM
     */
    @Override
    public void addPermissionInfo(PermissionCallback callback, List<String> permissions) {
        PermissionInfo info = new PermissionInfo();
        info.callback = callback;
        info.deniedPermissions = new ArrayList<>(permissions.size());
        info.grantedPermissions = new ArrayList<>(permissions.size());
        info.rationalePermissions = new ArrayList<>(permissions.size());
        for (String permission : permissions) {
            if (SERVICE.isPermissionGranted(permission)) {
                info.grantedPermissions.add(permission);
            } else {
                info.deniedPermissions.add(permission);
            }
        }
        INFOS.add(info);
    }

    /**
     * 开始申请权限
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 3:35 PM
     */
    @Override
    public void request() {
        for (PermissionInfo info : new ArrayList<>(INFOS)) {
            int requestCode = REQUEST_CODE.incrementAndGet();
            PERMISSION_MAPPER.put(requestCode, info);
            int size = info.deniedPermissions.size();
            requestPermissions(info.deniedPermissions.toArray(new String[size]), requestCode);
            INFOS.remove(info);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults == null || grantResults.length == 0) {
            return;
        }
        PermissionInfo info = PERMISSION_MAPPER.get(requestCode);
        if (info == null) {
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            String permission = permissions[i];
            info.grantedPermissions.add(permission);
            info.deniedPermissions.remove(permission);
        }
        for (String permission : info.deniedPermissions) {
            boolean flag = shouldShowRequestPermissionRationale(permission);
            if (flag) {
                continue;
            }
            info.rationalePermissions.add(permission);
            info.deniedPermissions.remove(permission);
        }
        int deniedSize = info.deniedPermissions.size();
        if (deniedSize > 0) {
            info.callback.onDenied(info.deniedPermissions.toArray(new String[deniedSize]));
        }
        int grantedSize = info.grantedPermissions.size();
        if (grantedSize > 0) {
            info.callback.onGranted(info.grantedPermissions.toArray(new String[grantedSize]));
        }
        int rationaleSize = info.rationalePermissions.size();
        if (rationaleSize > 0) {
            info.callback.onRationale(info.rationalePermissions.toArray(new String[rationaleSize]));
        }
    }
}
