package com.proxy.service.media.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.bean.SoundInfo;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.enums.CloudStreamTypeEnum;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudMediaSoundService;
import com.proxy.service.api.tag.CloudServiceTagMedia;
import com.proxy.service.api.log.ListUtils;
import com.proxy.service.api.log.Logger;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : cangHX
 * on 2021/06/01  8:25 PM
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = CloudServiceTagMedia.MEDIA_SOUND)
public class MediaSoundServiceImpl implements CloudMediaSoundService, ListUtils.ListComparator<SoundInfo, String> {

    private static final String TAG = "MediaSoundServiceImpl";
    private static final int MAX_STREAMS = 32;
    private static final int MAX_SOUND_SIZE = 100 * 1024;
    private static final int MAX_LOAD_NUM = 255;

    private static final HashMap<String, SoundPool> SOUND_POOL_MAPPER = new HashMap<>();
    private static final HashMap<String, List<SoundInfo>> SOUND_INFO_MAPPER = new HashMap<>();

    private final AtomicBoolean isInit = new AtomicBoolean(false);
    private String mTag = TAG;
    private SoundPool mSoundPool;
    private List<SoundInfo> mSoundInfoList;

    /**
     * 通过 tag 进行初始化 CloudMediaSoundService,
     * 如果成功, 则继承并更新上一次的设置,
     * 如果失败, 则需要通过{@link CloudMediaSoundService#initialize(int, CloudStreamTypeEnum, Builder, String)}进行初始化, 成功后会绑定到当前 tag.
     *
     * @param tag : 标示, 用于绑定设置
     * @return true 成功, false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:21 PM
     */
    @Override
    public boolean initializeByTag(@NonNull String tag) {
        if (!isInit.compareAndSet(false, true)) {
            Logger.Debug(CloudApiError.INIT_ONCE.setAbout("CloudMediaSoundService initialize()").build());
            return false;
        }
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        mTag = tag;
        SoundPool soundPool = SOUND_POOL_MAPPER.get(mTag);

        if (soundPool == null) {
            isInit.set(false);
            return false;
        }
        mSoundPool = soundPool;

        List<SoundInfo> list = SOUND_INFO_MAPPER.get(mTag);
        if (list == null) {
            list = new ArrayList<>();
            SOUND_INFO_MAPPER.put(mTag, list);
        }

        mSoundInfoList = list;
        return true;
    }

