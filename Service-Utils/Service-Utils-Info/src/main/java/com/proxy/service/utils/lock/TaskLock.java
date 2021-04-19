package com.proxy.service.utils.lock;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : cangHX
 * on 2020/08/07  10:53 PM
 */
public class TaskLock<RESPONSE> extends AbstractLock {

    public static class Response<RESPONSE> {
        public boolean isSuccess = false;
        public RESPONSE response;
        public Throwable throwable;
    }

    private final AtomicBoolean isCancel = new AtomicBoolean(false);
    private final AtomicBoolean isLock = new AtomicBoolean(false);
    private List<Response<RESPONSE>> responseList = new ArrayList<>();

    /**
     * 申请锁
     *
     * @return true 申请成功，false 申请失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:23 PM
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean lock() {
        return isLock.compareAndSet(false, true);
    }

    /**
     * 尝试并等待申请共享锁，直到成功
     *
     * @param runnable : 申请成功回调
     * @return 是否排队成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:24 PM
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean trySharedLock(final Runnable runnable) {
        LockInfo info = new LockInfo();
        info.isLock = isLock;
        info.task = new Runnable() {
            @Override
            public void run() {
                if (isCancel.get()) {
                    return;
                }
                runnable.run();
            }
        };
        return add(info);
    }

    /**
     * 尝试并等待申请独占锁，直到成功
     *
     * @param runnable : 申请成功回调
     * @return 是否排队成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:24 PM
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean tryLock(final Runnable runnable) {
        LockInfo info = new LockInfo();
        info.isLock = isLock;
        info.task = new Runnable() {
            @Override
            public void run() {
                if (isCancel.get()) {
                    return;
                }
                lock();
                runnable.run();
            }
        };
        return add(info);
    }

    /**
     * 放弃锁
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:24 PM
     */
    public void unLock() {
        isLock.set(false);
    }

    /**
     * 取消所有申请
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:24 PM
     */
    public void cancel() {
        isCancel.set(true);
        unLock();
    }

    public void setResponse(RESPONSE response) {
        Response<RESPONSE> responseResponse = new Response<>();
        responseResponse.response = response;
        responseResponse.isSuccess = true;
        responseList.add(responseResponse);
    }

    public void setThrowable(Throwable throwable) {
        Response<RESPONSE> responseResponse = new Response<>();
        responseResponse.throwable = throwable;
        responseResponse.isSuccess = true;
        responseList.add(responseResponse);
    }

    public void setResponseList(List<Response<RESPONSE>> responseList) {
        this.responseList = responseList;
    }

    @NonNull
    public List<Response<RESPONSE>> getResponseList() {
        if (responseList == null) {
            responseList = new ArrayList<>();
        }
        return responseList;
    }

}
