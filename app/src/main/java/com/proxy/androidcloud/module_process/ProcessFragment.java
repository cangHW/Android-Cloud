package com.proxy.androidcloud.module_process;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseFragment;
import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/06  12:16
 */
public class ProcessFragment extends BaseFragment implements CloudUiLifeCallback {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);
        TextView textView = view.findViewById(R.id.text_view);
        textView.setText("ProcessFragment");
        return view;
    }

    /**
     * onResume
     * 不会同时回调 onUiVisible
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:33 PM
     */
    @Override
    public void onUiResume() {
        Logger.Info("ProcessFragment  :  onUiResume");
    }

    /**
     * onStop
     * 不会同时回调 onUiInVisible
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:34 PM
     */
    @Override
    public void onUiStop() {
        Logger.Info("ProcessFragment  :  onUiStop");
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
        Logger.Info("ProcessFragment  :  onUiVisible");
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
        Logger.Info("ProcessFragment  :  onUiInVisible");
    }
}
