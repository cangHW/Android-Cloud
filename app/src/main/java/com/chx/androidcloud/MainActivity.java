package com.chx.androidcloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cloud.api.manager.CloudSystem;
import com.cloud.api.manager.listener.Converter;
import com.cloud.api.services.CloudUtilsDeviceService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        List<BaseService> list = new ArrayList<>();
//        list.add(new XXXX());
//        CloudSystem.registerServices(list);
        CloudSystem.init(this);
        CloudSystem.addConverter(CloudUtilsDeviceService.class, new Converter<CloudUtilsDeviceService>() {
            @NonNull
            @Override
            public CloudUtilsDeviceService converter(@NonNull final CloudUtilsDeviceService cloudUtilsDeviceService) throws Throwable {
                return new CloudUtilsDeviceService() {
                    @Override
                    public String getImel() {
                        return cloudUtilsDeviceService.getImel();
                    }
                };
            }
        });
        CloudSystem.addConverter("ss333", CloudUtilsDeviceService.class, new Converter<CloudUtilsDeviceService>() {
            @NonNull
            @Override
            public CloudUtilsDeviceService converter(@NonNull CloudUtilsDeviceService cloudUtilsDeviceService) throws Throwable {
                return new XXX() {
                    @Override
                    public void getJson() {

                    }

                    @Override
                    public String getImel() {
                        return "lll";
                    }
                };
            }
        });
    }

    public interface XXX extends CloudUtilsDeviceService{

        void getJson();

    }

    public void onClick(View view){
//        CloudService service=XXXX.class.getAnnotation(CloudService.class);
//        String servicsss = service.serviceTag();
        XXX service=CloudSystem.getServiceWithUuid("ss333","aaaa");
        service.getJson();
//        CloudUtilsDeviceService service2=CloudSystem.getServiceWithUuid("ss333","aaaa");
        Toast.makeText(MainActivity.this,service.getImel(),Toast.LENGTH_SHORT).show();

//        Logger.Error("MainActivity   :   "+service.getImel());
    }
}
