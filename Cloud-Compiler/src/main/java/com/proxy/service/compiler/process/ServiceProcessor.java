package com.proxy.service.compiler.process;

import com.proxy.service.compiler.handler.ServiceHandlerImpl;
import com.proxy.service.compiler.handler.AbstractHandler;
import com.proxy.service.compiler.handler.UiTabHostRewardHandlerImpl;
import com.proxy.service.consts.ClassConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author cangHX
 * on 2020/06/05  11:19
 */
public class ServiceProcessor extends AbstractProcessor {

    private static final String DEFAULT_NAME = "-1";

    private ProcessingEnvironment mProcessingEnvironment;
    private Messager mMessager;
    private String mModuleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mProcessingEnvironment = processingEnvironment;
        mMessager = processingEnvironment.getMessager();
        Map<String, String> options = processingEnvironment.getOptions();
        if (options != null) {
            mModuleName = options.getOrDefault(ClassConstants.CLOUD_MODULE_NAME, DEFAULT_NAME);
        }
        if (DEFAULT_NAME.equals(mModuleName)) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "Did you set the \"CLOUD_MODULE_NAME\" on gradle?");
        }
        mModuleName = mModuleName.replace("-", "");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        List<String> list = new ArrayList<>();

        try {
            list.addAll(AbstractHandler.create(UiTabHostRewardHandlerImpl.class).getSupportedAnnotationTypes());
            list.addAll(AbstractHandler.create(ServiceHandlerImpl.class).getSupportedAnnotationTypes());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return new HashSet<>(list);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.singleton(ClassConstants.CLOUD_MODULE_NAME);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.isEmpty()) {
            return false;
        }
        try {
            UiTabHostRewardHandlerImpl cloudUiTabHostRewardHandler = AbstractHandler.create(UiTabHostRewardHandlerImpl.class);
            cloudUiTabHostRewardHandler.setRoundEnvironment(roundEnvironment).execute(mProcessingEnvironment);
            AbstractHandler.create(ServiceHandlerImpl.class).setModuleName(mModuleName).setOtherList(cloudUiTabHostRewardHandler.getOtherNodes()).setRoundEnvironment(roundEnvironment).execute(mProcessingEnvironment);
        } catch (Throwable throwable) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, throwable.getMessage());
        }
        return false;
    }

}
