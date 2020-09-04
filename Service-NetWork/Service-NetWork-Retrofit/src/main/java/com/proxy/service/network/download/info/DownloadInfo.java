package com.proxy.service.network.download.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author : cangHX
 * on 2020/09/03  10:32 PM
 */
public class DownloadInfo implements Parcelable {

    public int downloadId = -1;

    public int serviceId;

    public String filePath;
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

    protected DownloadInfo(Parcel in) {
        downloadId = in.readInt();
        serviceId = in.readInt();
        filePath = in.readString();
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
        dest.writeString(filePath);
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
}
