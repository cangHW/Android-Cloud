package com.proxy.service.api.multidex;

/**
 * 系统相关判断
 *
 * @author: cangHX
 * on 2020/06/22  09:55
 */
class OsUtils {

    /**
     * 是否是阿里云OS系统
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-22 09:57
     */
    static boolean isYunOs() {
        try {
            String version = System.getProperty("ro.yunos.version");
            String vmName = System.getProperty("java.vm.name");

            boolean checkVn = vmName != null && vmName.toLowerCase().contains("lemur");
            boolean checkVs = version != null && version.trim().length() > 0;

            return checkVn || checkVs;
        } catch (Throwable ignore) {
            return false;
        }
    }

    /**
     * 是否是鸿蒙OS系统
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-22 09:59
     */
    static boolean isHmOs() {
        return false;
    }

}
