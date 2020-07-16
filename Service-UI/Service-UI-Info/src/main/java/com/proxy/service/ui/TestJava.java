package com.proxy.service.ui;

import java.math.BigDecimal;

/**
 * @author : cangHX
 * on 2020/07/16  4:24 PM
 */
public class TestJava {

    public static void main(String[] args){
        BigDecimal xx = new BigDecimal("9");
        BigDecimal ss = new BigDecimal("6");
        System.out.println(ss.subtract(xx).intValue());
    }

}
