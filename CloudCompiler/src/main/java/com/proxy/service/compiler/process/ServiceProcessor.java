package com.proxy.service.compiler.process;

import com.proxy.service.compiler.handler.ServiceHandlerImpl;
import com.proxy.service.compiler.handler.AbstractHandler;
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

    private Messager mMessager;
    private AbstractHandler serviceHandler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();

        String moduleName = DEFAULT_NAME;
        Map<String, String> options = processingEnvironment.getOptions();
        if (options != null) {
            moduleName = options.getOrDefault(ClassConstants.CLOUD_MODULE_NAME, DEFAULT_NAME);
        }
        if (DEFAULT_NAME.equals(moduleName)) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "Did you set the \"CLOUD_MODULE_NAME\" on gradle?");
        }
        moduleName = moduleName.replace("-", "");

        try {
            serviceHandler = AbstractHandler.create(ServiceHandlerImpl.class)
                    .setModuleName(moduleName)
                    .setRoundEnvironment(processingEnvironment);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        List<String> list = new ArrayList<>();
        list.addAll(serviceHandler.getSupportedAnnotationTypes());
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
        try {
            serviceHandler.execute(roundEnvironment);
        } catch (Throwable throwable) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, throwable.getMessage());
        }
        return true;
    }

}
