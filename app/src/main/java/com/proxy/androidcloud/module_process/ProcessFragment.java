package com.proxy.androidcloud.module_process;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proxy.androidcloud.R;

/**
 * @author: cangHX
 * on 2020/07/06  12:16
 */
public class ProcessFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process,container,false);
        TextView textView = view.findViewById(R.id.text_view);
        textView.setText("ProcessFragment");
        return view;
    }
}
