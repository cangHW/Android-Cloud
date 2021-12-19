package com.proxy.service.media.surface;

import android.media.MediaPlayer;

import com.proxy.service.api.utils.Logger;
import com.proxy.service.media.callback.BindSurfaceCallback;
import com.proxy.service.media.callback.SurfaceStateCallback;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 视频播放器视图适配器
 *
 * @author: cangHX
 * @date: 2021/12/12 21:20
 * @version: 1.0
 * @desc:
 */
public interface ISurface {

    AtomicBoolean isSetSurface = new AtomicBoolean(false);

    /**
     * 设置日志打印
     *
     * @param logger : 日志打印器
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 23:20
     */
    void setLog(Logger logger);

    /**
     * 设置对应尺寸是否可以修改
     *
     * @param canChangeWidth  : 宽度是否可以修改
     * @param canChangeHeight : 高度是否可以修改
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:38
     */
    void setSizeCanChanged(boolean canChangeWidth, boolean canChangeHeight);

    /**
     * 设置视图状态回调
     *
     * @param surfaceStateCallback : 视图状态回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:39
     */
    void setSurfaceStateCallback(SurfaceStateCallback surfaceStateCallback);

    /**
     * 设置视频宽高
     *
     * @param width  : 视频宽度
     * @param height : 视频高度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:40
     */
    void setVideoSize(int width, int height);

    /**
     * 绑定视图
     *
     * @param mediaPlayer         : 播放器对象
     * @param bindSurfaceCallback : 绑定视图回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:40
     */
    void bindSurface(MediaPlayer mediaPlayer, BindSurfaceCallback bindSurfaceCallback);

    /**
     * 是否资源
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 22:37
     */
    void release();
}
