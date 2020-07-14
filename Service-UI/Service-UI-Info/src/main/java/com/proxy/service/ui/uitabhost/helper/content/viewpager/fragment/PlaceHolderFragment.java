package com.proxy.service.ui.uitabhost.helper.content.viewpager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proxy.service.ui.uitabhost.helper.content.viewpager.cache.ViewCache;

/**
 * @author: cangHX
 * on 2020/07/12  18:54
 */
public class PlaceHolderFragment extends Fragment {

    public static final String TAG = "place_holder_key";
    public static final String INDEX = "place_holder_index";

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
        if (view==null){
            view = new View(inflater.getContext());
        }
        return view;
    }
}
