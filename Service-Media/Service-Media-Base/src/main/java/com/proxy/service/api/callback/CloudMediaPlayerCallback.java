package com.proxy.service.api.callback;

/**
 * @author: cangHX
 * @date: 2021/12/12 18:59
 * @version: 1.0
 * @desc:
 */
public interface CloudMediaPlayerCallback {
    /**
     * 缓冲中
     *
     * @param percent : 缓冲百分比（1 - 100）
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 23:52
     */
    void onMediaBufferingUpdate(int percent);

    /**
     * 资源准备完成，可以开始播放
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 20:28
     */
    void onMediaReady();

    /**
     * 播放开始回调
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/28 00:03
     */
    void onMediaStart();

    /**
     * 播放进度回调
     *
     * @param progress : 播放进度，单位：毫秒
     * @param total    : 视频总长度，单位：毫秒
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:20
     */
    void onMediaPlaying(long progress, long total);

    /**
     * 播放暂停回调
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:20
     */
    void onMediaPause();

    /**
     * 播放恢复回调
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/28 00:03
     */
    void onMediaResume();

    /**
     * 播放完成
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 23:50
     */
    void onMediaCompletion();

    /**
     * 播放出错
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 23:50
     */
    void onMediaError();
}
