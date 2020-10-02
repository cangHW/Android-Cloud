package com.proxy.service.api.receiver;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2020/09/29  11:37 PM
 */
public class CloudReceiverInfo {
    private static final AtomicInteger INTEGER = new AtomicInteger();

    private final int id;
    private final String action;
    private final ArrayList<String> categories;

    private CloudReceiverInfo(Builder builder) {
        this.id = INTEGER.incrementAndGet();
        this.action = builder.action;
        this.categories = builder.categories;
    }

    public int getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String action;
        private ArrayList<String> categories = new ArrayList<>();

        private Builder() {
        }

        public Builder setAction(String action) {
            if (action == null) {
                action = "";
            }
            this.action = action;
            return this;
        }

        public Builder addCategory(String category) {
            if (!categories.contains(category)) {
                this.categories.add(category.intern());
            }
            return this;
        }

        public CloudReceiverInfo build() {
            return new CloudReceiverInfo(this);
        }
    }
}
