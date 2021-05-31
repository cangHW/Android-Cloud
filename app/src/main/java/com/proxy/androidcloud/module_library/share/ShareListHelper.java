package com.proxy.androidcloud.module_library.share;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.services.CloudUtilsShareService;
import com.proxy.service.api.services.CloudUtilsSystemPageService;
import com.proxy.service.api.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2021/05/24  9:53 PM
 */
public class ShareListHelper extends AbstractListHelper {

    private static final Logger logger = Logger.create(ShareListHelper.class.getName());

    private final CloudUtilsShareService mShareService;
    private final CloudUtilsSystemPageService mSystemPageService;
    private final CloudUtilsFileService mFileService;

    public ShareListHelper() {
        mShareService = CloudSystem.getService(CloudUtilsShareService.class);
        mSystemPageService = CloudSystem.getService(CloudUtilsSystemPageService.class);
        mFileService = CloudSystem.getService(CloudUtilsFileService.class);
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
                        .setTitle("跳转相册")
                        .setId(1)
                        .build()
        );

        list.add(
                HelperItemInfo
                        .builder()
                        .setId(2)
                        .setTitle("分享图片")
                        .build()
        );

        list.add(
                HelperItemInfo
                        .builder()
                        .setTitle("分享文字")
                        .setId(3)
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
    @SuppressWarnings("deprecation")
    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (mShareService == null || mSystemPageService == null || mFileService == null) {
            return;
        }
        switch (itemInfo.id) {
            case 1:

                break;
            case 2:
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File picFile = new File(file,"12-03-30-07011152914460.jpg");
                if (picFile.exists()) {
                    //设置安全路径，允许此路径下的文件进行分享
                    mFileService.addProviderResourcePath(picFile.getAbsolutePath());
                    //发起分享
                    mShareService.openSystemShareImg(picFile.getAbsolutePath(), "图片分享");
                }else {
                    Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                mShareService.openSystemShareTxt("测试文字分享", "测试分享");
                break;
        }
    }
}
