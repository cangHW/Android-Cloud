package com.proxy.service.api.services;

import android.view.SurfaceView;
import android.view.TextureView;

import com.proxy.service.api.callback.CloudMediaPlayerCallback;
import com.proxy.service.base.BaseService;

/**
 * 音视频播放相关
 *
 * @author : cangHX
 * on 2021/06/04  9:25 PM
 */
public interface CloudMediaPlayerService extends BaseService {

    /**
     * 初始化视图
     *
     * @param surfaceView : 视频播放视图
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    void init(SurfaceView surfaceView);

    /**
     * 初始化视图
     *
     * @param surfaceView    : 视频播放视图
     * @param isSizeAdaptive : 宽高自适应
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    void init(SurfaceView surfaceView, boolean isSizeAdaptive);

    /**
     * 初始化视图
     *
     * @param surfaceView     : 视频播放视图
     * @param canChangeWidth  : 是否可以修改宽度
     * @param canChangeHeight : 是否可以修改高度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    void init(SurfaceView surfaceView, boolean canChangeWidth, boolean canChangeHeight);

    /**
     * 初始化视图
     *
     * @param textureView : 视频播放视图
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    void init(TextureView textureView);

    /**
     * 初始化视图
     *
     * @param textureView    : 视频播放视图
     * @param isSizeAdaptive : 宽高自适应
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    void init(TextureView textureView, boolean isSizeAdaptive);

    /**
     * 初始化视图
     *
     * @param textureView     : 视频播放视图
     * @param canChangeWidth  : 是否可以修改宽度
     * @param canChangeHeight : 是否可以修改高度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    void init(TextureView textureView, boolean canChangeWidth, boolean canChangeHeight);

    /**
     * 加载视频
     *
     * @param mediaPath : 视频地址
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:02
     */
    void load(String mediaPath);

    /**
     * 获取当前播放进度
     *
     * @return: 当前播放进度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/28 16:35
     */
    int getCurrentTime();

    /**
     * 获取当前视频总时长
     *
     * @return: 当前视频总时长
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/28 16:35
     */
    int getTotalTime();

    /**
     * 获取视频宽度
     *
     * @return: 视频宽度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 18:58
     */
    int getVideoWidth();

    /**
     * 获取视频高度
     *
     * @return: 视频高度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 18:58
     */
    int getVideoHeight();

    /**
     * 开始播放，最好在{@link CloudMediaPlayerCallback#onMediaReady}回调中调用
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 22:49
     */
    void start();

    /**
     * 快进
     *
     * @param msec : 准备快进到的位置
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/25 00:39
     */
    void seekTo(int msec);

    /**
     * 判断是否正在播放
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/25 00:39
     */
    boolean isPlay();

    /**
     * 暂停播放
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:06
     */
    void pause();

    /**
     * 继续播放
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:14
     */
    void resume();

    /**
     * 释放资源
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:07
     */
    void release();

    /**
     * 设置播放回调
     *
     * @param callback : 回调接口
     * @author: cangHX
     * @date: 2021/11/24 00:07
     */
    void setMediaPlayerCallback(CloudMediaPlayerCallback callback);
}
