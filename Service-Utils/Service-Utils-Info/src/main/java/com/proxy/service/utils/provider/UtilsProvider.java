package com.proxy.service.utils.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/12  15:37
 */
public class UtilsProvider extends ContentProvider {

    /**
     * 安全路径
     */
    private static List<String> SECURITY_PATHS = new ArrayList<>();

    /**
     * 添加安全路径
     * */
    public static void addSecurityPaths(@NonNull String filePath){
        SECURITY_PATHS.add(filePath);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        String decodeUriString = Uri.decode(uri.toString());
        String path = Uri.parse(decodeUriString).getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        boolean isPermission = false;
        for (String securityPath : SECURITY_PATHS) {
            if (path.startsWith(securityPath)) {
                isPermission = true;
                break;
            }
        }

        if (SECURITY_PATHS.size() == 0) {
            isPermission = true;
            Logger.Warning(CloudApiError.INSTALL_SECURITY_PATH_ERROR.setMsg("Lack of security path, the current application is easy to be broken, it is recommended to set the security path to improve the security level of this application, through the \"addProviderResourcePath\" method").build());
        }

        if (!isPermission){
            Logger.Error("The current path is an illegal path and is not allowed to share files，You can try to set it to a safe path by \"addProviderResourcePath\" method.");
            return null;
        }

        File file = new File(path);
        if (file.exists()) {
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        }
        return null;
    }
}
