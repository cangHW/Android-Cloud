package com.proxy.service.api.callback.response;

/**
 * @author : cangHX
 * on 2020/07/31  11:52 AM
 */
public final class CloudNetWorkResponse<T> {

    private final T response;
    private final String message;
    private final int code;
    private final Throwable throwable;

    private CloudNetWorkResponse(Builder<T> builder) {
        this.response = builder.response;
        this.message = builder.message;
        this.code = builder.code;
        this.throwable = builder.throwable;
    }

    public static <T> CloudNetWorkResponse<T> success(T response) {
        return success(200, response);
    }

    public static <T> CloudNetWorkResponse<T> success(int code, T response) {
        return new Builder<T>()
                .response(response)
                .code(code)
                .message("OK")
                .build();
    }

    public static <T> CloudNetWorkResponse<T> error(Throwable throwable) {
        return error(-1, throwable);
    }

    public static <T> CloudNetWorkResponse<T> error(int code, Throwable throwable) {
        return new Builder<T>()
                .code(code)
                .throwable(throwable)
                .message("ERROR")
                .build();
    }

    public String message() {
        return message;
    }

    /**
     * http code值
     *
     * @return code 值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 9:52 PM
     */
    public int code() {
        return code;
    }

    /**
     * 错误信息
     *
     * @return 错误信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 9:52 PM
     */
    public Throwable throwable() {
        return throwable;
    }

    /**
     * 返回数据
     *
     * @return 返回值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 9:52 PM
     */
    public T response() {
        return response;
    }

    /**
     * 本次请求是否成功
     *
     * @return true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 9:52 PM
     */
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    private static class Builder<T> {

        private T response;
        private String message;
        private int code;
        private Throwable throwable;

        private Builder() {
        }

        public Builder<T> response(T response) {
            this.response = response;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public CloudNetWorkResponse<T> build() {
            return new CloudNetWorkResponse<>(this);
        }
    }
}
