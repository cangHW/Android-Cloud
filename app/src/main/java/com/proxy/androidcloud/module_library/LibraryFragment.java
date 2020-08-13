package com.proxy.androidcloud.module_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseFragment;
import com.proxy.androidcloud.detail.DetailActivity;
import com.proxy.androidcloud.helper.HelperType;
import com.proxy.androidcloud.listener.onViewClickListener;
import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/06  12:16
 */
public class LibraryFragment extends BaseFragment implements CloudUiLifeCallback, onViewClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mThreadPool:
                DetailActivity.launch(getContext(), HelperType.THREAD_POOL);
                break;
            default:
        }
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
        Logger.Info("LibraryFragment  :  onUiResume");
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
        Logger.Info("LibraryFragment  :  onUiStop");
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
        Logger.Info("LibraryFragment  :  onUiVisible");
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
        Logger.Info("LibraryFragment  :  onUiInVisible");
    }

    @Override
    public String tag() {
        return "library";
    }
}
