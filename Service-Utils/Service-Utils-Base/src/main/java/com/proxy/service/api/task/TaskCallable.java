package com.proxy.service.api.task;

/**
 * @author : cangHX
 * on 2020/08/09  2:50 PM
 */
public interface TaskCallable<RESPONSE, RESULT> {

    /**
     * 后续执行
     *
     * @param iTasks : 任务对象数组
     * @return 当前任务返回值
     * @throws Exception : 执行中的异常信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:11 PM
     */
    RESULT then(ITask<RESPONSE>[] iTasks) throws Exception;

}
