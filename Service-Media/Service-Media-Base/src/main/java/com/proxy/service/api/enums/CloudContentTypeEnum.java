package com.proxy.service.api.enums;

import android.media.AudioAttributes;

/**
 * @author : cangHX
 * on 2021/06/01  9:38 PM
 */
public enum CloudContentTypeEnum {

    /**
     * 类型未知
     */
    CONTENT_TYPE_UNKNOWN(AudioAttributes.CONTENT_TYPE_UNKNOWN),
    /**
     * 语音
     */
    CONTENT_TYPE_SPEECH(AudioAttributes.CONTENT_TYPE_SPEECH),
    /**
     * 单纯音频，如：音乐
     */
    CONTENT_TYPE_MUSIC(AudioAttributes.CONTENT_TYPE_MUSIC),
    /**
     * 音频，如：视频中的音频
     **/
    CONTENT_TYPE_MOVIE(AudioAttributes.CONTENT_TYPE_MOVIE),
    /**
     * 交互音频，如：按键反馈、游戏奖励发放
     */
    CONTENT_TYPE_SONIFICATION(AudioAttributes.CONTENT_TYPE_SONIFICATION);

    private final int contentType;

    CloudContentTypeEnum(int contentType) {
        this.contentType = contentType;
    }

    public int getContentType(){
        return contentType;
    }

}
