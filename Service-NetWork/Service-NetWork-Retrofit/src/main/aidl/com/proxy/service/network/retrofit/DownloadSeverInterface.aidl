package com.proxy.service.network.retrofit;

import com.proxy.service.network.retrofit.DownloadClientInterface;

interface DownloadSeverInterface {

    void addCallback(DownloadClientInterface callback);

    void removeCallback(DownloadClientInterface callback);

    void cancel(int downloadId);

}
