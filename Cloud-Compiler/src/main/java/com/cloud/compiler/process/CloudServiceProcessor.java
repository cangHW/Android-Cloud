package com.cloud.compiler.process;

import com.cloud.annotations.CloudService;
import com.cloud.base.consts.ParamsConstants;
import com.cloud.compiler.handler.CloudServiceHandlerImpl;
import com.cloud.compiler.handler.AbstractHandler;

import java.util.Collections;
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
 * @author: cangHX
 * on 2020/06/05  11:19
 */
public class CloudServiceProcessor extends AbstractProcessor {

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
            mModuleName = options.getOrDefault(ParamsConstants.CLOUD_MODULE_NAME, DEFAULT_NAME);
        }
        if (DEFAULT_NAME.equals(mModuleName)) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "Did you set the \"CLOUD_MODULE_NAME\" on gradle?");
        }
        mModuleName = mModuleName.replace("-", "");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(CloudService.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.singleton(ParamsConstants.CLOUD_MODULE_NAME);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.isEmpty()) {
            return false;
        }
        try {
            AbstractHandler.create(CloudServiceHandlerImpl.class).setModuleName(mModuleName).setRoundEnvironment(roundEnvironment).execute(mProcessingEnvironment);
        } catch (Throwable throwable) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, throwable.getMessage());
        }
        return false;
    }

}
