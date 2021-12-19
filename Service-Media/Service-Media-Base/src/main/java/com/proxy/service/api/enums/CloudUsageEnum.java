package com.proxy.service.api.enums;

import android.media.AudioAttributes;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * @author : cangHX
 * on 2021/06/01  9:26 PM
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public enum CloudUsageEnum {

    /**
     * 未知用法
     */
    USAGE_UNKNOWN(AudioAttributes.USAGE_UNKNOWN),
    /**
     * 媒体播放
     */
    USAGE_MEDIA(AudioAttributes.USAGE_MEDIA),
    /**
     * 通话
     */
    USAGE_VOICE_COMMUNICATION(AudioAttributes.USAGE_VOICE_COMMUNICATION),
    /**
     * 呼叫铃声
     */
    USAGE_VOICE_COMMUNICATION_SIGNALLING(AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING),
    /**
     * 闹钟
     */
    USAGE_ALARM(AudioAttributes.USAGE_ALARM),
    /**
     * 通知
     */
    USAGE_NOTIFICATION(AudioAttributes.USAGE_NOTIFICATION),
    /**
     * 电话铃声
     */
    USAGE_NOTIFICATION_RINGTONE(AudioAttributes.USAGE_NOTIFICATION_RINGTONE),
    /**
     * 用于输入/结束通信请求，如VoIP通信、视频会议等。
     */
    USAGE_NOTIFICATION_COMMUNICATION_REQUEST(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST),
    /**
     * 即时通信的通知(如聊天或短信)
     */
    USAGE_NOTIFICATION_COMMUNICATION_INSTANT(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT),
    /**
     * 非即时通信的通知(如电子邮件)
     */
    USAGE_NOTIFICATION_COMMUNICATION_DELAYED(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED),
    /**
     * 用于吸引用户的注意，如提示或低电量警告。
     */
    USAGE_NOTIFICATION_EVENT(AudioAttributes.USAGE_NOTIFICATION_EVENT),
    /**
     * 用于可访问性时(如使用屏幕阅读器)
     */
    USAGE_ASSISTANCE_ACCESSIBILITY(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY),
    /**
     * 驾驶或导航
     */
    USAGE_ASSISTANCE_NAVIGATION_GUIDANCE(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE),
    /**
     * 声音化时(如用户界面声音)
     */
    USAGE_ASSISTANCE_SONIFICATION(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION),
    /**
     * 游戏音频
     */
    USAGE_GAME(AudioAttributes.USAGE_GAME),
    /**
     * 用于音频回应用户查询，音频说明或帮助话语。
     */
    USAGE_ASSISTANT(AudioAttributes.USAGE_ASSISTANT);

    private final int usage;

    CloudUsageEnum(int usage) {
        this.usage = usage;
    }

    public int getUsage() {
        return usage;
    }
}
