package com.cloud.compiler.handler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author: cangHX
 * on 2020/06/05  12:03
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractHandler {

    protected RoundEnvironment mRoundEnvironment;
    protected Messager mMessager;
    protected Elements mElements;
    protected Filer mFiler;

    public static <T extends AbstractHandler> T create(Class<T> tClass) throws Throwable {
        return tClass.getConstructor().newInstance();
    }

    public AbstractHandler setRoundEnvironment(RoundEnvironment roundEnvironment) {
        this.mRoundEnvironment = roundEnvironment;
        return this;
    }

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

    protected void check() {
        if (mRoundEnvironment == null) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "Did you set the \"RoundEnvironment\" on " + this.getClass().getSimpleName() + "?");
        }
    }

    /**
     * 处理器执行方法
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-05 14:59
     */
    protected abstract void run();
}
