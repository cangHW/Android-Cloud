package com.proxy.service.utils;

import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.utils.info.CloudUtilsInstallServiceImpl;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/17  14:01
 */
public class MyClass {

    public static void main(String[] args){
        CloudUtilsInstallServiceImpl service = new CloudUtilsInstallServiceImpl();
        List<CloudUtilsInstallService.AppInfo> list=service.getAllInstallAppsInfo();
        long xx=list.get(0).longVersionCode;
    }

}
