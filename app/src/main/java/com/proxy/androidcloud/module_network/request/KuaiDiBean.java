package com.proxy.androidcloud.module_network.request;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/05  6:04 PM
 */
public class KuaiDiBean {

    /**
     * message : ok
     * nu : 390011492112
     * ischeck : 1
     * com : quanfengkuaidi
     * status : 200
     * condition : F00
     * state : 3
     * data : [{"time":"2020-08-14 16:15:45","context":"查无结果","ftime":"2020-08-14 16:15:45"}]
     */

    private String message = "";
    private String nu;
    private String ischeck;
    private String com;
    private String status;
    private String condition;
    private String state;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<DataBean> getData() {
        if (data == null){
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * time : 2020-08-14 16:15:45
         * context : 查无结果
         * ftime : 2020-08-14 16:15:45
         */

        private String time;
        private String context;
        private String ftime;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }
    }
}
