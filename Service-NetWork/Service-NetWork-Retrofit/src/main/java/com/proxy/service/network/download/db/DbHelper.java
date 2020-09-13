package com.proxy.service.network.download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
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
        SQLiteDatabase writable = mDbManager.getWritableDatabase();
        writable.beginTransaction();
        try {
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
        DownloadInfo downloadInfo = null;

        SQLiteDatabase readable = mDbManager.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = readable.query(TableDownloadInfo.TABLE_NAME, null, selection, selectionArgs, null, null, null);
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
        SQLiteDatabase readable = mDbManager.getReadableDatabase();
        readable.beginTransaction();
        try {
            readable.update(TableDownloadInfo.TABLE_NAME, DownloadInfo.getContentValues(downloadInfo), whereClause, whereArgs);
            readable.setTransactionSuccessful();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            readable.endTransaction();
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
        SQLiteDatabase readable = mDbManager.getReadableDatabase();
        readable.beginTransaction();
        try {
            readable.delete(TableDownloadInfo.TABLE_NAME, whereClause, whereArgs);
            readable.setTransactionSuccessful();
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        } finally {
            readable.endTransaction();
        }
    }

}
