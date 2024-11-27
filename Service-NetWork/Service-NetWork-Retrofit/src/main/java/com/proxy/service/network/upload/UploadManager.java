package com.proxy.service.network.upload;

import android.os.Environment;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.log.Logger;
import com.proxy.service.network.factory.RetrofitManager;
import com.proxy.service.network.service.RetrofitService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : cangHX
 * on 2020/11/17  9:17 PM
 */
public class UploadManager {

    private Logger logger = Logger.create("upload");

    private CloudUtilsTaskService mTaskService;

    private UploadManager() {
        mTaskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);
    }

    private static class Factory {
        private static final UploadManager INSTANCE = new UploadManager();
    }

    public static UploadManager getInstance() {
        return Factory.INSTANCE;
    }

    public void ss() {
        String path = "Environment.getExternalStorageDirectory().getPath()";

        String url = "https://graph.baidu.com/upload";
        File file = new File(path,"asd.png");

        RequestBody requestBody = RequestBody.create(MediaType.parse("binary"), file);

        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("image_source", "PC_UPLOAD_SEARCH_FILE")
                .addFormDataPart("tn", "PC")
                .addFormDataPart("from", "PC")
                .addFormDataPart("image", file.getName(), requestBody)
                .setType(MediaType.parse("multipart/form-data"))
                .build();

        RetrofitManager.getInstance()
                .getRetrofit()
                .create(RetrofitService.class)
                .upload(url, body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        logger.error("onResponse");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        logger.error("onFailure");
                    }
                });
    }

}
