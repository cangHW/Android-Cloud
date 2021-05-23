package com.proxy.service.utils.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proxy.service.api.lifecycle.FragmentLifecycleState;
import com.proxy.service.api.utils.Logger;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author : cangHX
 * on 2021/05/20  9:35 PM
 */
public class LifecycleFragment extends Fragment {

    private Fragment mParentFragment;
    private final HashSet<LifecycleFragmentListener> LIFECYCLE_MAPPER = new HashSet<>();

    public void addLifecycleFragmentListener(Fragment fragment, LifecycleFragmentListener listener) {
        this.mParentFragment = fragment;
        try {
            LIFECYCLE_MAPPER.add(listener);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_ATTACH);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_START);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_CREATE);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_CREATE_VIEW);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_VIEW_CREATE);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_RESUME);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                if (hidden) {
                    lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_HIDE);
                } else {
                    lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_SHOW);
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_PAUSE);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_STOP);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_DESTROY);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_DESTROY_VIEW);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (LifecycleFragmentListener lifecycleFragmentListener : new ArrayList<>(LIFECYCLE_MAPPER)) {
            try {
                lifecycleFragmentListener.onLifecycleChanged(mParentFragment, FragmentLifecycleState.LIFECYCLE_DETACH);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

}
