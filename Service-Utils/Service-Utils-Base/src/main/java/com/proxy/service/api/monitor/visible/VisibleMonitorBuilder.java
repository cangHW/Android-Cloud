package com.proxy.service.api.monitor.visible;

/**
 * @author : cangHX
 * on 2020/11/11  9:14 PM
 */
public interface VisibleMonitorBuilder {

    /**
     * 设置曝光的有效区域比例（0—1 百分比）,展示多少算一次有效曝光，默认为 0.5
     *
     * @param area : 有效区域比例（0—1 百分比）
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    VisibleMonitorBuilder setArea(float area);

    /**
     * 设置曝光的有效时长（单位：毫秒）,多久算一次有效曝光, 默认为 1000
     *
     * @param duration : 曝光的有效时长
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    VisibleMonitorBuilder setDuration(long duration);

    /**
     * 设置曝光的检测间隔时间（单位：毫秒），时间越短, 灵敏度越高，默认为 500
     *
     * @param delayMillis : 曝光的检测间隔时间
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    VisibleMonitorBuilder setDelayMillis(long delayMillis);

    /**
     * 设置监控的回调接口
     *
     * @param callback : 监控的回调接口
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    VisibleMonitorBuilder setCallback(CloudVisibleMonitorCallback callback);

    /**
     * 创建监控
     *
     * @return 监控辅助器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    VisibleMonitorHelper build();
}
