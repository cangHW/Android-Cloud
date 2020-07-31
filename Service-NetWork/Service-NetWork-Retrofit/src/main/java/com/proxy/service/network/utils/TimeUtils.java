package com.proxy.service.network.utils;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/07/21  9:17 PM
 */
public class TimeUtils {

    public static int toMillis(String name, long duration, TimeUnit unit) {
        if (duration < 0) {
            Logger.Warning(CloudApiError.TIME_ERROR.setMsg(name + " cannot < 0").build());
            return -1;
        }
        if (unit == null) {
            Logger.Warning(CloudApiError.TIME_ERROR.setMsg("unit cannot be null with " + name).build());
            return -1;
        }
        long millis = unit.toMillis(duration);
        if (millis > Integer.MAX_VALUE) {
            Logger.Warning(CloudApiError.TIME_ERROR.setMsg(name + " too large.").build());
            return -1;
        }
        if (millis == 0 && duration > 0) {
            Logger.Warning(CloudApiError.TIME_ERROR.setMsg(name + " too small.").build());
            return -1;
        }
        return (int) millis;
    }

}
