package com.proxy.service.api.multidex;

import android.content.Context;

import com.proxy.service.api.log.Logger;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import dalvik.system.DexFile;

/**
 * dex加载相关
 *
 * @author: cangHX
 * on 2020/06/22  13:57
 */
public class ClassUtils {

    private static final String EXTRACTED_SUFFIX = ".zip";

    /**
     * 通过包名获取包下面的所有文件
     *
     * @param context     : 上下文环境
     * @param packageName : 包名
     * @return 文件路径集合
     * @throws Throwable 运行过程中的异常
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-22 14:33
     */
    public static Set<String> getFileNameByPackageName(Context context, final String packageName) throws Throwable {
        final Set<String> classNames = new HashSet<>();

        List<String> paths = SourceDex.getDexPaths(context);
        final CountDownLatch downLatch = new CountDownLatch(paths.size());

        for (final String path : paths) {
            run(new Runnable() {
                @Override
                public void run() {
                    DexFile dexfile = null;

                    try {
                        if (path.endsWith(EXTRACTED_SUFFIX)) {
                            dexfile = DexFile.loadDex(path, path + ".tmp", 0);
                        } else {
                            dexfile = new DexFile(path);
                        }

                        Enumeration<String> dexEntries = dexfile.entries();
                        while (dexEntries.hasMoreElements()) {
                            String className = dexEntries.nextElement();
                            if (className.startsWith(packageName)) {
                                classNames.add(className);
                            }
                        }
                    } catch (Throwable throwable) {
                        Logger.e(throwable, "Scan map file in dex files made error.");
                    }

                    if (dexfile != null) {
                        try {
                            dexfile.close();
                        } catch (Throwable ignore) {
                        }
                    }
                    downLatch.countDown();
                }
            });
        }

        downLatch.await();

        Logger.d("Filter " + classNames.size() + " classes by packageName <" + packageName + ">");
        return classNames;
    }

    /**
     * 执行线程任务
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-22 14:26
     */
    private static void run(final Runnable runnable) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                runnable.run();
            }
        }.start();
    }

}