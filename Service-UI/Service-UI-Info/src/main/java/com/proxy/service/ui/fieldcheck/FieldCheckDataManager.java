package com.proxy.service.ui.fieldcheck;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.fieldcheck.enums.FieldCheckEnum;
import com.proxy.service.ui.fieldcheck.node.BaseFieldCheckNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: cangHX
 * on 2020/07/08  10:02
 */
public class FieldCheckDataManager {

    private static final Map<Class<?>, List<BaseFieldCheckNode>> NODE_MAPPER = new HashMap<>();

    public static void init(@NonNull Class<?> aClass) {
        List<BaseFieldCheckNode> nodes = NODE_MAPPER.get(aClass);
        if (nodes != null) {
            return;
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
        NODE_MAPPER.put(aClass, nodes);
    }

    @NonNull
    public static List<BaseFieldCheckNode> get(@NonNull Class<?> aClass) {
        List<BaseFieldCheckNode> list = new ArrayList<>();

        return list;
    }


}
