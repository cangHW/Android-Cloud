package com.proxy.service.api.enums;

import android.media.AudioManager;

/**
 * @author : cangHX
 * on 2021/06/01  10:43 AM
 */
public enum CloudStreamTypeEnum {

    /**
     * 通话音量
     */
    STREAM_VOICE_CALL(AudioManager.STREAM_VOICE_CALL),

    /**
     * 系统音量
     */
    STREAM_SYSTEM(AudioManager.STREAM_SYSTEM),

    /**
     * 铃声音量
     */
    STREAM_RING(AudioManager.STREAM_RING),

    /**
     * 音乐音量
     */
    STREAM_MUSIC(AudioManager.STREAM_MUSIC),

    /**
     * 闹钟音量
     */
    STREAM_ALARM(AudioManager.STREAM_ALARM),

    /**
     * 通知音量
     */
    STREAM_NOTIFICATION(AudioManager.STREAM_NOTIFICATION);

    final int type;

    CloudStreamTypeEnum(int type) {
        this.type = type;
    }

    public int getType(){
        return type;
    }

}
