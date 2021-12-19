package com.proxy.service.media.info;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.SurfaceView;
import android.view.TextureView;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.callback.CloudMediaPlayerCallback;
import com.proxy.service.api.services.CloudMediaPlayerService;
import com.proxy.service.api.tag.CloudServiceTagMedia;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.media.callback.BindSurfaceCallback;
import com.proxy.service.media.callback.SurfaceStateCallback;
import com.proxy.service.media.surface.ISurface;
import com.proxy.service.media.surface.SurfaceViewImpl;
import com.proxy.service.media.surface.TextureViewImpl;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 视频播放器服务实现类
 *
 * @author : cangHX
 * on 2021/06/04  9:26 PM
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = CloudServiceTagMedia.MEDIA_PLAYER)
public class MediaPlayerServiceImpl implements CloudMediaPlayerService {

    private static final Logger LOGGER = Logger.create("MediaPlayerServiceImpl");

    public static final int DELAY_MILLIS = 30;
    private static final HandlerThread HANDLER_THREAD = new HandlerThread("MediaPlayerServiceImpl");

    static {
        HANDLER_THREAD.start();
    }

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static final Handler HANDLER = new Handler(HANDLER_THREAD.getLooper());

    private final AtomicBoolean isInit = new AtomicBoolean(false);
    private final AtomicBoolean isMediaPrepareReady = new AtomicBoolean(false);
    private final AtomicBoolean isSurfaceReady = new AtomicBoolean(false);

    public MediaPlayer mMediaPlayer;
    public CloudMediaPlayerCallback mMediaPlayerCallback;
    public ISurface iSurface;

    private volatile MediaRunnable mMediaRunnable;

    /**
     * 初始化视图
     *
     * @param surfaceView : 视频播放视图
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    @Override
    public void init(SurfaceView surfaceView) {
        init(surfaceView, false);
    }

    /**
     * 初始化视图
     *
     * @param surfaceView    : 视频播放视图
     * @param isSizeAdaptive : 宽高自适应
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    @Override
    public void init(SurfaceView surfaceView, boolean isSizeAdaptive) {
        init(surfaceView, isSizeAdaptive, isSizeAdaptive);
    }

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
    @Override
    public void init(SurfaceView surfaceView, boolean canChangeWidth, boolean canChangeHeight) {
        if (!isInit.compareAndSet(false, true)) {
            LOGGER.debug("MediaPlayerService 已经初始化过了");
            return;
        }
        LOGGER.debug("MediaPlayerService 开始初始化 SurfaceView");
        iSurface = new SurfaceViewImpl(surfaceView);
        iSurface.setSurfaceStateCallback(surfaceStateCallback);
        iSurface.setSizeCanChanged(canChangeWidth, canChangeHeight);
    }

    /**
     * 初始化视图
     *
     * @param textureView : 视频播放视图
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    @Override
    public void init(TextureView textureView) {
        init(textureView, false);
    }

    /**
     * 初始化视图
     *
     * @param textureView    : 视频播放视图
     * @param isSizeAdaptive : 宽高自适应
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/27 01:02
     */
    @Override
    public void init(TextureView textureView, boolean isSizeAdaptive) {
        init(textureView, isSizeAdaptive, isSizeAdaptive);
    }

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
    @Override
    public void init(TextureView textureView, boolean canChangeWidth, boolean canChangeHeight) {
        if (!isInit.compareAndSet(false, true)) {
            LOGGER.debug("MediaPlayerService 已经初始化过了");
            return;
        }
        LOGGER.debug("MediaPlayerService 开始初始化 TextureView");
        iSurface = new TextureViewImpl(textureView);
        iSurface.setSurfaceStateCallback(surfaceStateCallback);
        iSurface.setSizeCanChanged(canChangeWidth, canChangeHeight);
    }

