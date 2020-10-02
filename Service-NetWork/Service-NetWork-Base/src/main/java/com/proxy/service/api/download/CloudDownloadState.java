package com.proxy.service.api.download;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : cangHX
 * on 2020/09/03  10:39 PM
 */
@IntDef({
        CloudDownloadState.EMPTY,
        CloudDownloadState.ADD,
        CloudDownloadState.START,
        CloudDownloadState.PAUSE,
        CloudDownloadState.CONTINUES,
        CloudDownloadState.LOADING,
        CloudDownloadState.COMPLETED,
        CloudDownloadState.FAILED
})
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface CloudDownloadState {

    /**
     * 未知任务
     */
    int EMPTY = 0;

    /**
     * 添加
     */
    int ADD = 1;
    /**
     * 开始
     */
    int START = 2;
    /**
     * 暂停
     */
    int PAUSE = 3;
    /**
     * 继续
     */
    int CONTINUES = 4;
    /**
     * 下载中
     */
    int LOADING = 5;
    /**
     * 完成
     */
    int COMPLETED = 6;
    /**
     * 失败
     */
    int FAILED = 7;
}
