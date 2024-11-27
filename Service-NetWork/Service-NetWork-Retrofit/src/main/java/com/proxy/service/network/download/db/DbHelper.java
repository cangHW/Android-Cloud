package com.proxy.service.network.download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.log.Logger;
import com.proxy.service.network.download.info.DownloadInfo;

/**
 * @author : cangHX
 * on 2020/09/06  5:13 PM
 */
public class DbHelper {

    private DbManager mDbManager;

    private DbHelper() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        mDbManager = new DbManager(context);
    }

    private static class Factory {
        private static final DbHelper INSTANCE = new DbHelper();
    }

    public static DbHelper getInstance() {
        return Factory.INSTANCE;
    }

    /**
     * 插入数据
     *
     * @param downloadInfo : 下载任务信息
     * @return 数据在数据库中的 ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 5:50 PM
     */
    public long insert(DownloadInfo downloadInfo) {
        if (mDbManager == null) {
            return -1;
        }

        SQLiteDatabase writable = mDbManager.getWritableDatabase();
        try {
            writable.beginTransaction();
            long id = writable.insertOrThrow(TableDownloadInfo.TABLE_NAME, downloadInfo.fileUrl, DownloadInfo.getContentValues(downloadInfo));
            if (id != -1) {
                writable.setTransactionSuccessful();
            }
            return id;
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            writable.endTransaction();
        }
        return -1;
    }

    /**
     * 查询数据
     *
     * @param selection     : 条件语句
     * @param selectionArgs : 判断数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 5:50 PM
     */
    public DownloadInfo query(String selection, String[] selectionArgs) {
        if (mDbManager == null) {
            return null;
        }

        DownloadInfo downloadInfo = null;

        SQLiteDatabase readable = mDbManager.getReadableDatabase();
        try {
            Cursor cursor = readable.query(TableDownloadInfo.TABLE_NAME, null, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                downloadInfo = DownloadInfo.getDownloadInfo(cursor);
                cursor.close();
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            readable.close();
        }
        return downloadInfo;
    }

    /**
     * 更新数据
     *
     * @param downloadInfo : 下载任务信息
     * @param whereClause  : 条件语句
     * @param whereArgs    : 判断数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 5:50 PM
     */
    public void update(DownloadInfo downloadInfo, String whereClause, String[] whereArgs) {
        if (mDbManager == null) {
            return;
        }

        SQLiteDatabase writable = mDbManager.getWritableDatabase();
        try {
            writable.beginTransaction();
            writable.update(TableDownloadInfo.TABLE_NAME, DownloadInfo.getContentValues(downloadInfo), whereClause, whereArgs);
            writable.setTransactionSuccessful();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            writable.endTransaction();
        }
    }

    /**
     * 删除数据
     *
     * @param whereClause : 条件语句
     * @param whereArgs   : 判断数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 5:50 PM
     */
    public void delete(String whereClause, String[] whereArgs) {
        if (mDbManager == null) {
            return;
        }

        SQLiteDatabase writable = mDbManager.getWritableDatabase();
        try {
            writable.beginTransaction();
            writable.delete(TableDownloadInfo.TABLE_NAME, whereClause, whereArgs);
            writable.setTransactionSuccessful();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            writable.endTransaction();
        }
    }

}