    /**
     * 加载视频
     *
     * @param mediaPath : 视频地址
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:02
     */
    @Override
    public void load(String mediaPath) {
        if (isInit.get()) {
            LOGGER.debug("MediaPlayerService 还没有初始化");
            return;
        }
        release();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mediaPath);
            mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setOnErrorListener(onErrorListener);
            mMediaPlayer.setOnPreparedListener(onPreparedListener);
            mMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
            mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            mMediaPlayer.prepareAsync();
        } catch (Throwable throwable) {
            LOGGER.debug(throwable);
        }
    }

    /**
     * 获取当前播放进度
     *
     * @return: 当前播放进度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/28 16:35
     */
    @Override
    public int getCurrentTime() {
        if (!isReady()) {
            return -1;
        }
        try {
            return mMediaPlayer.getCurrentPosition();
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
        }
        return -1;
    }

    /**
     * 获取当前视频总时长
     *
     * @return: 当前视频总时长
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/28 16:35
     */
    @Override
    public int getTotalTime() {
        if (!isReady()) {
            return -1;
        }
        try {
            return mMediaPlayer.getDuration();
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
        }
        return -1;
    }

    /**
     * 获取视频宽度
     *
     * @return: 视频宽度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 18:58
     */
    @Override
    public int getVideoWidth() {
        if (!isReady()) {
            return -1;
        }
        try {
            return mMediaPlayer.getVideoWidth();
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
        }
        return -1;
    }

    /**
     * 获取视频高度
     *
     * @return: 视频高度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 18:58
     */
    @Override
    public int getVideoHeight() {
        if (!isReady()) {
            return -1;
        }
        try {
            return mMediaPlayer.getVideoHeight();
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
        }
        return -1;
    }

    /**
     * 开始播放，最好在{@link CloudMediaPlayerCallback#onMediaReady}回调中调用
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 22:49
     */
    @Override
    public void start() {
        if (!isReady()) {
            return;
        }
        if (isPlay()) {
            LOGGER.debug("已经处于播放中");
            return;
        }
        if (iSurface == null) {
            return;
        }
        iSurface.setVideoSize(getVideoWidth(), getVideoHeight());
        iSurface.bindSurface(mMediaPlayer, new BindSurfaceCallback() {
            @Override
            public void onBind() {
                resume();
            }
        });
    }

    /**
     * 快进
     *
     * @param msec : 准备快进到的位置
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/25 00:39
     */
    @Override
    public void seekTo(final int msec) {
        if (!isReady()) {
            return;
        }
        try {
            mMediaPlayer.seekTo(msec);
            safeRunOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayerCallback == null) {
                        return;
                    }
                    mMediaPlayerCallback.onMediaPlaying(msec, mMediaPlayer.getDuration());
                }
            });
        } catch (Throwable throwable) {
            LOGGER.error(throwable.getMessage());
        }
    }

    /**
     * 判断是否正在播放
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/25 00:39
     */
    @Override
    public boolean isPlay() {
        if (!isReady()) {
            return false;
        }
        try {
            return mMediaPlayer.isPlaying();
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
        }
        return false;
    }

    /**
     * 暂停播放
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:06
     */
    @Override
    public void pause() {
        if (!isReady()) {
            return;
        }
        if (!isPlay()) {
            LOGGER.debug("暂未播放");
            return;
        }
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (mMediaRunnable != null) {
                    HANDLER.removeCallbacks(mMediaRunnable);
                }
                try {
                    mMediaPlayer.pause();
                    safeRunOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mMediaPlayerCallback == null) {
                                return;
                            }
                            mMediaPlayerCallback.onMediaPause();
                        }
                    });
                } catch (Throwable throwable) {
                    LOGGER.error(throwable.getMessage());
                }
            }
        });
    }

    /**
     * 继续播放
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:14
     */
    @Override
    public void resume() {
        if (!isReady()) {
            return;
        }
        if (isPlay()) {
            LOGGER.debug("已经处于播放中");
            return;
        }
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                try {
                    final int progress = mMediaPlayer.getCurrentPosition() / 1000;
                    final int total = mMediaPlayer.getDuration() / 1000;
                    mMediaPlayer.start();
                    safeRunOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mMediaPlayerCallback == null) {
                                return;
                            }
                            if (progress == 0 || progress == total) {
                                mMediaPlayerCallback.onMediaStart();
                            } else {
                                mMediaPlayerCallback.onMediaResume();
                            }
                        }
                    });
                    mMediaRunnable = new MediaRunnable(MediaPlayerServiceImpl.this);
                    HANDLER.post(mMediaRunnable);
                } catch (Throwable throwable) {
                    LOGGER.error(throwable.getMessage());
                }
            }
        });
    }

    /**
     * 释放资源
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/11/24 00:07
     */
    @Override
    public void release() {
        mMediaPlayerCallback = null;
        releaseMedia();
        releaseSurface();
        mMediaRunnable = null;
    }

    /**
     * 设置播放回调
     *
     * @param callback : 回调接口
     * @author: cangHX
     * @date: 2021/11/24 00:07
     */
    @Override
    public void setMediaPlayerCallback(CloudMediaPlayerCallback callback) {
        this.mMediaPlayerCallback = callback;
    }

    /**
     * 释放播放器
     */
    private void releaseMedia() {
        if (mMediaRunnable != null) {
            HANDLER.removeCallbacks(mMediaRunnable);
        }
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        } catch (Throwable throwable) {
            LOGGER.debug(throwable.getMessage());
        } finally {
            isMediaPrepareReady.set(false);
            mMediaPlayer = null;
        }
    }

    /**
     * 释放视图
     */
    private void releaseSurface() {
        try {
            iSurface.release();
        } catch (Throwable throwable) {
            LOGGER.debug(throwable.getMessage());
        } finally {
            isInit.set(false);
            iSurface = null;
        }
    }

    /**
     * 播放器是否准备好
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isReady() {
        if (!isInit.get()) {
            LOGGER.debug("MediaPlayerService 还没有完成初始化");
            return false;
        }
        if (!isMediaPrepareReady.get()) {
            LOGGER.debug("MediaPlayerService 还没有加载完成视频");
            return false;
        }
        if (!isSurfaceReady.get()) {
            LOGGER.debug("MediaPlayerService 还没有准备好视图");
            return false;
        }
        return true;
    }

    private final MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, final int percent) {
            LOGGER.debug("视频缓冲中。。。 " + percent + "%");
            safeRunOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayerCallback != null) {
                        mMediaPlayerCallback.onMediaBufferingUpdate(percent);
                    }
                }
            });
        }
    };

    private final MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            LOGGER.debug("拖动成功 : " + mp.getCurrentPosition() + "$$ " + mp.getDuration());
        }
    };

    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(final MediaPlayer mp) {
            LOGGER.debug("视频播放完成");
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mMediaRunnable != null) {
                            HANDLER.removeCallbacks(mMediaRunnable);
                        }
                    } catch (Throwable throwable) {
                        LOGGER.debug(throwable.getMessage());
                    }
                    final int total = mp.getDuration();
                    safeRunOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mMediaPlayerCallback != null) {
                                mMediaPlayerCallback.onMediaPlaying(total, total);
                                mMediaPlayerCallback.onMediaCompletion();
                            }
                        }
                    });
                }
            });
        }
    };

    private final MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            LOGGER.debug("视频播放出错");
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mMediaRunnable != null) {
                            HANDLER.removeCallbacks(mMediaRunnable);
                        }
                    } catch (Throwable throwable) {
                        LOGGER.debug(throwable.getMessage());
                    }
                    safeRunOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mMediaPlayerCallback != null) {
                                mMediaPlayerCallback.onMediaError();
                            }
                        }
                    });
                }
            });
            return false;
        }
    };

    private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            LOGGER.debug("缓冲完成");
            try {
                isMediaPrepareReady.set(true);
                if (mMediaPlayerCallback != null) {
                    mMediaPlayerCallback.onMediaReady();
                }
            } catch (Throwable throwable) {
                LOGGER.debug(throwable);
            }
        }
    };

    private final MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            LOGGER.debug("获取视频宽度 : " + width + "，获取视频高度 : " + height);
            if (width == 0 || height == 0) {
                return;
            }
            if (iSurface != null) {
                iSurface.setVideoSize(width, height);
            }
        }
    };

    private final SurfaceStateCallback surfaceStateCallback = new SurfaceStateCallback() {
        /**
         * 视图创建
         *
         * @version: 1.0
         * @author: cangHX
         * @date: 2021/12/14 00:40
         */
        @Override
        public void onCreate() {
            isSurfaceReady.set(true);
        }

        /**
         * 视图销毁
         *
         * @version: 1.0
         * @author: cangHX
         * @date: 2021/12/14 00:40
         */
        @Override
        public void onDestroy() {
            isSurfaceReady.set(false);
        }
    };

    private void safeRunOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                LOGGER.debug(throwable.getMessage());
            }
        } else {
            MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    safeRunOnUiThread(runnable);
                }
            });
        }
    }

    private static class MediaRunnable implements Runnable {

        private final WeakReference<MediaPlayerServiceImpl> mediaPlayerServiceWeakReference;

        public MediaRunnable(MediaPlayerServiceImpl mediaPlayerService) {
            mediaPlayerServiceWeakReference = new WeakReference<>(mediaPlayerService);
        }

        @Override
        public void run() {
            WeakReferenceUtils.checkValueIsEmpty(mediaPlayerServiceWeakReference, new WeakReferenceUtils.Callback<MediaPlayerServiceImpl>() {
                @Override
                public void onCallback(WeakReference<MediaPlayerServiceImpl> weakReference, final MediaPlayerServiceImpl mediaPlayerService) {
                    final int progress = mediaPlayerService.getCurrentTime();
                    final int total = mediaPlayerService.getTotalTime();
                    try {
                        MediaPlayerServiceImpl.MAIN_HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayerService.mMediaPlayerCallback.onMediaPlaying(progress, total);
                            }
                        });
                        MediaPlayerServiceImpl.HANDLER.postDelayed(MediaRunnable.this, MediaPlayerServiceImpl.DELAY_MILLIS);
                    } catch (Throwable throwable) {
                        MediaPlayerServiceImpl.LOGGER.debug(throwable.getMessage());
                    }
                }
            });
        }
    }
}
