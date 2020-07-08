package com.proxy.androidcloud.module_network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseFragment;

/**
 * @author: cangHX
 * on 2020/07/06  10:21
 */
public class NetWorkFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_work,container,false);
        return view;
    }
}
