package com.proxy.service.network.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.QueryMap;

/**
 * @author : cangHX
 * on 2020/07/23  9:52 PM
 */
public interface RetrofitService {

    Call<String> get(@QueryMap() Map<String, String> map);

}
