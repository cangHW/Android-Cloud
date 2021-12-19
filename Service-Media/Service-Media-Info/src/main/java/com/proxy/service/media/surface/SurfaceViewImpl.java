package com.proxy.service.media.surface;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.media.callback.BindSurfaceCallback;
import com.proxy.service.media.callback.SurfaceStateCallback;

import java.lang.ref.WeakReference;

/**
 * SurfaceView 适配器
 *
 * @author: cangHX
 * @date: 2021/12/12 21:32
 * @version: 1.0
 */
public class SurfaceViewImpl implements ISurface {

    private Logger mLogger;

    private WeakReference<SurfaceHolder> mSurfaceHolderWeakReference;
    private WeakReference<MediaPlayer> mMediaPlayerWeakReference;
    private WeakReference<SurfaceStateCallback> mStateCallbackWeakReference;

    private BindSurfaceCallback mBindSurfaceCallback;

    private boolean isCanChangeWidth;
    private boolean isCanChangeHeight;
    private int mVideoWidth = -1;
    private int mVideoHeight = -1;
    private int mSurfaceWidth = -1;
    private int mSurfaceHeight = -1;

    public SurfaceViewImpl(SurfaceView surfaceView) {
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mLogger.debug("视图 surface 创建");
                mSurfaceHolderWeakReference = new WeakReference<>(holder);
                WeakReferenceUtils.checkValueIsEmpty(mStateCallbackWeakReference, new WeakReferenceUtils.Callback<SurfaceStateCallback>() {
                    @Override
                    public void onCallback(WeakReference<SurfaceStateCallback> weakReference, SurfaceStateCallback surfaceStateCallback) {
                        surfaceStateCallback.onCreate();
                    }
                });
                setSurfaceToPlayer();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                mLogger.debug("视图 surface 尺寸变化");
                if (mSurfaceWidth > width && mSurfaceHeight > height) {
                    return;
                }
                mSurfaceWidth = width;
                mSurfaceHeight = height;
                changeViewSize();
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                mLogger.debug("视图 surface 销毁");
                WeakReferenceUtils.checkValueIsEmpty(mStateCallbackWeakReference, new WeakReferenceUtils.Callback<SurfaceStateCallback>() {
                    @Override
                    public void onCallback(WeakReference<SurfaceStateCallback> weakReference, SurfaceStateCallback surfaceStateCallback) {
                        surfaceStateCallback.onDestroy();
                    }
                });
                mSurfaceHolderWeakReference.clear();
                mSurfaceHolderWeakReference = null;
            }
        });
    }

    /**
     * 设置日志打印
     *
     * @param logger : 日志打印器
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 23:20
     */
    @Override
    public void setLog(Logger logger) {
        this.mLogger = logger;
    }

    /**
     * 设置对应尺寸是否可以修改
     *
     * @param canChangeWidth  : 宽度是否可以修改
     * @param canChangeHeight : 高度是否可以修改
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:38
     */
    @Override
    public void setSizeCanChanged(boolean canChangeWidth, boolean canChangeHeight) {
        isCanChangeWidth = canChangeWidth;
        isCanChangeHeight = canChangeHeight;
    }

    /**
     * 设置视图状态回调
     *
     * @param surfaceStateCallback : 视图状态回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:39
     */
    @Override
    public void setSurfaceStateCallback(SurfaceStateCallback surfaceStateCallback) {
        mStateCallbackWeakReference = new WeakReference<>(surfaceStateCallback);
    }

    /**
     * 设置视频宽高
     *
     * @param width  : 视频宽度
     * @param height : 视频高度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:40
     */
    @Override
    public void setVideoSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        changeViewSize();
    }

    /**
     * 绑定视图
     *
     * @param mediaPlayer         : 播放器对象
     * @param bindSurfaceCallback : 绑定视图回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 21:40
     */
    @Override
    public void bindSurface(final MediaPlayer mediaPlayer, BindSurfaceCallback bindSurfaceCallback) {
        this.mBindSurfaceCallback = bindSurfaceCallback;
        mMediaPlayerWeakReference = new WeakReference<>(mediaPlayer);
        setSurfaceToPlayer();
    }

    /**
     * 是否资源
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/12 22:37
     */
    @Override
    public void release() {
        if (mMediaPlayerWeakReference != null) {
            mMediaPlayerWeakReference.clear();
        }
        mMediaPlayerWeakReference = null;
        if (mSurfaceHolderWeakReference != null) {
            mSurfaceHolderWeakReference.clear();
        }
        mSurfaceHolderWeakReference = null;
        if (mStateCallbackWeakReference != null) {
            mStateCallbackWeakReference.clear();
        }
        mStateCallbackWeakReference = null;
        mLogger = null;
    }

    private void changeViewSize() {
        final int w;
        final int h;
        if (isCanChangeWidth && isCanChangeHeight) {
            w = mVideoWidth;
            h = mVideoHeight;
        } else if (isCanChangeWidth) {
            w = (int) (mVideoHeight * 1.0f / mSurfaceHeight * mSurfaceWidth);
            h = mVideoHeight;
        } else if (isCanChangeHeight) {
            w = mVideoWidth;
            h = (int) (mVideoWidth * 1.0f / mSurfaceWidth * mSurfaceHeight);
        } else {
            return;
        }
        WeakReferenceUtils.checkValueIsEmpty(mSurfaceHolderWeakReference, new WeakReferenceUtils.Callback<SurfaceHolder>() {
            @Override
            public void onCallback(WeakReference<SurfaceHolder> weakReference, SurfaceHolder surfaceHolder) {
                surfaceHolder.setFixedSize(w, h);
            }
        });
    }

    /**
     * 给播放器设置视图
     */
    private void setSurfaceToPlayer() {
        WeakReferenceUtils.checkValueIsEmpty(mSurfaceHolderWeakReference, new WeakReferenceUtils.Callback<SurfaceHolder>() {
            @Override
            public void onCallback(WeakReference<SurfaceHolder> weakReference, final SurfaceHolder surfaceHolder) {
                WeakReferenceUtils.checkValueIsEmpty(mMediaPlayerWeakReference, new WeakReferenceUtils.Callback<MediaPlayer>() {
                    @Override
                    public void onCallback(WeakReference<MediaPlayer> weakReference, MediaPlayer mediaPlayer) {
                        try {
                            if (isSetSurface.compareAndSet(false, true)) {
                                mediaPlayer.setDisplay(surfaceHolder);
                            }
                            if (mBindSurfaceCallback != null) {
                                mBindSurfaceCallback.onBind();
                            }
                            mBindSurfaceCallback = null;
                        } catch (Throwable throwable) {
                            mLogger.error(throwable);
                        }
                    }
                });
            }
        });
    }
}
