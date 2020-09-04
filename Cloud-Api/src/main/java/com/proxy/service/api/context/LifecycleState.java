package com.proxy.service.api.context;

/**
 * @author : cangHX
 * on 2020/08/31  10:59 PM
 */
public enum  LifecycleState {

    /**
     * onCreate
     */
    LIFECYCLE_CREATE("lifecycle_create", 0),
    /**
     * onStart
     */
    LIFECYCLE_START("lifecycle_start", 1),
    /**
     * onResume
     */
    LIFECYCLE_RESUME("lifecycle_resume", 2),
    /**
     * onPause
     */
    LIFECYCLE_PAUSE("lifecycle_pause", 3),
    /**
     * onStop
     */
    LIFECYCLE_STOP("lifecycle_stop", 4),
    /**
     * onDestroy
     */
    LIFECYCLE_DESTROY("lifecycle_destroy", 5);

    private final String state;
    private final int code;

    LifecycleState(String state, int code) {
        this.state = state;
        this.code = code;
    }

    public String state() {
        return state;
    }

    public int code() {
        return code;
    }

}
