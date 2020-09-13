package com.proxy.service.network.download.info;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.network.download.db.TableDownloadInfo;

/**
 * @author : cangHX
 * on 2020/09/03  10:32 PM
 */
public class DownloadInfo implements Parcelable {

    public int downloadId = -1;

    public int serviceId;

    public String fileDir;
    public String fileCachePath;
    public String fileName;
    public String fileUrl;
    public String fileMd5;
    public long fileSize;
    public String tag;

    @DownloadState
    public int state;
    public long startTime;
    public long finishTime;

    private DownloadInfo() {
    }

    protected DownloadInfo(Parcel in) {
        downloadId = in.readInt();
        serviceId = in.readInt();
        fileDir = in.readString();
        fileCachePath = in.readString();
        fileName = in.readString();
        fileUrl = in.readString();
        fileMd5 = in.readString();
        fileSize = in.readLong();
        tag = in.readString();
        state = in.readInt();
        startTime = in.readLong();
        finishTime = in.readLong();
    }

    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel in) {
            return new DownloadInfo(in);
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(downloadId);
        dest.writeInt(serviceId);
        dest.writeString(fileDir);
        dest.writeString(fileCachePath);
        dest.writeString(fileName);
        dest.writeString(fileUrl);
        dest.writeString(fileMd5);
        dest.writeLong(fileSize);
        dest.writeString(tag);
        dest.writeInt(state);
        dest.writeLong(startTime);
        dest.writeLong(finishTime);
    }

    public static ContentValues getContentValues(DownloadInfo downloadInfo) {
        ContentValues values = new ContentValues();
        values.put(TableDownloadInfo.COLUMN_FILE_DIR, downloadInfo.fileDir);
        values.put(TableDownloadInfo.COLUMN_FILE_CACHE_PATH, downloadInfo.fileCachePath);
        values.put(TableDownloadInfo.COLUMN_FILE_NAME, downloadInfo.fileName);
        values.put(TableDownloadInfo.COLUMN_FILE_URL, downloadInfo.fileUrl);
        values.put(TableDownloadInfo.COLUMN_FILE_MD5, downloadInfo.fileMd5);
        values.put(TableDownloadInfo.COLUMN_FILE_SIZE, downloadInfo.fileSize);
        values.put(TableDownloadInfo.COLUMN_TAG, downloadInfo.tag);
        values.put(TableDownloadInfo.COLUMN_STATE, downloadInfo.state);
        values.put(TableDownloadInfo.COLUMN_START_TIME, downloadInfo.startTime);
        values.put(TableDownloadInfo.COLUMN_FINISH_TIME, downloadInfo.finishTime);
        return values;
    }

    public static DownloadInfo getDownloadInfo(Cursor cursor) {
        if (cursor.moveToNext()) {
            DownloadInfo downloadInfo = new DownloadInfo();
            downloadInfo.downloadId = cursor.getInt(cursor.getColumnIndex(TableDownloadInfo.COLUMN_DOWNLOAD_ID));
            downloadInfo.fileDir = cursor.getString(cursor.getColumnIndex(TableDownloadInfo.COLUMN_FILE_DIR));
            downloadInfo.fileCachePath = cursor.getString(cursor.getColumnIndex(TableDownloadInfo.COLUMN_FILE_CACHE_PATH));
            downloadInfo.fileName = cursor.getString(cursor.getColumnIndex(TableDownloadInfo.COLUMN_FILE_NAME));
            downloadInfo.fileUrl = cursor.getString(cursor.getColumnIndex(TableDownloadInfo.COLUMN_FILE_URL));
            downloadInfo.fileMd5 = cursor.getString(cursor.getColumnIndex(TableDownloadInfo.COLUMN_FILE_MD5));
            downloadInfo.fileSize = cursor.getLong(cursor.getColumnIndex(TableDownloadInfo.COLUMN_FILE_SIZE));
            downloadInfo.tag = cursor.getString(cursor.getColumnIndex(TableDownloadInfo.COLUMN_TAG));
            downloadInfo.state = cursor.getInt(cursor.getColumnIndex(TableDownloadInfo.COLUMN_STATE));
            downloadInfo.startTime = cursor.getLong(cursor.getColumnIndex(TableDownloadInfo.COLUMN_START_TIME));
            downloadInfo.finishTime = cursor.getLong(cursor.getColumnIndex(TableDownloadInfo.COLUMN_FINISH_TIME));
            return downloadInfo;
        }
        return null;
    }

    public static DownloadInfo getDownloadInfo(CloudNetWorkDownloadInfo info) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.fileDir = info.getFileDir();
        downloadInfo.fileCachePath = info.getFileCachePath();
        downloadInfo.fileName = info.getFileName();
        downloadInfo.fileUrl = info.getFileUrl();
        downloadInfo.fileMd5 = info.getFileMd5();
        downloadInfo.fileSize = info.getFileSize();
        downloadInfo.tag = info.getTag();
        return downloadInfo;
    }
}
