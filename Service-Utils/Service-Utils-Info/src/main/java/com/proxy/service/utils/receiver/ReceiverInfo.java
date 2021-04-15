package com.proxy.service.utils.receiver;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/09/30  10:36 PM
 */
public class ReceiverInfo {

    private int id;
    /**
     * 类型
     */
    private String type;
    /**
     * 开发者设置的 action
     */
    private String action;
    /**
     * 开发者设置的 scheme
     */
    private String scheme;
    /**
     * 权限
     */
    private String permission;
    /**
     * 开发者设置的 categories
     */
    private ArrayList<String> categories;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        if (type == null) {
            type = "";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        if (action == null) {
            action = "";
        }
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getScheme() {
        if (scheme == null) {
            scheme = "";
        }
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getPermission() {
        if (permission == null) {
            permission = "";
        }
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public ArrayList<String> getCategories() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public boolean equals(ReceiverInfo info) {
        if (info == null) {
            return false;
        }
        boolean isPermissionSame = getPermission().equals(info.getPermission());
        boolean isTypeSame = getType().equals(info.getType());
        boolean isCategoriesSame = getCategories().size() == info.getCategories().size() && getCategories().containsAll(info.getCategories());
        boolean isSchemeSame = getScheme().equals(info.getScheme());
        return isPermissionSame && isTypeSame && isSchemeSame && isCategoriesSame;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "action='" + action + '\'' +
                ", permission='" + permission + '\'' +
                ", scheme=" + scheme +
                ", categories=" + categories +
                '}';
    }
}
