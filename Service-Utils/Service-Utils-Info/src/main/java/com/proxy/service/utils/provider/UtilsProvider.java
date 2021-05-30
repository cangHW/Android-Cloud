package com.proxy.service.utils.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: cangHX
 * on 2020/06/12  15:37
 */
public class UtilsProvider extends ContentProvider {

    private static final String[] COLUMNS = {
            OpenableColumns.DISPLAY_NAME,
            OpenableColumns.SIZE
    };

    /**
     * 安全路径
     */
    @GuardedBy("sCache")
    private static final Set<String> SECURITY_PATHS = new HashSet<>();

    @GuardedBy("sCache")
    private static final HashMap<String, PathStrategy> sCache = new HashMap<>();
    private PathStrategy mStrategy;

    /**
     * 添加安全路径
     */
    public static void addSecurityPaths(@NonNull String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        SECURITY_PATHS.add(filePath);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public void attachInfo(@NonNull Context context, @NonNull ProviderInfo info) {
        super.attachInfo(context, info);

        if (info.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (!info.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }

        mStrategy = new SimplePathStrategy(info.authority);
    }

    public static Uri getUriForFile(@NonNull String authority, @NonNull File file) {
        final PathStrategy strategy = getPathStrategy(authority);
        return strategy.getUriForFile(file);
    }

    static PathStrategy getPathStrategy(String authority) {
        PathStrategy strategy = sCache.get(authority);
        if (strategy == null) {
            strategy = new SimplePathStrategy(authority);
            sCache.put(authority, strategy);
        }
        return strategy;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final File file = mStrategy.getFileForUri(uri);

        if (projection == null) {
            projection = COLUMNS;
        }

        String[] cols = new String[projection.length];
        Object[] values = new Object[projection.length];
        int i = 0;
        for (String col : projection) {
            if (OpenableColumns.DISPLAY_NAME.equals(col)) {
                cols[i] = OpenableColumns.DISPLAY_NAME;
                values[i++] = file.getName();
            } else if (OpenableColumns.SIZE.equals(col)) {
                cols[i] = OpenableColumns.SIZE;
                values[i++] = file.length();
            }
        }

        cols = copyOf(cols, i);
        values = copyOf(values, i);

        final MatrixCursor cursor = new MatrixCursor(cols, 1);
        cursor.addRow(values);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final File file = mStrategy.getFileForUri(uri);

        final int lastDot = file.getName().lastIndexOf('.');
        if (lastDot >= 0) {
            final String extension = file.getName().substring(lastDot + 1);
            final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
        }

        return "application/octet-stream";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final File file = mStrategy.getFileForUri(uri);
        return file.delete() ? 1 : 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        final File file = mStrategy.getFileForUri(uri);
        if (file == null || !file.exists()) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout(uri.getEncodedPath()).build());
            return null;
        }
        final int fileMode = modeToMode(mode);
        return ParcelFileDescriptor.open(file, fileMode);
    }

    private static int modeToMode(String mode) {
        int modeBits;
        if ("r".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_ONLY;
        } else if ("w".equals(mode) || "wt".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_WRITE_ONLY
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_TRUNCATE;
        } else if ("wa".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_WRITE_ONLY
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_APPEND;
        } else if ("rw".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_WRITE
                    | ParcelFileDescriptor.MODE_CREATE;
        } else if ("rwt".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_WRITE
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_TRUNCATE;
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        return modeBits;
    }

    interface PathStrategy {

        Uri getUriForFile(File file);

        File getFileForUri(Uri uri);
    }

    static class SimplePathStrategy implements PathStrategy {

        private final String mAuthority;

        SimplePathStrategy(String authority) {
            mAuthority = authority;
        }

        @Override
        public Uri getUriForFile(File file) {
            String path = "";
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                Logger.Error(e);
            }
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

//            if (SECURITY_PATHS.size() == 0) {
//                isPermission = true;
//                Logger.Warning(CloudApiError.INSTALL_SECURITY_PATH_ERROR.setMsg("Lack of security path, the current application is easy to be broken, it is recommended to set the security path to improve the security level of this application, through the \"addProviderResourcePath\" method").build());
//            }

            if (!isPermission) {
                Logger.Error("The current path is an illegal path and is not allowed to share files，You can try to set it to a safe path by \"addProviderResourcePath\" method.");
                return null;
            }

            return new Uri.Builder().scheme("content").authority(mAuthority).encodedPath(path).build();
        }

        @Override
        public File getFileForUri(Uri uri) {
            String path = uri.getEncodedPath();
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

//            if (SECURITY_PATHS.size() == 0) {
//                isPermission = true;
//                Logger.Warning(CloudApiError.INSTALL_SECURITY_PATH_ERROR.setMsg("Lack of security path, the current application is easy to be broken, it is recommended to set the security path to improve the security level of this application, through the \"addProviderResourcePath\" method").build());
//            }

            if (!isPermission) {
                Logger.Error("The current path is an illegal path and is not allowed to share files，You can try to set it to a safe path by \"addProviderResourcePath\" method.");
                return null;
            }

            return new File(path);
        }
    }

    private static String[] copyOf(String[] original, int newLength) {
        final String[] result = new String[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        final Object[] result = new Object[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }
}
