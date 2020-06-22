/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
 */

package com.proxy.service.utils.oaid.hw;

public interface HwOaidCallback {
    void onSuccuss(String oaid, boolean isOaidTrackLimited);

    void onFail(String errMsg);
}
