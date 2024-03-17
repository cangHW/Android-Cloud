package com.proxy.service.utils.util;

/**
 * @author: cangHX
 * @date: 2021/12/19 23:19
 * @version: 1.0
 * @desc:
 */
public class FileUtils {

    /**
     * 获取文件扩展名
     *
     * @param path : 文件路径
     * @return: 扩展名格式
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/19 23:20
     */
    public static String getExtension(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("jpg") || lowerPath.endsWith("jpeg")) {
            return "image/jpeg";
        } else if (lowerPath.endsWith("png")) {
            return "image/png";
        } else if (lowerPath.endsWith("gif")) {
            return "image/gif";
        } else if (lowerPath.endsWith("mp4") || lowerPath.endsWith("mpeg4")) {
            return "video/mp4";
        } else if (lowerPath.endsWith("3gp")) {
            return "video/3gp";
        }
        return "";
    }

}
