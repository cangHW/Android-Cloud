package com.proxy.service.compiler.handler;

import com.cloud.annotations.CloudNewInstance;
import com.cloud.annotations.CloudService;
import com.proxy.service.consts.ClassConstants;
import com.proxy.service.node.BaseNode;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author: cangHX
 * on 2020/06/05  12:12
 */
public class CloudServiceHandlerImpl extends AbstractHandler {

    private static class Node extends BaseNode {
        /**
         * 服务类地址
         */
        String classPath;

        private Node(String serviceTag, boolean isNewInstance, String classPath) {
            this.serviceTag = serviceTag;
            this.isNewInstance = isNewInstance;
            this.classPath = classPath;
        }

        static Node create(String serviceTag, boolean isNewInstance, String classPath) {
            return new Node(serviceTag, isNewInstance, classPath);
        }
    }

    private ArrayList<Node> mNodes = new ArrayList<>();

    private String mModuleName;

    public CloudServiceHandlerImpl setModuleName(String moduleName) {
        this.mModuleName = moduleName;
        return this;
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
        if (elements == null || elements.isEmpty()) {
            return;
        }
        traverseElement(elements);
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
            mNodes.add(Node.create(cloudService.serviceTag(), cloudNewInstance != null, value));
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
        for (Node node : mNodes) {
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
