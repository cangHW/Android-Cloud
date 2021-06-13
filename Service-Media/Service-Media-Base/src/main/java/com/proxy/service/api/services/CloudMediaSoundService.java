package com.proxy.service.api.services;

import android.content.res.AssetFileDescriptor;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.proxy.service.api.enums.CloudContentTypeEnum;
import com.proxy.service.api.enums.CloudUsageEnum;
import com.proxy.service.api.enums.CloudStreamTypeEnum;
import com.proxy.service.base.BaseService;

import java.io.FileDescriptor;

/**
 * 短音频播放相关
 *
 * @author : cangHX
 * on 2021/06/01  8:22 PM
 */
public interface CloudMediaSoundService extends BaseService {

    class Builder {
        private CloudUsageEnum usageEnum;
        private CloudContentTypeEnum contentTypeEnum;

        public CloudUsageEnum getUsageEnum() {
            return usageEnum;
        }

        public void setUsageEnum(CloudUsageEnum usageEnum) {
            this.usageEnum = usageEnum;
        }

        public CloudContentTypeEnum getContentTypeEnum() {
            return contentTypeEnum;
        }

        public void setContentTypeEnum(CloudContentTypeEnum contentTypeEnum) {
            this.contentTypeEnum = contentTypeEnum;
        }
    }

    /**
     * 通过 tag 进行初始化 CloudMediaSoundService,
     * 如果成功, 则继承并更新上一次的设置,
     * 如果失败, 则需要通过{@link CloudMediaSoundService#initialize(int, CloudStreamTypeEnum, Builder)}进行初始化, 成功后会绑定到当前 tag.
     *
     * @param tag : 标示, 用于绑定设置
     * @return true 成功, false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:21 PM
     */
    boolean initializeByTag(@NonNull String tag);

    /**
     * 初始化一个播放池, 并绑定 Tag
     *
     * @param maxStreams : 最大声音数量(1 - 32)
     * @param streamType : 声音类型
     * @param builder    : 额外设置
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:00 PM
     */
    void initialize(@IntRange(from = 1, to = 32) int maxStreams, @NonNull CloudStreamTypeEnum streamType, @Nullable Builder builder);

    /**
     * 加载音频(最多加载 255 个音频)
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、暂停等.
     * @param path     : 音频地址
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:12 PM
     */
    void load(@NonNull String soundTag, @NonNull String path);

    /**
     * 加载音频(最多加载 255 个音频)
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、暂停等.
     * @param resId    : 音频资源 ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:15 PM
     */
    void load(@NonNull String soundTag, @RawRes int resId);

    /**
     * 加载音频(最多加载 255 个音频)
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、暂停等.
     * @param afd      : 资源文件描述
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:29 PM
     */
    void load(@NonNull String soundTag, @NonNull AssetFileDescriptor afd);

    /**
     * 加载音频(最多加载 255 个音频)
     *
     * @param soundTag : 音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param fd       : 资源文件描述
     * @param offset   : 声音开始的偏移值
     * @param length   : 声音的长度
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/1 9:29 PM
     */
    void load(@NonNull String soundTag, @NonNull FileDescriptor fd, long offset, long length);

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
    int play(@NonNull String soundTag,
             @FloatRange(from = 0.0, to = 1.0) float leftVolume,
             @FloatRange(from = 0.0, to = 1.0) float rightVolume,
             int loop,
             @FloatRange(from = 0.5, to = 2.0) float rate);

    /**
     * 暂停正在播放的音频
     *
     * @param playId : 播放 id, 播放时返回的 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    void pause(int playId);

    /**
     * 暂停所有正在播放的音频
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    void pauseAll();

    /**
     * 继续播放暂停的音频
     *
     * @param playId : 播放 id, 播放时返回的 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    void resume(int playId);

    /**
     * 继续播放所有暂停的音频
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    void resumeAll();

    /**
     * 退出播放
     *
     * @param playId : 播放 id, 播放时返回的 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 9:51 PM
     */
    void stop(int playId);

    /**
     * 卸载音频（不刷新计数，即仅仅清除内存消耗，不会刷新 255 限制）
     *
     * @param soundTag : 音频唯一标示
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/4 9:51 PM
     */
    void unload(@NonNull String soundTag);

    /**
     * 清除全部资源消耗（刷新 255 限制）
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/4 9:51 PM
     */
    void release();
}