    /**
     * 初始化一个播放池
     *
     * @param maxStreams : 最大声音数量
     * @param streamType : 音量类型
     * @param builder    : 额外设置
     * @param tag        : 标示, 用于绑定设置
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:00 PM
     */
    @SuppressLint("WrongConstant")
    @Override
    public void initialize(int maxStreams, @Nullable CloudStreamTypeEnum streamType, @Nullable Builder builder, @Nullable String tag) {
        if (!isInit.compareAndSet(false, true)) {
            Logger.Debug(CloudApiError.INIT_ONCE.setAbout("CloudMediaSoundService initialize()").build());
            return;
        }

        mTag = tag;

        if (maxStreams <= 0) {
            maxStreams = 1;
        } else if (maxStreams > MAX_STREAMS) {
            maxStreams = MAX_STREAMS;
        }

        if (streamType == null) {
            streamType = CloudStreamTypeEnum.STREAM_SYSTEM;
        }
        if (builder == null) {
            builder = new Builder();
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mSoundPool = new SoundPool(maxStreams, streamType.getType(), 0);
            } else {
                AudioAttributes.Builder attributes = new AudioAttributes.Builder();
                attributes.setLegacyStreamType(streamType.getType());
//              attributes.setFlags()
//              attributes.setHapticChannelsMuted()
                if (builder.getContentTypeEnum() != null) {
                    attributes.setContentType(builder.getContentTypeEnum().getContentType());
                }
                if (builder.getUsageEnum() != null) {
                    attributes.setUsage(builder.getUsageEnum().getUsage());
                }

                SoundPool.Builder soundPool = new SoundPool.Builder();
                soundPool.setMaxStreams(maxStreams);
                soundPool.setAudioAttributes(attributes.build());
                mSoundPool = soundPool.build();
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }

        if (mSoundPool == null) {
            isInit.set(false);
            return;
        }

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                List<SoundInfo> list = SOUND_INFO_MAPPER.get(mTag);
                SoundInfo soundInfo = ListUtils.getValueWhen(list, sampleId, new ListUtils.ListComparator<SoundInfo, Integer>() {
                    @Override
                    public boolean comparator(SoundInfo soundInfo, Integer integer) {
                        if (soundInfo == null) {
                            return false;
                        }
                        return soundInfo.soundId == integer;
                    }
                });
                if (soundInfo == null) {
                    Logger.Error(CloudApiError.DATA_EMPTY.setAbout("SoundPool load error. sampleId : " + sampleId).build());
                    return;
                }
                soundInfo.isReady = true;
            }
        });

        SOUND_POOL_MAPPER.put(mTag, mSoundPool);
        List<SoundInfo> list = SOUND_INFO_MAPPER.get(mTag);
        if (list == null) {
            list = new ArrayList<>();
            SOUND_INFO_MAPPER.put(mTag, list);
        }
        mSoundInfoList = list;
    }

    /**
     * 加载音频(最多加载 255 个音频), 音频最大 100KB
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param path     : 音频地址
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:12 PM
     */
    @Override
    public void load(@NonNull String soundTag, @NonNull String path) {
        if (TextUtils.isEmpty(soundTag)) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The soundTag cannot be empty.").build());
            return;
        }

        if (TextUtils.isEmpty(path)) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The path cannot be empty.").build());
            return;
        }

        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        if (ListUtils.getValueWhen(mSoundInfoList, soundTag, this) != null) {
            Logger.Error(CloudApiError.DATA_DUPLICATION.setAbout("CloudMediaSoundService.load(). with soundTag = " + soundTag).build());
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The path file cannot be null.").build());
            return;
        }

        if (file.length() <= 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The file length cannot be 0.").build());
            return;
        }

        if (file.length() > MAX_SOUND_SIZE) {
            Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("CloudMediaSoundService.load(). File size cannot exceed " + MAX_SOUND_SIZE).build());
            return;
        }

        if (mSoundInfoList.size() >= MAX_LOAD_NUM) {
            Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("Load up to " + MAX_LOAD_NUM).build());
            return;
        }

        SoundInfo soundInfo = new SoundInfo();
        soundInfo.soundTag = soundTag;
        soundInfo.soundId = mSoundPool.load(path, 1);
        mSoundInfoList.add(soundInfo);
    }

    /**
     * 加载音频(最多加载 255 个音频), 音频最大 100KB
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param resId    : 音频资源 ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:15 PM
     */
    @Override
    public void load(@NonNull String soundTag, @RawRes int resId) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        if (TextUtils.isEmpty(soundTag)) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The soundTag cannot be empty.").build());
            return;
        }

        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        if (ListUtils.getValueWhen(mSoundInfoList, soundTag, this) != null) {
            Logger.Error(CloudApiError.DATA_DUPLICATION.setAbout("CloudMediaSoundService.load(). with soundTag = " + soundTag).build());
            return;
        }

        InputStream stream = null;
        try {
            stream = context.getResources().openRawResource(resId);
            if (stream.available() <= 0) {
                Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The resId file length cannot be 0.").build());
                return;
            }

            if (stream.available() > MAX_SOUND_SIZE) {
                Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("CloudMediaSoundService.load(). The resId file size cannot exceed " + MAX_SOUND_SIZE).build());
                return;
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
            return;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }

        if (mSoundInfoList.size() >= MAX_LOAD_NUM) {
            Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("Load up to " + MAX_LOAD_NUM).build());
            return;
        }

        SoundInfo soundInfo = new SoundInfo();
        soundInfo.soundTag = soundTag;
        soundInfo.soundId = mSoundPool.load(context, resId, 1);
        mSoundInfoList.add(soundInfo);
    }

    /**
     * 加载音频(最多加载 255 个音频), 音频最大 100KB
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param afd      : 资源文件描述
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:29 PM
     */
    @Override
    public void load(@NonNull String soundTag, @NonNull AssetFileDescriptor afd) {
        if (TextUtils.isEmpty(soundTag)) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The soundTag cannot be empty.").build());
            return;
        }

        if (afd.getLength() <= 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The AssetFileDescriptor length cannot be 0.").build());
            return;
        }

        if ((afd.getLength() - afd.getStartOffset()) > MAX_SOUND_SIZE) {
            Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("CloudMediaSoundService.load(). The file size cannot exceed " + MAX_SOUND_SIZE).build());
            return;
        }

        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        if (ListUtils.getValueWhen(mSoundInfoList, soundTag, this) != null) {
            Logger.Error(CloudApiError.DATA_DUPLICATION.setAbout("CloudMediaSoundService.load(). with soundTag = " + soundTag).build());
            return;
        }

        if (mSoundInfoList.size() >= MAX_LOAD_NUM) {
            Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("Load up to " + MAX_LOAD_NUM).build());
            return;
        }

        SoundInfo soundInfo = new SoundInfo();
        soundInfo.soundTag = soundTag;
        soundInfo.soundId = mSoundPool.load(afd, 1);
        mSoundInfoList.add(soundInfo);
    }

    /**
     * 加载音频(最多加载 255 个音频), 音频最大 100KB
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param fd       : 资源文件描述
     * @param offset   : 声音开始的偏移值
     * @param length   : 声音的长度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:29 PM
     */
    @Override
    public void load(@NonNull String soundTag, @NonNull FileDescriptor fd, long offset, long length) {

        if (TextUtils.isEmpty(soundTag)) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The soundTag cannot be empty.").build());
            return;
        }

        if (length <= 0) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("CloudMediaSoundService.load(). The length cannot be 0.").build());
            return;
        }

        if ((length - offset) > MAX_SOUND_SIZE) {
            Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("CloudMediaSoundService.load(). The size cannot exceed " + MAX_SOUND_SIZE).build());
            return;
        }

        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        if (ListUtils.getValueWhen(mSoundInfoList, soundTag, this) != null) {
            Logger.Error(CloudApiError.DATA_DUPLICATION.setAbout("CloudMediaSoundService.load(). with soundTag = " + soundTag).build());
            return;
        }

        if (mSoundInfoList.size() >= MAX_LOAD_NUM) {
            Logger.Error(CloudApiError.DATA_TO_MORE.setAbout("Load up to " + MAX_LOAD_NUM).build());
            return;
        }

        SoundInfo soundInfo = new SoundInfo();
        soundInfo.soundTag = soundTag;
        soundInfo.soundId = mSoundPool.load(fd, offset, length, 1);
        mSoundInfoList.add(soundInfo);
    }

    /**
     * 播放音频（多次播放会生成不同播放 ID）
     *
     * @param soundTag : 音频唯一标示
     * @return 播放 id(大于0), 可用于暂停本次播放等操作(-1 代表播放失败)
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/7/23 9:16 PM
     */
    @Override
    public int play(@NonNull String soundTag) {
        return play(soundTag, 1.0f, 1.0f, 0, 1);
    }

    /**
     * 播放音频（多次播放会生成不同播放 ID）
     *
     * @param soundTag    : 音频唯一标示
     * @param leftVolume  : 左声道音量(0.0 - 1.0)
     * @param rightVolume : 右声道音量(0.0 - 1.0)
     * @param loop        : 循环播放次数(0 不循环, -1 无限循环)
     * @param rate        : 播放速率(0.5 - 2.0)
     * @return 播放 id(大于0), 可用于暂停本次播放等操作(-1 代表播放失败)
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:12 PM
     */
    @Override
    public int play(@NonNull String soundTag, float leftVolume, float rightVolume, int loop, float rate) {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return -1;
        }

        SoundInfo soundInfo = ListUtils.getValueWhen(mSoundInfoList, soundTag, this);
        if (soundInfo == null) {
            return -1;
        }

        if (!soundInfo.isReady) {
            Logger.Debug(CloudApiError.DATA_ERROR.setAbout("The " + soundTag + " is not ready").build());
            return -1;
        }

        return mSoundPool.play(soundInfo.soundId, leftVolume, rightVolume, 0, loop, rate);
    }

    /**
     * 暂停播放
     *
     * @param playId : 播放 id, 播放时返回的 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    @Override
    public void pause(int playId) {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        try {
            mSoundPool.pause(playId);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    /**
     * 暂停所有正在播放的音频
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    @Override
    public void pauseAll() {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        try {
            mSoundPool.autoPause();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    /**
     * 继续播放
     *
     * @param playId : 播放 id, 播放时返回的 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    @Override
    public void resume(int playId) {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        try {
            mSoundPool.resume(playId);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    /**
     * 继续播放所有暂停的音频
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    @Override
    public void resumeAll() {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        try {
            mSoundPool.autoResume();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    /**
     * 退出播放
     *
     * @param playId : 播放 id, 播放时返回的 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    @Override
    public void stop(int playId) {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }

        try {
            mSoundPool.stop(playId);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    /**
     * 卸载音频（不刷新计数，即仅仅清除内存消耗，不会刷新 255 限制）
     *
     * @param soundTag : 音频唯一标示
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/4 9:51 PM
     */
    @Override
    public void unload(@NonNull String soundTag) {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }
        for (int i = 0; i < mSoundInfoList.size(); i++) {
            SoundInfo soundInfo = mSoundInfoList.get(i);
            if (soundInfo.soundTag.equals(soundTag)) {
                try {
                    mSoundInfoList.set(i, null);
                    mSoundPool.unload(soundInfo.soundId);
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
                return;
            }
        }
    }

    /**
     * 清除全部资源消耗（刷新 255 限制）
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/4 9:51 PM
     */
    @Override
    public void release() {
        if (!isInit.get() && !initializeByTag(mTag)) {
            Logger.Error(CloudApiError.INIT_EMPTY.setMsg("Do you initialize CloudMediaSoundService ?").build());
            return;
        }
        try {
            mSoundInfoList.clear();
            mSoundPool.release();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        try {
            mSoundInfoList = null;
            mSoundPool = null;
            SOUND_POOL_MAPPER.remove(mTag);
            SOUND_INFO_MAPPER.remove(mTag);
            isInit.set(false);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    @Override
    public boolean comparator(SoundInfo soundInfo, String s) {
        if (soundInfo == null) {
            return false;
        }
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        return s.equals(soundInfo.soundTag);
    }
}
