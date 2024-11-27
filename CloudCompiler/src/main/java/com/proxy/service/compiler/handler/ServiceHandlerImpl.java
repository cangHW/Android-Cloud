package com.proxy.service.compiler.handler;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.compiler.node.NodeService;
import com.proxy.service.consts.ClassConstants;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author cangHX
 * on 2020/06/05  12:12
 */
public class ServiceHandlerImpl extends AbstractHandler {

    /**
     * 服务节点列表
     */
    private final ArrayList<NodeService> mServiceNodes = new ArrayList<>();
    /**
     * 模块名称
     */
    private String mModuleName;

    /**
     * 设置模块名称
     *
     * @param moduleName 模块名称
     * @return 当前对象
     */
    public ServiceHandlerImpl setModuleName(String moduleName) {
        this.mModuleName = moduleName;
        return this;
    }

    /**
     * 当前 handler 准备执行哪些注解
     *
     * @return 返回注解类型
     * @version 1.0
     * @author cangHX
     * 2020-06-30 10:22
     */
    @Override
    public List<String> getSupportedAnnotationTypes() {
        return Collections.singletonList(CloudApiService.class.getCanonicalName());
    }

    /**
     * 处理器执行方法
     *
     * @version 1.0
     * @author cangHX
     * 2020-06-05 14:59
     */
    @Override
    protected void run() {
        Set<? extends Element> elements = mRoundEnvironment.getElementsAnnotatedWith(CloudApiService.class);
        if (elements != null && !elements.isEmpty()) {
            traverseElement(elements);
        }

        boolean isServiceEmpty = mServiceNodes.isEmpty();

        if (isServiceEmpty) {
            return;
        }

        try {
            createClass();
        } catch (IOException e) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    private void traverseElement(Set<? extends Element> elements) {
        for (Element element : elements) {
            CloudApiService cloudApiService = element.getAnnotation(CloudApiService.class);
            CloudApiNewInstance cloudApiNewInstance = element.getAnnotation(CloudApiNewInstance.class);
            TypeElement typeElement = (TypeElement) element;
            String value = typeElement.getQualifiedName().toString();
            mServiceNodes.add(NodeService.create(cloudApiService.serviceTag(), cloudApiNewInstance != null, value));
        }
    }

    private void createClass() throws IOException {
        ClassName className = ClassName.get(mElements.getTypeElement(ClassConstants.SUPPER_CLASS_PATH));
        TypeSpec.Builder builder = TypeSpec.classBuilder(ClassConstants.CLASS_PREFIX + mModuleName);
        builder.addModifiers(Modifier.PUBLIC).superclass(className);
        builder.addMethod(createMethodSpecGetServices());
        JavaFile.builder(ClassConstants.PACKAGE_SERVICES_CACHE, builder.build()).build().writeTo(mFiler);
    }

    private MethodSpec createMethodSpecGetServices() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(ClassConstants.SUPPER_CLASS_METHOD_NAME);

        ClassName typeName = ClassName.get(mElements.getTypeElement(ClassConstants.PARAM_SERVICE_NODE_CLASS_PATH));
        ParameterizedTypeName returnTypeName = ParameterizedTypeName.get(ClassName.get(ArrayList.class), typeName);
        builder.addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(returnTypeName);
        builder.addStatement("$T list = new $T()", returnTypeName, returnTypeName);
        for (NodeService node : mServiceNodes) {
            TypeElement element = mElements.getTypeElement(node.classPath);
            if (element != null) {
                ClassName className = ClassName.get(element);
                try {
                    String format = "list.add(new $T($S,$L,new $T()))";
                    builder.addStatement(format, typeName, node.serviceTag, node.isNewInstance, className);
                } catch (Throwable throwable) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR, "Are you sure " + node.classPath + " inherits BaseService?");
                }
            }
        }
        builder.addStatement("return list");
        return builder.build();
    }
}
