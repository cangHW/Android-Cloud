package com.proxy.service.network.download.db;

/**
 * @author : cangHX
 * on 2020/09/03  10:51 PM
 */
public class TableDownloadInfo {

    public static final String TABLE_NAME = "CloudDownload";

    public static final String COLUMN_DOWNLOAD_ID = "download_id";

    public static final String COLUMN_FILE_DIR = "file_dir";
    public static final String COLUMN_FILE_CACHE_PATH = "file_cache_path";
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_FILE_URL = "file_url";
    public static final String COLUMN_FILE_MD5 = "file_md5";
    public static final String COLUMN_FILE_SIZE = "file_size";
    public static final String COLUMN_TAG = "tag";

    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_FINISH_TIME = "finish_time";

    public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            "(" +
            COLUMN_DOWNLOAD_ID + " integer PRIMARY KEY," +
            COLUMN_FILE_DIR + " text," +
            COLUMN_FILE_CACHE_PATH + " text," +
            COLUMN_FILE_NAME + " text," +
            COLUMN_FILE_URL + " text," +
            COLUMN_FILE_MD5 + " text," +
            COLUMN_FILE_SIZE + " long," +
            COLUMN_TAG + " text," +
            COLUMN_STATE + " integer," +
            COLUMN_START_TIME + " long," +
            COLUMN_FINISH_TIME + " long" +
            ")";

}
