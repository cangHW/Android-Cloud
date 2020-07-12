package com.proxy.service.ui.fieldcheck;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.fieldcheck.cache.FieldCheckDataCache;
import com.proxy.service.ui.fieldcheck.enums.FieldCheckEnum;
import com.proxy.service.ui.fieldcheck.node.BaseFieldCheckNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/08  10:02
 */
public class FieldCheckDataManager {

    public static List<BaseFieldCheckNode> init(@NonNull Class<?> aClass) {
        List<BaseFieldCheckNode> nodes = FieldCheckDataCache.INSTANCE.get(aClass);
        if (nodes != null) {
            return nodes;
        }
        nodes = new ArrayList<>();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getType().getCanonicalName();

            if (TextUtils.isEmpty(name)) {
                continue;
            }

            switch (name) {
                case "java.lang.String":
                    try {
                        nodes.addAll(FieldCheckEnum.STRING.getNodes(field));
                    } catch (Throwable throwable) {
                        Logger.Error(throwable);
                    }
                    break;
                case "java.lang.Integer":
                case "int":
                case "java.lang.Double":
                case "double":
                case "java.lang.Float":
                case "float":
                case "java.lang.Long":
                case "long":
                    try {
                        nodes.addAll(FieldCheckEnum.NUMBER.getNodes(field));
                    } catch (Throwable throwable) {
                        Logger.Error(throwable);
                    }
                    break;
                case "java.lang.Boolean":
                case "boolean":
                    try {
                        nodes.addAll(FieldCheckEnum.BOOLEAN.getNodes(field));
                    } catch (Throwable throwable) {
                        Logger.Error(throwable);
                    }
                    break;
                default:
            }
        }
        FieldCheckDataCache.INSTANCE.put(aClass, nodes);
        return nodes;
    }

}
