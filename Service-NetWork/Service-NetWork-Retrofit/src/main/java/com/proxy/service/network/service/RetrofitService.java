package com.proxy.service.network.service;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author : cangHX
 * on 2020/07/23  9:52 PM
 */
public interface RetrofitService {

    /**
     * get 请求
     *
     * @param url    : 请求地址
     * @param header : 请求header
     * @param params : 请求参数
     * @return 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/24  8:52 PM
     */
    @GET()
    Call<String> get(@Url String url, @HeaderMap Map<String, String> header, @QueryMap() Map<String, String> params);

    /**
     * post
     *
     * @param url    : 请求地址
     * @param header : 请求header
     * @param params : 请求参数
     * @return 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/24  8:52 PM
     */
    @POST()
    Call<String> post(@Url String url, @HeaderMap Map<String, String> header, @QueryMap() Map<String, String> params);

    /**
     * 下载任务
     *
     * @param url   : 下载地址
     * @param start : 从哪个字节开始下载
     * @return 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 9:40 PM
     */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @Header("RANGE") String start);

    /**
     * 上传任务
     *
     * @param body : 上传体
     * @param url  : 上传地址
     * @return 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/16 3:49 PM
     */
    @POST
    Call<ResponseBody> upload(@Url String url, @Body RequestBody body);

}
