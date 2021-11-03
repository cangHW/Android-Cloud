package com.proxy.androidcloud.module_library.media;

import android.content.Context;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.enums.CloudContentTypeEnum;
import com.proxy.service.api.enums.CloudStreamTypeEnum;
import com.proxy.service.api.services.CloudMediaSoundService;
import com.proxy.service.api.tag.CloudServiceTagMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2021/08/04  8:14 PM
 */
public class SoundHelper extends AbstractListHelper {

    private static final String TAG = SoundHelper.class.getSimpleName();

    private static final String SoundTag = "click";

    private final CloudMediaSoundService mSoundService;

    public SoundHelper() {
        mSoundService = CloudSystem.getService(CloudServiceTagMedia.MEDIA_SOUND);
    }

    /**
     * 创建 item 信息
     *
     * @return item 信息集合
     */
    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> list = new ArrayList<>();

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(0)
                        .setTitle("初始化")
                        .build()
        );

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(1)
                        .setTitle("加载音频")
                        .build()
        );

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(2)
                        .setTitle("播放音频")
                        .build()
        );

        return list;
    }

    /**
     * item 点击
     *
     * @param context  : 上下文
     * @param itemInfo : item 信息
     * @param button   : button位置
     *                 1、{@link HelperItemInfo#BUTTON_TITLE},
     *                 2、{@link HelperItemInfo#BUTTON_CENTER},
     *                 3、{@link HelperItemInfo#BUTTON_LEFT},
     *                 4、{@link HelperItemInfo#BUTTON_RIGHT}
     */
    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (mSoundService == null) {
            return;
        }

        switch (itemInfo.id) {
            case 0:
                CloudMediaSoundService.Builder builder = new CloudMediaSoundService.Builder();
                builder.setContentTypeEnum(CloudContentTypeEnum.CONTENT_TYPE_MUSIC);
                mSoundService.initialize(5, CloudStreamTypeEnum.STREAM_RING, builder, TAG);
                break;
            case 1:
                mSoundService.load(SoundTag, R.raw.sound);
                mSoundService.load(SoundTag + "s", R.raw.sound);
                mSoundService.load(SoundTag + "ss", R.raw.sound);
                break;
            case 2:
                mSoundService.play(SoundTag);
                break;
        }
    }
}
