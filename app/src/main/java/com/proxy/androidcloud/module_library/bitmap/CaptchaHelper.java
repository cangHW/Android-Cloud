package com.proxy.androidcloud.module_library.bitmap;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.helper.AbstractDetailHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.bitmap.CloudCaptchaInfo;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.tag.CloudServiceTagUtils;

/**
 * @author : cangHX
 * on 2020/09/24  9:12 PM
 */
public class CaptchaHelper extends AbstractDetailHelper {
    CloudUtilsBitmapService bitmapService;

    @Override
    public int getLayoutId() {
        bitmapService = CloudSystem.getService(CloudServiceTagUtils.UTILS_BITMAP);
        return R.layout.item_captcha;
    }

    @Override
    public void init(Activity activity) {
        EditText mEditText = activity.findViewById(R.id.mEditText);
        Button mButton = activity.findViewById(R.id.mButton);
        ImageView mBitmap = activity.findViewById(R.id.mBitmap);

        mButton.setOnClickListener(v -> {
            if (bitmapService == null) {
                return;
            }
            CloudCaptchaInfo info = bitmapService.captcha(0, 0, mEditText.getText().toString(),0);
            mBitmap.setImageBitmap(info.getBitmap());
            Log.d("asd", "key code : " + info.getKey());
        });
    }
}
