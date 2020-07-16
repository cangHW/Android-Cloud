package com.proxy.androidcloud.module_network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseFragment;
import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/06  10:21
 */
public class NetWorkFragment extends BaseFragment implements CloudUiLifeCallback {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_work,container,false);
        return view;
    }

    /**
     * onResume
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:33 PM
     */
    @Override
    public void onUiResume() {
        Logger.Info("NetWorkFragment  :  onUiResume");
    }

    /**
     * onStop
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:34 PM
     */
    @Override
    public void onUiStop() {
        Logger.Info("NetWorkFragment  :  onUiStop");
    }

    /**
     * 显示
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:34 PM
     */
    @Override
    public void onUiVisible() {
        Logger.Info("NetWorkFragment  :  onUiVisible");
    }

    /**
     * 隐藏
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:34 PM
     */
    @Override
    public void onUiInVisible() {
        Logger.Info("NetWorkFragment  :  onUiInVisible");
    }
}
