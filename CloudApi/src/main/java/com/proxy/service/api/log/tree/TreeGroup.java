package com.proxy.service.api.log.tree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.log.base.LogTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: cangHX
 * @data: 2024/11/15 09:59
 * @desc:
 */
public class TreeGroup extends LogTree {

    private static final LogTree[] TREE_ARRAY_EMPTY = new LogTree[0];
    private static final List<LogTree> FOREST = new ArrayList<>();
    private volatile static LogTree[] forestAsArray = TREE_ARRAY_EMPTY;

    /**
     * @noinspection ToArrayCallWithZeroLengthArrayArgument
     */
    public static void plant(@NonNull LogTree... trees) {
        synchronized (FOREST) {
            Collections.addAll(FOREST, trees);
            forestAsArray = FOREST.toArray(new LogTree[FOREST.size()]);
        }
    }

    @Override
    public void setTag(@NonNull String tag) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.setTag(tag);
        }
    }

    @Override
    public void v(@NonNull String message, @NonNull Object[] args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.v(message, args);
        }
    }

    @Override
    public void v(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.v(throwable, message, args);
        }
    }

    @Override
    public void v(@NonNull Throwable throwable) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.v(throwable);
        }
    }

    @Override
    public void d(@NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.d(message, args);
        }
    }

    @Override
    public void d(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.d(throwable, message, args);
        }
    }

    @Override
    public void d(@NonNull Throwable throwable) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.d(throwable);
        }
    }

    @Override
    public void i(@NonNull String message, @NonNull Object[] args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.i(message, args);
        }
    }

    @Override
    public void i(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.i(throwable, message, args);
        }
    }

    @Override
    public void i(@NonNull Throwable throwable) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.i(throwable);
        }
    }

    @Override
    public void w(@NonNull String message, @NonNull Object[] args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.w(message, args);
        }
    }

    @Override
    public void w(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.w(throwable, message, args);
        }
    }

    @Override
    public void w(@NonNull Throwable throwable) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.w(throwable);
        }
    }

    @Override
    public void e(@NonNull String message, @NonNull Object[] args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.e(message, args);
        }
    }

    @Override
    public void e(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.e(throwable, message, args);
        }
    }

    @Override
    public void e(@NonNull Throwable throwable) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.e(throwable);
        }
    }

    @Override
    public void wtf(@NonNull String message, @NonNull Object[] args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.wtf(message, args);
        }
    }

    @Override
    public void wtf(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.wtf(throwable, message, args);
        }
    }

    @Override
    public void wtf(@NonNull Throwable throwable) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.wtf(throwable);
        }
    }

    @Override
    public void log(int priority, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.log(priority, message, args);
        }
    }

    @Override
    public void log(int priority, @NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.log(priority, throwable, message, args);
        }
    }

    @Override
    public void log(int priority, @NonNull Throwable throwable) {
        LogTree[] forest = forestAsArray;
        for (LogTree logTree : forest) {
            logTree.log(priority, throwable);
        }
    }

    @Override
    protected void onLog(int priority, @NonNull String tag, @NonNull String message, @Nullable Throwable throwable) {
        //nothing
    }
}