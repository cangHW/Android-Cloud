package com.proxy.service.ui.fieldcheck;

import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.interfaces.IReallyUiFieldCheck;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.fieldcheck.node.BaseFieldCheckNode;

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

    private final Map<String, List<BaseFieldCheckNode>> mNodeMapper = new HashMap<>();
    private final CloudUiFieldCheckErrorCallback mCallback;
    private final Object mObject;
    private final CloudUtilsTaskService mTaskService;
    private Map<String, List<BaseFieldCheckNode>> mNodeMapperWithContent;

    private boolean isError = false;

    public RealUiFieldCheckImpl(Object object, CloudUiFieldCheckErrorCallback callback) {
        this.mObject = object;
        this.mCallback = callback;
        this.mTaskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);

        if (object instanceof Class<?>) {
            isError = true;
            return;
        }

        List<BaseFieldCheckNode> nodeList = FieldCheckDataManager.init(object.getClass());
        for (BaseFieldCheckNode node : nodeList) {
            List<BaseFieldCheckNode> list = mNodeMapper.get(node.markId);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(node);
            mNodeMapper.put(node.markId, list);
        }
    }

    public void setCheckNodes(SparseArray<BaseFieldCheckNode> mCheckNodes) {
        mNodeMapperWithContent = new HashMap<>();
        if (mCheckNodes == null || mCheckNodes.size() == 0) {
            return;
        }
        for (int i = 0; i < mCheckNodes.size(); i++) {
            BaseFieldCheckNode node = mCheckNodes.valueAt(i);

            List<BaseFieldCheckNode> list = mNodeMapperWithContent.get(node.markId);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(node);
            mNodeMapperWithContent.put(node.markId, list);
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
    public void runMain(Runnable runnable) {
        if (isError) {
            return;
        }
        if (mTaskService.isMainThread()) {
            runnable.run();
        } else {
            mTaskService.callUiThread(() -> {
                runnable.run();
                return null;
            });
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
    public void runWork(Runnable runnable) {
        if (isError) {
            return;
        }
        if (mTaskService.isMainThread()) {
            mTaskService.callWorkThread(() -> {
                runnable.run();
                return null;
            });
        } else {
            runnable.run();
        }
    }

    /**
     * 发起检测
     *
     * @param markId : 标记id，标记当前检测条件
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck check(String markId) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list == null || list.size() == 0) {
            Logger.Debug("The judgment condition does not exist. markId : " + markId);
            return this;
        }

        for (BaseFieldCheckNode node : list) {
            isError = node.isHasError(mObject, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }

    /**
     * 发起检测
     *
     * @param markId  : 标记id，标记当前检测条件
     * @param content : 待检测的内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    @NonNull
    @Override
    public IReallyUiFieldCheck check(String markId, Object content) {
        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> list = mNodeMapper.get(markId);
        if (list != null && list.size() > 0) {
            for (BaseFieldCheckNode node : list) {
                isError = node.isHasError(mObject, mCallback);
                if (isError) {
                    break;
                }
            }
        }

        if (isError) {
            return this;
        }

        List<BaseFieldCheckNode> listWithContent = mNodeMapperWithContent.get(markId);
        if (listWithContent == null || listWithContent.size() == 0) {
            return this;
        }

        for (BaseFieldCheckNode node : listWithContent) {
            isError = node.isHasErrorWithContent(content, mCallback);
            if (isError) {
                break;
            }
        }

        return this;
    }
}
