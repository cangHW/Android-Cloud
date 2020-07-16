package com.proxy.service.ui.uitabhost.helper.content.viewpager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.cache.ViewCache;

/**
 * @author: cangHX
 * on 2020/07/12  18:54
 */
public class PlaceHolderFragment extends Fragment implements CloudUiLifeCallback {

    public static final String TAG = "place_holder_key";
    public static final String INDEX = "place_holder_index";

    private CloudUiLifeCallback mUiLifeCallback;
    private String mTag = "";
    private int mIndex = -1;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args == null) {
            return;
        }
        mTag = args.getString(TAG, "");
        mIndex = args.getInt(INDEX, -1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = ViewCache.INSTANCE.getView(mTag, mIndex);
        if (view == null) {
            view = new View(inflater.getContext());
        }
        if (view instanceof CloudUiLifeCallback) {
            mUiLifeCallback = (CloudUiLifeCallback) view;
        }
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
        if (mUiLifeCallback != null) {
            mUiLifeCallback.onUiResume();
        }
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
        if (mUiLifeCallback != null) {
            mUiLifeCallback.onUiStop();
        }
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
        if (mUiLifeCallback != null) {
            mUiLifeCallback.onUiVisible();
        }
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
        if (mUiLifeCallback != null) {
            mUiLifeCallback.onUiInVisible();
        }
    }
}
