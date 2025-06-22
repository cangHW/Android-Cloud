package com.proxy.service.api.log;

import com.proxy.service.api.log.tree.DebugTree;
import com.proxy.service.api.log.tree.ReleaseTree;
import com.proxy.service.api.log.tree.TreeGroup;

/**
 * @author: cangHX
 * @data: 2024/4/28 18:53
 * @desc:
 */
public class LogInit {

    public static void setIsDebug(boolean isDebug) {
        if (isDebug) {
            TreeGroup.plant(new DebugTree());
        } else {
            TreeGroup.plant(new ReleaseTree());
        }
    }
}