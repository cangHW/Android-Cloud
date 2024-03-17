package com.proxy.service.network.retrofit;

interface DownloadClientInterface {

    void onStart(int downloadId);

    void onProgress(int downloadId, long progress, long total);

    void onSuccess(int downloadId);

    void onWarning(int downloadId, String warningMsg);

    void onFailed(int downloadId, String errorMsg);

}
