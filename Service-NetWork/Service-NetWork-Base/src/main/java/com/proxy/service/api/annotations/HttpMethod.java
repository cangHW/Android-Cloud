package com.proxy.service.api.annotations;

import androidx.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : cangHX
 * on 2020/07/27  9:55 PM
 */
@StringDef({
        HttpMethod.GET,
        HttpMethod.POST
})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {

    String GET = "get";

    String POST = "post";

}
