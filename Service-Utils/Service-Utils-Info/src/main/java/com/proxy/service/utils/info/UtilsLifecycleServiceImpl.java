package com.proxy.service.utils.info;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.context.lifecycle.LifecycleBean;
import com.proxy.service.api.context.listener.CloudLifecycleListener;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.lifecycle.CloudActivityLifecycleListener;
import com.proxy.service.api.lifecycle.CloudFragmentLifecycleListener;
import com.proxy.service.api.lifecycle.FragmentLifecycleState;
import com.proxy.service.api.services.CloudUtilsLifecycleService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.utils.lifecycle.FragmentLifecycleBean;
import com.proxy.service.utils.lifecycle.LifecycleFragment;
import com.proxy.service.utils.lifecycle.LifecycleFragmentListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author : cangHX
 * on 2021/05/20  9:31 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_LIFECYCLE)
public class UtilsLifecycleServiceImpl implements CloudUtilsLifecycleService, CloudLifecycleListener, LifecycleFragmentListener {

    private static final String TAG = UtilsLifecycleServiceImpl.class.getName();
    private static final WeakHashMap<Activity, LifecycleBean> ACTIVITY_MAPPER = new WeakHashMap<>();
    private static final WeakHashMap<Fragment, FragmentLifecycleBean> FRAGMENT_MAPPER = new WeakHashMap<>();

    /**
     * 绑定 Activity 的生命周期
     *
     * @param activity          : 上下文
     * @param lifecycleListener : 生命周期回调
     * @param lifecycleState    : 声明监听的生命周期
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    @Override
    public void bind(Activity activity, CloudActivityLifecycleListener lifecycleListener, LifecycleState... lifecycleState) {
        if (activity == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("The activity cannot be empty, in the method bind. CloudUtilsLifecycleService").build());
            return;
        }
        if (lifecycleState == null || lifecycleState.length == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("The lifecycleState cannot be empty, in the method bind. CloudUtilsLifecycleService").build());
            return;
        }
        LifecycleBean lifecycleBean = ACTIVITY_MAPPER.get(activity);
        if (lifecycleBean == null) {
            lifecycleBean = new LifecycleBean();
            ACTIVITY_MAPPER.put(activity, lifecycleBean);
        }
        for (LifecycleState state : lifecycleState) {
            if (state == null) {
                continue;
            }
            lifecycleBean.setLifecycleListener(state, lifecycleListener);
        }
        ContextManager.addLifecycleListener(activity, UtilsLifecycleServiceImpl.this
                , LifecycleState.LIFECYCLE_CREATE
                , LifecycleState.LIFECYCLE_START
                , LifecycleState.LIFECYCLE_RESUME
                , LifecycleState.LIFECYCLE_PAUSE
                , LifecycleState.LIFECYCLE_STOP
                , LifecycleState.LIFECYCLE_DESTROY
        );
    }

    /**
     * 绑定 Fragment 的生命周期
     *
     * @param fragment          : 上下文
     * @param lifecycleListener : 生命周期回调
     * @param lifecycleState    : 声明监听的生命周期
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    @Override
    public void bind(Fragment fragment, CloudFragmentLifecycleListener lifecycleListener, FragmentLifecycleState... lifecycleState) {
        if (fragment == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("The fragment cannot be empty, in the method bind. CloudUtilsLifecycleService").build());
            return;
        }
        if (lifecycleState == null || lifecycleState.length == 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("The FragmentLifecycleState cannot be empty, in the method bind. CloudUtilsLifecycleService").build());
            return;
        }
        FragmentLifecycleBean lifecycleBean = FRAGMENT_MAPPER.get(fragment);
        if (lifecycleBean == null) {
            lifecycleBean = new FragmentLifecycleBean();
            FRAGMENT_MAPPER.put(fragment, lifecycleBean);
        }
        for (FragmentLifecycleState state : lifecycleState) {
            if (state == null) {
                continue;
            }
            lifecycleBean.setLifecycleListener(state, lifecycleListener);
        }

        try {
            FragmentManager fragmentManager = fragment.getChildFragmentManager();
            LifecycleFragment lifecycleFragment = (LifecycleFragment) fragmentManager.findFragmentByTag(TAG);
            if (lifecycleFragment != null) {
                lifecycleFragment.addLifecycleFragmentListener(fragment, this);
                return;
            }
            lifecycleFragment = new LifecycleFragment();
            lifecycleFragment.addLifecycleFragmentListener(fragment, this);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(lifecycleFragment, TAG);
            transaction.commitNowAllowingStateLoss();
        } catch (Throwable throwable) {
            Logger.Error(throwable);
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
    public void onLifecycleChanged(final Activity activity, final LifecycleState lifecycleState) {
        LifecycleBean lifecycleBean = ACTIVITY_MAPPER.get(activity);
        if (lifecycleBean == null) {
            return;
        }
        List<WeakReference<CloudLifecycleListener>> list = lifecycleBean.getLifecycleListener(lifecycleState);
        WeakReferenceUtils.checkValueIsEmpty(list, new WeakReferenceUtils.Callback<CloudLifecycleListener>() {
            @Override
            public void onCallback(WeakReference<CloudLifecycleListener> weakReference, CloudLifecycleListener lifecycleListener) {
                lifecycleListener.onLifecycleChanged(activity, lifecycleState);
            }
        });
        if (lifecycleState == LifecycleState.LIFECYCLE_DESTROY) {
            ACTIVITY_MAPPER.remove(activity);
        }
    }

    /**
     * 回调生命周期
     *
     * @param fragment       : 准备观察生命周期的对象
     * @param lifecycleState : 生命周期状态回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/05/20  9:56 PM
     */
    @Override
    public void onLifecycleChanged(final Fragment fragment, final FragmentLifecycleState lifecycleState) {
        FragmentLifecycleBean lifecycleBean = FRAGMENT_MAPPER.get(fragment);
        if (lifecycleBean == null) {
            return;
        }
        List<WeakReference<CloudFragmentLifecycleListener>> list = lifecycleBean.getLifecycleListener(lifecycleState);
        WeakReferenceUtils.checkValueIsEmpty(list, new WeakReferenceUtils.Callback<CloudFragmentLifecycleListener>() {
            @Override
            public void onCallback(WeakReference<CloudFragmentLifecycleListener> weakReference, CloudFragmentLifecycleListener lifecycleListener) {
                lifecycleListener.onLifecycleChanged(fragment, lifecycleState);
            }
        });
        if (lifecycleState == FragmentLifecycleState.LIFECYCLE_DESTROY) {
            FRAGMENT_MAPPER.remove(fragment);
        }
    }
}
