package com.proxy.service.ui.fieldcheck.enums;

import android.content.Context;
import android.text.TextUtils;

import com.proxy.service.api.annotations.CloudUiCheckBoolean;
import com.proxy.service.api.annotations.CloudUiCheckBooleans;
import com.proxy.service.api.annotations.CloudUiCheckNumber;
import com.proxy.service.api.annotations.CloudUiCheckNumbers;
import com.proxy.service.api.annotations.CloudUiCheckString;
import com.proxy.service.api.annotations.CloudUiCheckStrings;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.fieldcheck.node.BaseFieldCheckNode;
import com.proxy.service.ui.fieldcheck.node.BooleanFieldCheckNode;
import com.proxy.service.ui.fieldcheck.node.NumberFieldCheckNode;
import com.proxy.service.ui.fieldcheck.node.StringFieldCheckNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/08  14:39
 */
@SuppressWarnings("unchecked")
public enum FieldCheckEnum {

    /**
     * 检测数字类型注解
     */
    NUMBER() {
        @Override
        public List<NumberFieldCheckNode> getNodes(Field field) {
            List<NumberFieldCheckNode> list = new ArrayList<>();

            if (field == null) {
                return list;
            }

            try {
                CloudUiCheckNumber checkNumber = field.getAnnotation(CloudUiCheckNumber.class);
                if (checkNumber != null) {
                    list.add(getNodeFromAnnotation(checkNumber));
                }
            } catch (Throwable ignored) {
            }

            try {
                CloudUiCheckNumbers checkNumbers = field.getAnnotation(CloudUiCheckNumbers.class);
                if (checkNumbers == null) {
                    return list;
                }
                for (CloudUiCheckNumber checkNumber : checkNumbers.value()) {
                    list.add(getNodeFromAnnotation(checkNumber));
                }
            } catch (Throwable ignored) {
            }

            return list;
        }

        private NumberFieldCheckNode getNodeFromAnnotation(CloudUiCheckNumber checkNumber) {
            NumberFieldCheckNode node = new NumberFieldCheckNode();
            node.markId = checkNumber.markId();
            node.max = checkNumber.max();
            node.maxNotSame = checkNumber.maxNotSame();
            node.min = checkNumber.min();
            node.minNotSame = checkNumber.minNotSame();
            node.scale = checkNumber.scale();
            if (!TextUtils.isEmpty(checkNumber.message())) {
                node.message = checkNumber.message();
                return node;
            }
            int stringId = checkNumber.stringId();
            Context context = ContextManager.getApplication();
            if (context == null) {
                Logger.Error(CloudApiError.NO_INIT.build());
                node.message = "";
                return node;
            }
            node.message = context.getString(stringId);
            return node;
        }
    },

    /**
     * 检测字符串类型注解
     */
    STRING() {
        @Override
        public List<StringFieldCheckNode> getNodes(Field field) {
            List<StringFieldCheckNode> list = new ArrayList<>();

            if (field == null) {
                return list;
            }

            try {
                CloudUiCheckString checkString = field.getAnnotation(CloudUiCheckString.class);
                if (checkString != null) {
                    list.add(getNodeFromAnnotation(checkString));
                }
            } catch (Throwable ignored) {
            }

            try {
                CloudUiCheckStrings checkStrings = field.getAnnotation(CloudUiCheckStrings.class);
                if (checkStrings == null) {
                    return list;
                }
                for (CloudUiCheckString checkString : checkStrings.value()) {
                    list.add(getNodeFromAnnotation(checkString));
                }
            } catch (Throwable ignored) {
            }

            return list;
        }

        private StringFieldCheckNode getNodeFromAnnotation(CloudUiCheckString checkString) {
            StringFieldCheckNode node = new StringFieldCheckNode();
            node.markId = checkString.markId();
            node.maxLength = checkString.maxLength();
            node.maxLengthNotSame = checkString.maxLengthNotSame();
            node.minLength = checkString.minLength();
            node.minLengthNotSame = checkString.minLengthNotSame();
            node.notEmpty = checkString.notEmpty();
            node.notBlank = checkString.notBlank();
            if (!TextUtils.isEmpty(checkString.message())) {
                node.message = checkString.message();
                return node;
            }
            int stringId = checkString.stringId();
            Context context = ContextManager.getApplication();
            if (context == null) {
                Logger.Error(CloudApiError.NO_INIT.build());
                node.message = "";
                return node;
            }
            node.message = context.getString(stringId);
            return node;
        }
    },

    /**
     * 检测布尔类型注解
     */
    BOOLEAN() {
        @Override
        public List<BooleanFieldCheckNode> getNodes(Field field) {
            List<BooleanFieldCheckNode> list = new ArrayList<>();

            if (field == null) {
                return list;
            }

            try {
                CloudUiCheckBoolean checkBoolean = field.getAnnotation(CloudUiCheckBoolean.class);
                if (checkBoolean != null) {
                    list.add(getNodeFromAnnotation(checkBoolean));
                }
            } catch (Throwable ignored) {
            }

            try {
                CloudUiCheckBooleans checkBooleans = field.getAnnotation(CloudUiCheckBooleans.class);
                if (checkBooleans == null) {
                    return list;
                }
                for (CloudUiCheckBoolean checkBoolean : checkBooleans.value()) {
                    list.add(getNodeFromAnnotation(checkBoolean));
                }
            } catch (Throwable ignored) {
            }

            return list;
        }

        private BooleanFieldCheckNode getNodeFromAnnotation(CloudUiCheckBoolean checkBoolean) {
            BooleanFieldCheckNode node = new BooleanFieldCheckNode();
            node.markId = checkBoolean.markId();
            node.isValue = checkBoolean.isValue();
            if (!TextUtils.isEmpty(checkBoolean.message())) {
                node.message = checkBoolean.message();
                return node;
            }
            int stringId = checkBoolean.stringId();
            Context context = ContextManager.getApplication();
            if (context == null) {
                Logger.Error(CloudApiError.NO_INIT.build());
                node.message = "";
                return node;
            }
            node.message = context.getString(stringId);
            return node;
        }
    };


    public <T extends BaseFieldCheckNode> List<T> getNodes(Field field) {
        return new ArrayList<>();
    }
}
