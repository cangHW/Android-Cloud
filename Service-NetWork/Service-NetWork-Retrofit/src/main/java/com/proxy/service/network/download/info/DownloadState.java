package com.proxy.service.network.download.info;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : cangHX
 * on 2020/09/03  10:39 PM
 */
@IntDef({
        DownloadState.ADD,
        DownloadState.START,
        DownloadState.PAUSE,
        DownloadState.CONTINUES,
        DownloadState.LOADING,
        DownloadState.COMPLETED,
        DownloadState.FAILED
})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadState {
    /**
     * 添加
     */
    int ADD = 0;
    /**
     * 开始
     */
    int START = 1;
    /**
     * 暂停
     */
    int PAUSE = 2;
    /**
     * 继续
     */
    int CONTINUES = 3;
    /**
     * 下载中
     */
    int LOADING = 4;
    /**
     * 完成
     */
    int COMPLETED = 5;
    /**
     * 失败
     */
    int FAILED = 6;
}
