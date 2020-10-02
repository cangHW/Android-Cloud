package com.proxy.androidcloud.module_library.bitmap;

import android.content.Context;

import com.proxy.androidcloud.detail.DetailActivity;
import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.androidcloud.helper.DetailHelperType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/09/23  10:15 PM
 */
public class BitmapListHelper extends AbstractListHelper {
    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> infos = new ArrayList<>();
        infos.add(HelperItemInfo.builder()
                .setId(0)
                .setTitle("图片添加文字")
                .build());
        infos.add(HelperItemInfo.builder()
                .setId(1)
                .setTitle("图形验证码")
                .build());
        return infos;
    }

    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        switch (itemInfo.id){
            case 0:
                DetailActivity.launch(context, DetailHelperType.BITMAP_PRINT_WORD);
                break;
            case 1:
                DetailActivity.launch(context, DetailHelperType.BITMAP_CAPTCHA);
                break;
            default:
        }
    }
}
