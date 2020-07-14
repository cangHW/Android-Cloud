package com.proxy.service.compiler.handler;

import com.proxy.service.annotations.CloudNewInstance;
import com.proxy.service.annotations.CloudService;
import com.proxy.service.compiler.node.NodeOther;
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
 * @author: cangHX
 * on 2020/06/05  12:12
 */
public class ServiceHandlerImpl extends AbstractHandler {

    private final ArrayList<NodeService> mServiceNodes = new ArrayList<>();
    private final ArrayList<NodeOther> mOtherNodes = new ArrayList<>();

    private String mModuleName;

    public ServiceHandlerImpl setModuleName(String moduleName) {
        this.mModuleName = moduleName;
        return this;
    }

    public ServiceHandlerImpl setOtherList(ArrayList<NodeOther> nodes) {
        mOtherNodes.clear();
        mOtherNodes.addAll(nodes);
        return this;
    }

    /**
     * 当前 handler 准备执行哪些注解
     *
     * @return 返回注解类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 10:22
     */
    @Override
    public List<String> getSupportedAnnotationTypes() {
        return Collections.singletonList(CloudService.class.getCanonicalName());
    }

    /**
     * 处理器执行方法
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-05 14:59
     */
    @Override
    protected void run() {
        Set<? extends Element> elements = mRoundEnvironment.getElementsAnnotatedWith(CloudService.class);
        if (elements != null && elements.size() > 0) {
            traverseElement(elements);
        }

        boolean isServiceEmpty = mServiceNodes == null || mServiceNodes.isEmpty();
        boolean isOtherEmpty = mOtherNodes == null || mOtherNodes.isEmpty();

        if (isServiceEmpty && isOtherEmpty) {
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
            CloudService cloudService = element.getAnnotation(CloudService.class);
            CloudNewInstance cloudNewInstance = element.getAnnotation(CloudNewInstance.class);
            TypeElement typeElement = (TypeElement) element;
            String value = typeElement.getQualifiedName().toString();
            mServiceNodes.add(NodeService.create(cloudService.serviceTag(), cloudNewInstance != null, value));
        }
    }

    private void createClass() throws IOException {
        ClassName className = ClassName.get(mElements.getTypeElement(ClassConstants.SUPPER_CLASS_PATH));
        TypeSpec.Builder builder = TypeSpec.classBuilder(ClassConstants.CLASS_PREFIX + mModuleName);
        builder.addModifiers(Modifier.PUBLIC).superclass(className);
        builder.addMethod(createMethodSpecGetServices());
        builder.addMethod(createMethodSpecGetOthers());
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

    private MethodSpec createMethodSpecGetOthers() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(ClassConstants.SUPPER_CLASS_METHOD_NAME_2);

        ClassName typeName = ClassName.get(mElements.getTypeElement(ClassConstants.PARAM_OTHER_NODE_CLASS_PATH));
        ParameterizedTypeName returnTypeName = ParameterizedTypeName.get(ClassName.get(ArrayList.class), typeName);
        builder.addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(returnTypeName);
        builder.addStatement("$T list = new $T()", returnTypeName, returnTypeName);
        for (NodeOther node : mOtherNodes) {
            TypeElement element = mElements.getTypeElement(node.classPath);
            if (element != null) {
                ClassName className = ClassName.get(element);
                try {
                    String format = "list.add(new $T($L,new $T()))";
                    builder.addStatement(format, typeName, node.isNewInstance, className);
                } catch (Throwable throwable) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR, "Are you sure " + node.classPath + " is has?");
                }
            }
        }
        builder.addStatement("return list");
        return builder.build();
    }
}
