// DownloadSeverInterface.aidl
package com.proxy.service.network.retrofit;

// Declare any non-default types here with import statements

import com.proxy.service.network.retrofit.DownloadClientInterface;

interface DownloadSeverInterface {

    void addCallback(DownloadClientInterface callback);

    void removeCallback(DownloadClientInterface callback);

    void cancel(int downloadId);

}
