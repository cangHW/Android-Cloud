package com.proxy.service.api;

import com.proxy.service.api.annotations.CloudUiCheckNumber;

import java.lang.reflect.Field;

/**
 * @author: cangHX
 * on 2020/07/07  17:23
 */
public class MyClass {

    @CloudUiCheckNumber(markId = "", message = "asd")
    private static String xx = "";

    @CloudUiCheckNumber(markId = "", message = "xxx")
    public static int ccc = 0;

    @CloudUiCheckNumber(markId = "", message = "asd---")
    private String mmm = "";

    @CloudUiCheckNumber(markId = "", message = "xxx---")
    public int vvvv = 0;

    public static void main(String[] args) {
        //xx.
        Field[] fields = MyClass.class.getDeclaredFields();
        for (Field field : fields) {
            Class aClass = field.getType();
            System.out.println("type  :  " + aClass.getCanonicalName());

            CloudUiCheckNumber checkNumber = field.getAnnotation(CloudUiCheckNumber.class);
            if (checkNumber != null) {
                System.out.println("message  :  " + checkNumber.message());
            }
        }
    }

}
