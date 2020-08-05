package com.proxy.service.network.cache;

import android.text.TextUtils;

import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.services.CloudNetWorkRequestService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/05  4:04 PM
 */
public class CallCache {

    private static final ArrayList<CallInfo> CALL_INFOS = new ArrayList<>();

    public static void put(String tag, CloudNetWorkRequestService service, CloudNetWorkCall<?> call) {
        CallInfo callInfo = getCallInfo(tag, service);
        callInfo.addCloudNetWorkCall(call);
    }

    public static void cancelAll() {
        ArrayList<CallInfo> arrayList = new ArrayList<>(CALL_INFOS);
        for (CallInfo callInfo : arrayList) {
            if (callInfo.isAlive()) {
                callInfo.cancel();
            }
            CALL_INFOS.remove(callInfo);
        }
    }

    public static void cancelByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        ArrayList<CallInfo> arrayList = new ArrayList<>(CALL_INFOS);
        for (CallInfo callInfo : arrayList) {
            if (callInfo.isAlive() && tag.equals(callInfo.tag)) {
                callInfo.cancel();
            }
            CALL_INFOS.remove(callInfo);
        }
    }

    public static void cancelByService(CloudNetWorkRequestService service) {
        if (service == null) {
            return;
        }
        ArrayList<CallInfo> arrayList = new ArrayList<>(CALL_INFOS);
        for (CallInfo callInfo : arrayList) {
            if (callInfo.isAlive()) {
                CloudNetWorkRequestService requestService = callInfo.getService();
                if (requestService == null || requestService == service) {
                    callInfo.cancel();
                }
            }
            CALL_INFOS.remove(callInfo);
        }
    }

    public static void notifyCancel() {
        ArrayList<CallInfo> arrayList = new ArrayList<>(CALL_INFOS);
        for (CallInfo callInfo : arrayList) {
            callInfo.notifyCancel();
        }
    }

    private static synchronized CallInfo getCallInfo(String tag, CloudNetWorkRequestService service) {
        CallInfo info;
        ArrayList<CallInfo> arrayList = new ArrayList<>(CALL_INFOS);
        for (CallInfo callInfo : arrayList) {
            if (callInfo.check(tag, service)) {
                return callInfo;
            }
        }
        info = new CallInfo();
        info.setTag(tag);
        info.setCloudNetWorkRequestService(service);
        CALL_INFOS.add(info);
        return info;
    }

    private static class CallInfo {

        private String tag;
        private WeakReference<CloudNetWorkRequestService> weakReference;
        private final List<CloudNetWorkCall<?>> list = new ArrayList<>();
        private boolean isAlive = true;

        public CallInfo() {
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setCloudNetWorkRequestService(CloudNetWorkRequestService service) {
            this.weakReference = new WeakReference<>(service);
        }

        public CloudNetWorkRequestService getService() {
            return weakReference != null ? weakReference.get() : null;
        }

        public void addCloudNetWorkCall(CloudNetWorkCall<?> call) {
            synchronized (list) {
                if (!isAlive) {
                    return;
                }
                this.list.add(call);
            }
        }

        public void cancel() {
            synchronized (list) {
                isAlive = false;
                List<CloudNetWorkCall<?>> clone = new ArrayList<>(list);
                for (CloudNetWorkCall<?> call : clone) {
                    call.cancel();
                }
            }
        }

        public boolean isAlive() {
            synchronized (list) {
                return isAlive;
            }
        }

        public boolean check(String tag, CloudNetWorkRequestService service) {
            CloudNetWorkRequestService requestService = weakReference != null ? weakReference.get() : null;
            return tag.equals(this.tag) && service == requestService;
        }

        public void notifyCancel() {
            List<CloudNetWorkCall<?>> clone = new ArrayList<>(list);
            for (CloudNetWorkCall<?> call : clone) {
                if (call.isCanceled()) {
                    list.remove(call);
                }
            }
        }
    }
}
