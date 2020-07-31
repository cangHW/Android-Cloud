package com.proxy.service.utils.info;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.interfaces.ITask;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;

/**
 * @author : cangHX
 * on 2020/07/30  6:21 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_TASK)
public class UtilsTaskServiceImpl implements CloudUtilsTaskService {

    private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    private static final HandlerThread BG_THREAD = new HandlerThread("Cloud_Background_Thread");

    static {
        BG_THREAD.start();
    }

    private static final Handler BG_HANDLER = new Handler(BG_THREAD.getLooper());

    /**
     * 当前线程是否是主线程
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    @Override
    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 发起一个 ui 线程任务
     *
     * @param runnable : 任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    @Override
    public void postUiTask(@NonNull Runnable runnable) {
        UI_HANDLER.post(runnable);
    }

    /**
     * 移除一个 ui 线程任务，如果存在
     *
     * @param runnable : 准备移除的任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    @Override
    public void removeUiTask(@NonNull Runnable runnable) {
        UI_HANDLER.removeCallbacks(runnable);
    }

    /**
     * 发起一个 ui 线程延迟任务
     *
     * @param runnable    : 任务体
     * @param delayMillis : 延迟时间，单位：毫秒
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    @Override
    public void postUiTaskDelayed(@NonNull Runnable runnable, long delayMillis) {
        UI_HANDLER.postDelayed(runnable, delayMillis);
    }

    /**
     * 发起一个后台线程任务
     *
     * @param runnable : 任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:18 PM
     */
    @Override
    public void postBgTask(@NonNull Runnable runnable) {
        BG_HANDLER.post(runnable);
    }

    /**
     * 移除一个后台线程任务，如果存在
     *
     * @param runnable : 准备移除的任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:18 PM
     */
    @Override
    public void removeBgTask(@NonNull Runnable runnable) {
        BG_HANDLER.removeCallbacks(runnable);
    }

    /**
     * 发起一个后台线程延迟任务
     *
     * @param runnable    : 任务体
     * @param delayMillis : 延迟时间，单位：毫秒
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    @Override
    public void postBgTaskDelayed(@NonNull Runnable runnable, long delayMillis) {
        BG_HANDLER.postDelayed(runnable, delayMillis);
    }

    /**
     * 创建一个 ITask 对象，用于完成更复杂的线程逻辑
     *
     * @return ITask 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:18 PM
     */
    @NonNull
    @Override
    public ITask builder() {
        //todo 线程池
        return null;
    }
}
