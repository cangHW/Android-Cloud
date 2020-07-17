package com.proxy.androidcloud.module_process;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.view.AlphaChangedTextView;
import com.proxy.service.annotations.CloudUiTabHostReward;
import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.base.CloudUiTabHostFragmentReward;
import com.proxy.service.api.interfaces.IRewardHelper;

/**
 * @author: cangHX
 * on 2020/07/06  13:26
 */
@CloudUiTabHostReward(rewardTag = "main")
public class ProcessFragmentRewardImpl extends CloudUiTabHostFragmentReward {

    private boolean isSelect = false;
    private AlphaChangedTextView mTextView;

    /**
     * 获取显示内容
     *
     * @param context : 上下文环境
     * @return 显示内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    @Override
    public Fragment getContent(Context context) {
        return new ProcessFragment();
    }

    /**
     * 获取显示tab
     *
     * @param context : 上下文环境
     * @return 显示tab
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    @Override
    public View getTab(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_process, null, false);
        mTextView = view.findViewById(R.id.text_view);
        return view;
    }

    @Override
    public void onSelectProgress(float progress) {
        mTextView.setAlpha(progress);
    }

    @Override
    public void onSelectProgressEnd() {
        if (isSelect) {
            mTextView.setAlpha(1);
        } else {
            mTextView.setAlpha(0);
        }
    }

    @Override
    public void onUnSelect(String from) {
        isSelect = false;
    }

    @Override
    public void onSelect(String from) {
        switch (from) {
            case TabHostRewardSelectFrom.FROM_TAB:
                if (isSelect) {
                    mRewardHelper.set(IRewardHelper.Set.UI_EVENT, getIndex(), "你要双击刷新么？");
                } else {
                    mRewardHelper.set(IRewardHelper.Set.UI_EVENT, getIndex(), "通过按钮选中了进程模块");
                }
                break;
            case TabHostRewardSelectFrom.FROM_CONTENT:
                mRewardHelper.set(IRewardHelper.Set.UI_EVENT, getIndex(), "通过滑动选中了进程模块");
                break;
            case TabHostRewardSelectFrom.FROM_HELPER:
                mRewardHelper.set(IRewardHelper.Set.UI_EVENT, getIndex(), "通过设置选中了进程模块");
                break;
            default:
        }
        isSelect = true;
    }

    /**
     * 获取展示位置
     *
     * @return 展示位置
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:53
     */
    @Override
    public int getIndex() {
        return 1;
    }
}
