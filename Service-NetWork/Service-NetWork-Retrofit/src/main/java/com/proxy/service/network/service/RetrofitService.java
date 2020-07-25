package com.proxy.service.network.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author : cangHX
 * on 2020/07/23  9:52 PM
 */
public interface RetrofitService {

    /**
     * get 请求
     *
     * @param path : 请求地址
     * @param map  : 请求参数
     * @return 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/24  8:52 PM
     */
    @GET("{path}")
    Call<String> get(@Path("path") String path, @QueryMap() Map<String, String> map);

    /**
     * post 带参请求
     *
     * @param path : 请求地址
     * @param map  : 请求参数
     * @return 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/24  8:52 PM
     */
    @POST("{path}")
    @FormUrlEncoded
    Call<String> postWithParams(@Path("path") String path, @QueryMap() Map<String, String> map);

    /**
     * post 无参请求
     *
     * @param path : 请求地址
     * @return 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/24  8:52 PM
     */
    @POST("{path}")
    Call<String> post(@Path("path") String path);
}
