package com.proxy.service.ui.uitabhost.cache;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/12  21:12
 */
public enum ViewCache {

    /**
     * 单例
     */
    INSTANCE;

    public interface ObtainViewListener {
        /**
         * 获取当前接口对应的 tag
         *
         * @return 获取到的 tag
         * @version: 1.0
         * @author: cangHX
         * @date: 2020-07-12 21:31
         */
        @NonNull
        String getTag();

        /**
         * 通过 index 获取对应的view
         *
         * @param index : 位置
         * @return 对应的view
         * @version: 1.0
         * @author: cangHX
         * @date: 2020-07-12 21:32
         */
        View obtain(int index);
    }

    private final List<WeakReference<ObtainViewListener>> mListeners = new ArrayList<>();

    public synchronized void addObtainViewListener(@NonNull ObtainViewListener listener) {
        mListeners.add(new WeakReference<>(listener));
    }

    @Nullable
    public synchronized View getView(@NonNull String tag, int index) {
        List<WeakReference<ObtainViewListener>> list = new ArrayList<>(mListeners);

        for (WeakReference<ObtainViewListener> weakReference : list) {
            if (weakReference == null) {
                continue;
            }

            ObtainViewListener listener = weakReference.get();
            if (listener == null) {
                this.mListeners.remove(weakReference);
                continue;
            }

            String t = listener.getTag();
            if (!t.equals(tag)) {
                continue;
            }

            return listener.obtain(index);
        }
        return null;
    }

}
