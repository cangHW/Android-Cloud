package com.proxy.service.api.multidex;

import com.proxy.service.api.log.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 虚拟机多dex相关判断
 *
 * @author: cangHX
 * on 2020/06/22  09:53
 */
class VmMultiDex {

    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?");
    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;

    /**
     * 判断当前虚拟机系统是否支持多dex
     *
     * @return true 支持，false 不支持
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-22 10:27
     */
    static boolean isVmMultiDexCapable() {
        boolean isMultidexCapable = false;
        String vmName;

        if (OsUtils.isYunOs()) {
            vmName = "'YunOS'";
            isMultidexCapable = isYunOsMultiDexCapable();
        } else if (OsUtils.isHmOs()) {
            vmName = "'HmOs'";
        } else {
            vmName = "'Android'";
            isMultidexCapable = isAndroidMultiDexCapable();
        }

        Logger.d("VM with name " + vmName + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }

    private static boolean isYunOsMultiDexCapable() {
        try {
            return Integer.valueOf(System.getProperty("ro.build.version.sdk")) >= 21;
        } catch (Throwable throwable) {
            return false;
        }
    }

    private static boolean isAndroidMultiDexCapable() {
        String versionString = System.getProperty("java.vm.version");
        if (versionString != null) {
            Matcher matcher = PATTERN.matcher(versionString);
            if (matcher.matches()) {
                try {
                    int major = Integer.parseInt(matcher.group(1));
                    int minor = Integer.parseInt(matcher.group(2));

                    boolean isMajor = major > VM_WITH_MULTIDEX_VERSION_MAJOR;
                    boolean isMinor = (major == VM_WITH_MULTIDEX_VERSION_MAJOR) && (minor >= VM_WITH_MULTIDEX_VERSION_MINOR);

                    return isMajor || isMinor;
                } catch (Throwable ignore) {
                }
            }
        }
        return false;
    }

}
