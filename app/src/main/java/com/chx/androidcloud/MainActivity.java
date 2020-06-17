package com.chx.androidcloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CloudSystem.init(this);
    }

    public void onClick(View view){

        CloudUtilsInstallService service=CloudSystem.getService(CloudServiceTagUtils.UTILS_INSTALL);
        if (service==null){
            return;
        }
        List<CloudUtilsInstallService.AppInfo> list = service.getAllInstallAppsInfo();
        Logger.Error("list.size  :  "+list.size()+"");
        for (CloudUtilsInstallService.AppInfo appInfo:list){
            Logger.Debug(appInfo.name);
            Logger.Debug(appInfo.packageName);
            Logger.Debug(appInfo.isInstallSd+"");
            Logger.Debug(appInfo.isSystemApp+"");
            Logger.Debug("  ");
        }
    }
}
