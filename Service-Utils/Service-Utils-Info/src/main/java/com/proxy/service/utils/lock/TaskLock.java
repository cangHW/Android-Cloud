package com.proxy.service.utils.lock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2020/08/07  10:53 PM
 */
public class TaskLock<RESPONSE> extends AbstractLock {

    private AtomicBoolean isCancel = new AtomicBoolean(false);
    private AtomicInteger count = new AtomicInteger(0);

    private boolean isSuccess = false;
    private RESPONSE mResponse;
    private Throwable mThrowable;

    /**
     * 申请锁
     *
     * @return true 申请成功，false 申请失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:23 PM
     */
    public boolean lock() {
        if (count.get() != 0) {
            return false;
        }
        count.incrementAndGet();
        return true;
    }

    /**
     * 尝试并等待申请共享锁，直到成功
     *
     * @param runnable : 申请成功回调
     * @return 是否排队申请成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:24 PM
     */
    public boolean trySharedLock(final Runnable runnable) {
        Info info = new Info();
        info.atomicInteger = count;
        info.runnable = new Runnable() {
            @Override
            public void run() {
                if (isCancel.get()) {
                    return;
                }
                //todo 线程切换
                runnable.run();
            }
        };
        return add(info);
    }

    /**
     * 尝试并等待申请独占锁，直到成功
     *
     * @param runnable : 申请成功回调
     * @return 是否排队申请成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:24 PM
     */
    public boolean tryLock(final Runnable runnable) {
        Info info = new Info();
        info.atomicInteger = count;
        info.runnable = new Runnable() {
            @Override
            public void run() {
                if (isCancel.get()) {
                    return;
                }
                lock();
                //todo 线程切换
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
        if (count.get() == 0) {
            return;
        }
        if (count.getAndDecrement() <= 0) {
            count.incrementAndGet();
        }
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
        count.set(0);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public RESPONSE getResponse() {
        return mResponse;
    }

    public void setResponse(RESPONSE response) {
        this.mResponse = response;
        this.isSuccess = true;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setThrowable(Throwable throwable) {
        this.mThrowable = throwable;
        this.isSuccess = false;
    }
}
