/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
 */

package com.proxy.service.utils.oaid.hw;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.proxy.service.api.utils.Logger;
import com.uodis.opendevice.aidl.OpenDeviceIdentifierService;

/**
 * 获取华为oaid
 *
 * @author: cangHX
 * on 2020/06/19  18:18
 */
class HwOaidAidlUtil {
    private Logger mLogger = Logger.create("HwOaidAidlUtil");
    private static final String SERVICE_PACKAGE_NAME = "com.huawei.hwid";
    private static final String SERVICE_ACTION = "com.uodis.opendevice.OPENIDS_SERVICE";
    private Context mContext;
    private ServiceConnection mServiceConnection;
    private OpenDeviceIdentifierService mService;
    private HwOaidCallback mCallback;

    HwOaidAidlUtil(Context context) {
        mContext = context;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean bindService() {
        mLogger.info("bindService");
        if (null == mContext) {
            mLogger.error("context is null");
            return false;
        }
        mServiceConnection = new IdentifierServiceConnection();
        Intent intent = new Intent(SERVICE_ACTION);
        intent.setPackage(SERVICE_PACKAGE_NAME);
        boolean result = mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        mLogger.info("bindService result: " + result);
        return result;
    }

    /**
     * 解除绑定获取OAID的aidl服务。| Unbind OAID aidl services.
     */
    private void unbindService() {
        mLogger.info("unbindService");
        if (null == mContext) {
            mLogger.error("context is null");
            return;
        }
        if (null != mServiceConnection) {
            mContext.unbindService(mServiceConnection);
            mService = null;
            mContext = null;
            mCallback = null;
        }
    }

    void getOaid(HwOaidCallback callback) {
        if (null == callback) {
            mLogger.error("callback is null");
            return;
        }
        mCallback = callback;
        // 绑定获取OAID的aidl 服务。| Bind OAID aidl services.
        bindService();
    }

    private final class IdentifierServiceConnection implements ServiceConnection {

        private IdentifierServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mLogger.info("onServiceConnected");
            mService = OpenDeviceIdentifierService.Stub.asInterface(iBinder);
            if (mService == null) {
                return;
            }
            try {
                if (mCallback == null) {
                    return;
                }
                mCallback.onSuccuss(mService.getOaid(), mService.isOaidTrackLimited());
            } catch (Throwable e) {
                mLogger.error("getChannelInfo RemoteException", e);
                if (mCallback == null) {
                    return;
                }
                mCallback.onFail(e.getMessage());
            } finally {
                unbindService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mLogger.info("onServiceDisconnected");
            mService = null;
        }
    }
}
