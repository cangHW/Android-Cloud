package com.proxy.service.api.lifecycle;

/**
 * @author : cangHX
 * on 2021/05/20  9:45 PM
 */
public enum FragmentLifecycleState {

    /**
     * onAttach
     */
    LIFECYCLE_ATTACH("lifecycle_attach", 0),
    /**
     * onCreate
     */
    LIFECYCLE_CREATE("lifecycle_create", 1),
    /**
     * onCreateView
     */
    LIFECYCLE_CREATE_VIEW("lifecycle_create_view", 2),
    /**
     * onViewCreate
     */
    LIFECYCLE_VIEW_CREATE("lifecycle_view_create", 3),
    /**
     * onStart
     */
    LIFECYCLE_START("lifecycle_start", 4),
    /**
     * onResume
     */
    LIFECYCLE_RESUME("lifecycle_resume", 5),
    /**
     * onPause
     */
    LIFECYCLE_PAUSE("lifecycle_pause", 6),
    /**
     * onStop
     */
    LIFECYCLE_STOP("lifecycle_stop", 7),
    /**
     * onDestroy
     */
    LIFECYCLE_DESTROY("lifecycle_destroy", 8),
    /**
     * onDestroyView
     */
    LIFECYCLE_DESTROY_VIEW("lifecycle_destroy_view", 9),
    /**
     * onDetach
     */
    LIFECYCLE_DETACH("lifecycle_detach", 10),
    /**
     * onHide
     */
    LIFECYCLE_HIDE("lifecycle_hide", 11),
    /**
     * onShow
     */
    LIFECYCLE_SHOW("lifecycle_show", 12);

    private final String state;
    private final int code;

    FragmentLifecycleState(String state, int code) {
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
