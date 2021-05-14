package com.proxy.androidcloud.module_library.bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.helper.AbstractDetailHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.bitmap.CloudPrintWordMessage;
import com.proxy.service.api.services.CloudUtilsBitmapService;

/**
 * @author : cangHX
 * on 2020/09/23  10:00 PM
 */
public class PrintWordHelper extends AbstractDetailHelper {

    private CloudUtilsBitmapService bitmapService;

    @Override
    public int getLayoutId() {
        if (bitmapService == null) {
            bitmapService = CloudSystem.getService(CloudUtilsBitmapService.class);
        }
        return R.layout.item_print_word;
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
            Bitmap bitmap = bitmapService.toBitmap(R.drawable.enter_app_button, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                return;
            }
            String text = mEditText.getText().toString();
            Bitmap bitmap1 = bitmapService.printWord(bitmap, text, CloudPrintWordMessage.builder()
                    .setBold(true)
                    .setLeft(30)
                    .setTextSize(40)
                    .build());
            mBitmap.setImageBitmap(bitmap1);
        });
    }
}
