package com.proxy.service.api.download;

/**
 * @author : cangHX
 * on 2020/09/02  7:54 PM
 */
public final class CloudNetWorkDownloadInfo {

    private int downloadId = -1;
    private boolean enable = true;

    private String filePath;
    private String fileCachePath;
    private String fileName;
    private String fileUrl;
    private String fileMd5;
    private String tag;

    private CloudNetWorkDownloadInfo(Builder builder){

    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private Builder(){

        }
    }
}
