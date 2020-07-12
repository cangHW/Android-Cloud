package com.proxy.service.ui.fieldcheck;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.interfaces.IReallyUiFieldCheck;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.fieldcheck.node.BaseFieldCheckNode;
import com.proxy.service.ui.util.TaskUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 真正执行检测的地方
 *
 * @author: cangHX
 * on 2020/07/08  09:36
 */
public class RealUiFieldCheckImpl implements IReallyUiFieldCheck {

    private Map<String, List<BaseFieldCheckNode>> mNodeMapper = new HashMap<>();
    private CloudUiFieldCheckErrorCallback mCallback;

    private boolean isError = false;

    public RealUiFieldCheckImpl(Class<?> aClass, CloudUiFieldCheckErrorCallback callback) {
        this.mCallback = callback;

        List<BaseFieldCheckNode> nodeList = FieldCheckDataManager.init(aClass);
        for (BaseFieldCheckNode node : nodeList) {
            List<BaseFieldCheckNode> list = mNodeMapper.get(node.markId);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(node);
            mNodeMapper.put(node.markId, list);
        }
    }

    /**
     * 检测成功执行，主线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    @Override
    public void runUi(Runnable runnable) {
        if (isError) {
            return;
        }
        if (TaskUtils.isMainThread()) {
            runnable.run();
        } else {
            TaskUtils.postUi(runnable);
        }
    }

    /**
     * 检测成功执行，子线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    @Override
    public void runBg(Runnable runnable) {
        if (isError) {
            return;
        }
        if (TaskUtils.isMainThread()) {
            TaskUtils.postBg(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * 添加一个待检测的 String 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param s      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, String s) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list == null || list.size() == 0) {
            Logger.Debug("The judgment condition does not exist. markId : " + markId);
            return this;
        }

        for (BaseFieldCheckNode node : list) {
            isError = node.isHasError(s, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }

    /**
     * 添加一个待检测的 double 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param d      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, double d) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list == null || list.size() == 0) {
            Logger.Debug("The judgment condition does not exist. markId : " + markId);
            return this;
        }

        for (BaseFieldCheckNode node : list) {
            isError = node.isHasError(d, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }

    /**
     * 添加一个待检测的 float 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param f      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, float f) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list == null || list.size() == 0) {
            Logger.Debug("The judgment condition does not exist. markId : " + markId);
            return this;
        }

        for (BaseFieldCheckNode node : list) {
            isError = node.isHasError(f, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }

    /**
     * 添加一个待检测的 int 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param i      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, int i) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list == null || list.size() == 0) {
            Logger.Debug("The judgment condition does not exist. markId : " + markId);
            return this;
        }

        for (BaseFieldCheckNode node : list) {
            isError = node.isHasError(i, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }

    /**
     * 添加一个待检测的 long 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param l      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, long l) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list == null || list.size() == 0) {
            Logger.Debug("The judgment condition does not exist. markId : " + markId);
            return this;
        }

        for (BaseFieldCheckNode node : list) {
            isError = node.isHasError(l, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }

    /**
     * 添加一个待检测的 boolean 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param b      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck of(String markId, boolean b) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list == null || list.size() == 0) {
            Logger.Debug("The judgment condition does not exist. markId : " + markId);
            return this;
        }

        for (BaseFieldCheckNode node : list) {
            isError = node.isHasError(b, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }
}
