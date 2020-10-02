package com.proxy.androidcloud.helper;

import com.proxy.androidcloud.module_library.bitmap.CaptchaHelper;
import com.proxy.androidcloud.module_library.bitmap.PrintWordHelper;
import com.proxy.androidcloud.module_library.install.InstallHelper;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.services.CloudUtilsInstallService;

/**
 * @author : cangHX
 * on 2020/09/23  10:05 PM
 */
public enum DetailHelperType {

    /**
     * 图片处理
     */
    BITMAP_PRINT_WORD() {
        @Override
        public String serviceName() {
            return CloudUtilsBitmapService.class.getSimpleName();
        }

        @Override
        public AbstractDetailHelper create() {
            return new PrintWordHelper();
        }
    },

    /**
     * 图片处理
     */
    BITMAP_CAPTCHA() {
        @Override
        public String serviceName() {
            return CloudUtilsBitmapService.class.getSimpleName();
        }

        @Override
        public AbstractDetailHelper create() {
            return new CaptchaHelper();
        }
    },

    /**
     * 安装应用
     */
    INSTALL() {
        @Override
        public String serviceName() {
            return CloudUtilsInstallService.class.getSimpleName();
        }

        @Override
        public AbstractDetailHelper create() {
            return new InstallHelper();
        }
    };


    public abstract String serviceName();

    public abstract AbstractDetailHelper create();

}
