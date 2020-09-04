// DownloadClientInterface.aidl
package com.proxy.service.network.retrofit;

// Declare any non-default types here with import statements

interface DownloadClientInterface {

    void onStart(int downloadId);

    void onProgress(int downloadId, long progress, long total);

    void onPause(int downloadId);

    void onSuccess(int downloadId);

    void onFailed(int downloadId, String errType);

}
