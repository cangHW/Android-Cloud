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
import com.proxy.androidcloud.detail.ListActivity;
import com.proxy.androidcloud.helper.DetailHelperType;
import com.proxy.androidcloud.helper.ListHelperType;
import com.proxy.androidcloud.listener.onViewClickListener;
import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/06  10:16 PM
 */
public class LibraryFragment extends BaseFragment implements CloudUiLifeCallback, onViewClickListener {

    private static final Logger logger = Logger.create(LibraryFragment.class.getName());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.mThreadPool) {
            ListActivity.launch(getContext(), ListHelperType.THREAD_POOL);
        } else if (id == R.id.mBitmap) {
            ListActivity.launch(getContext(), ListHelperType.BITMAP);
        } else if (id == R.id.mInstall) {
            DetailActivity.launch(getContext(), DetailHelperType.INSTALL);
        } else if (id == R.id.mReceiver) {
            ListActivity.launch(getContext(), ListHelperType.RECEIVER);
        } else if (id == R.id.mApp) {
            ListActivity.launch(getContext(), ListHelperType.APP);
        } else if (id == R.id.mFile) {
            ListActivity.launch(getContext(), ListHelperType.FILE);
        } else if (id == R.id.mNet_Work) {
            ListActivity.launch(getContext(), ListHelperType.NET_WORK);
        } else if (id == R.id.mShare) {
            ListActivity.launch(getContext(), ListHelperType.SHARE);
        } else if (id == R.id.mLifecycle) {
            ListActivity.launch(getContext(), ListHelperType.LIFECYCLE);
        } else if (id == R.id.mEvent) {
            ListActivity.launch(getContext(), ListHelperType.EVENT);
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
