package com.proxy.service.compiler.handler;

import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author cangHX
 * on 2020/06/05  12:03
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractHandler {

    /**
     * 节点元素
     */
    protected RoundEnvironment mRoundEnvironment;
    /**
     * 日志
     */
    protected Messager mMessager;
    /**
     * 节点元素
     */
    protected Elements mElements;
    /**
     * 写
     */
    protected Filer mFiler;

    /**
     * 创建处理器
     *
     * @param tClass 处理器class
     * @param <T>    处理器类
     * @return 处理器对象
     * @throws Throwable 创建对象失败
     */
    public static <T extends AbstractHandler> T create(Class<T> tClass) throws Throwable {
        return tClass.getConstructor().newInstance();
    }

    /**
     * 设置 RoundEnvironment
     *
     * @param roundEnvironment RoundEnvironment
     * @return 当前对象
     */
    public AbstractHandler setRoundEnvironment(RoundEnvironment roundEnvironment) {
        this.mRoundEnvironment = roundEnvironment;
        return this;
    }

    /**
     * 执行
     *
     * @param processingEnvironment processingEnvironment
     */
    public void execute(ProcessingEnvironment processingEnvironment) {
        mFiler = processingEnvironment.getFiler();
        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();

        check();
        try {
            run();
        } catch (Throwable throwable) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, throwable.getMessage());
        }

    }

    /**
     * 检查
     */
    protected void check() {
        if (mRoundEnvironment == null) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "Did you set the \"RoundEnvironment\" on " + this.getClass().getSimpleName() + "?");
        }
    }

    /**
     * 当前 handler 准备执行哪些注解
     *
     * @return 返回注解类型
     * @version 1.0
     * @author cangHX
     * 2020-06-30 10:22
     */
    public abstract List<String> getSupportedAnnotationTypes();

    /**
     * 处理器执行方法
     *
     * @version 1.0
     * @author cangHX
     * 2020-06-05 14:59
     */
    protected abstract void run();
}
