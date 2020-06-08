package com.cloud.base;

import com.cloud.base.base.BaseService;

import java.util.ArrayList;
import java.util.List;

public class MyClass {

    public static void main(String[] args){

        List<String> xx=new ArrayList<>();
        xx.add("aaaa");
        int i =2;
        if (xx.size()>=i) {
            xx.add(i, "aaaa");
        }else {
            System.out.println("asd");
        }

        System.out.println(BaseService.class.getCanonicalName());
    }

}
