package com.proxy.service.compiler.handler;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudUiTabHostReward;
import com.proxy.service.compiler.node.NodeOther;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * @author: cangHX
 * on 2020/06/30  10:04
 */
public class UiTabHostRewardHandlerImpl extends AbstractHandler {

    private final ArrayList<NodeOther> mOtherNodes = new ArrayList<>();

    public ArrayList<NodeOther> getOtherNodes() {
        return mOtherNodes;
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
        List<String> list = new ArrayList<>();
        for (Class<? extends Annotation> aClass : getAnnotations()) {
            list.add(aClass.getCanonicalName());
        }
        return list;
    }

    private List<Class<? extends Annotation>> getAnnotations() {
        List<Class<? extends Annotation>> list = new ArrayList<>();

        list.add(CloudUiTabHostReward.class);

        return list;
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
        for (Class<? extends Annotation> aClass : getAnnotations()) {
            Set<? extends Element> elements = mRoundEnvironment.getElementsAnnotatedWith(aClass);
            if (elements == null || elements.isEmpty()) {
                return;
            }
            traverseElement(elements);
        }
    }

    private void traverseElement(Set<? extends Element> elements) {
        for (Element element : elements) {
            CloudApiNewInstance cloudApiNewInstance = element.getAnnotation(CloudApiNewInstance.class);
            TypeElement typeElement = (TypeElement) element;
            String value = typeElement.getQualifiedName().toString();
            mOtherNodes.add(NodeOther.create(cloudApiNewInstance != null, value));
        }
    }

}
