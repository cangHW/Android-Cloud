package com.proxy.service.api.security;

/**
 * @author : cangHX
 * on 2020/11/10  9:19 PM
 */
public enum SecurityType {

    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    String name;

    SecurityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
