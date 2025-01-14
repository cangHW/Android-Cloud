package com.proxy.service.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.proxy.service.api.action.Action;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.permission.IPermissionManager;
import com.proxy.service.api.log.Logger;

import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2021/04/11  6:03 PM
 */
public class PermissionManagerImpl implements IPermissionManager {

    private static final String TAG = "CloudPermissionFragment";

    private final ArrayList<String> PERMISSIONS = new ArrayList<>();
    private Action<String[]> mGrantedAction;
    private Action<String[]> mDeniedAction;
    private Action<String[]> mRationaleAction;

    /**
     * 添加要申请的权限
     *
     * @param permission : 要申请的权限
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:05 PM
     */
    @Override
    public IPermissionManager addPermission(String permission) {
        if (TextUtils.isEmpty(permission)) {
            Logger.Error(CloudApiError.DATA_EMPTY.setMsg("permission is not be null").build());
            return this;
        }
        PERMISSIONS.add(permission);
        return this;
    }

    /**
     * 设置允许权限回调
     *
     * @param action : 回调对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:10 PM
     */
    @Override
    public IPermissionManager onGranted(Action<String[]> action) {
        this.mGrantedAction = action;
        return this;
    }

    /**
     * 设置拒绝权限回调
     *
     * @param action : 回调对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:10 PM
     */
    @Override
    public IPermissionManager onDenied(Action<String[]> action) {
        this.mDeniedAction = action;
        return this;
    }

    /**
     * 设置拒绝并不再提示权限回调
     *
     * @param action : 回调对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:10 PM
     */
    @Override
    public IPermissionManager onRationale(Action<String[]> action) {
        this.mRationaleAction = action;
        return this;
    }

    /**
     * 开始请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:12 PM
     */
    @Override
    public void request() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        Activity activity = ContextManager.getCurrentActivity();
        if (activity == null) {
            Logger.Error(CloudApiError.NO_RUNNING_ACTIVITY.build());
            return;
        }
        if (PERMISSIONS.size() == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("Please add permissions first.").build());
            return;
        }
        try {
            IPermissionFragment iPermissionFragment = getIPermissionFragment(activity);
            iPermissionFragment.addPermissionInfo(CALLBACK, PERMISSIONS);
            iPermissionFragment.request();
        } catch (Throwable throwable) {
            Logger.Error(throwable);
        }
    }

    private final PermissionCallback CALLBACK = new PermissionCallback() {
        @Override
        public void onGranted(String[] permissions) {
            if (mGrantedAction != null) {
                try {
                    mGrantedAction.onAction(permissions);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
            }
        }

        @Override
        public void onDenied(String[] permissions) {
            if (mDeniedAction != null) {
                try {
                    mDeniedAction.onAction(permissions);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
            }
        }

        @Override
        public void onRationale(String[] permissions) {
            if (mRationaleAction != null) {
                try {
                    mRationaleAction.onAction(permissions);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
            }
        }
    };

    private IPermissionFragment getIPermissionFragment(Activity context) {
        IPermissionFragment iPermissionFragment;

        FragmentActivity fragmentActivity = (FragmentActivity) context;
        FragmentManager manager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        if (fragment instanceof IPermissionFragment) {
            return (IPermissionFragment) fragment;
        }
        fragment = new PermissionFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(fragment, TAG);
        transaction.commitNowAllowingStateLoss();
        manager.executePendingTransactions();
        iPermissionFragment = (IPermissionFragment) fragment;

        return iPermissionFragment;
    }
}
