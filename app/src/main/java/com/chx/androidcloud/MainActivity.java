package com.chx.androidcloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.api.tag.CloudServiceTagLibrary;
import com.proxy.service.api.utils.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CloudSystem.init(this);
    }

    public void onClick(View view){

        CloudUtilsInstallService service=CloudSystem.getService(CloudServiceTagLibrary.UTILS_INSTALL);
        if (service==null){
            return;
        }
        boolean flag =service.isInstallApp("com.huawei.scenepack");
        Logger.Debug(flag+"");
            Logger.Debug("  ");
//        List<CloudUtilsInstallService.AppInfo> list = service.getAllInstallAppsInfo();
//        Logger.Error("list.size  :  "+list.size()+"");
//        for (CloudUtilsInstallService.AppInfo appInfo:list){
//            Logger.Debug(appInfo.name);
//            Logger.Debug(appInfo.packageName);
//            Logger.Debug(appInfo.isInstallSd+"");
//            Logger.Debug(appInfo.isSystemApp+"");
//            Logger.Debug("  ");
//        }
    }
}
