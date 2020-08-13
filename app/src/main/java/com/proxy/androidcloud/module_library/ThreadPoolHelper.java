package com.proxy.androidcloud.module_library;

import android.content.Context;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.OneTaskCallable;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.task.TaskCallable;
import com.proxy.service.api.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : cangHX
 * on 2020/08/13  10:00 PM
 */
public class ThreadPoolHelper extends AbstractHelper {

    private Logger logger = Logger.create("thread-pool");
    private CloudUtilsTaskService service;

    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> list = new ArrayList<>();
        list.add(HelperItemInfo.builder()
                .setId(0)
                .setTitle("当前线程任务")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(1)
                .setTitle("后台线程任务")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(2)
                .setTitle("主线程任务")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(3)
                .setTitle("定时器——串行任务")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(4)
                .setTitle("计时器——循环任务")
                .setLeftButton("开始")
                .setRightButton("结束")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(5)
                .setTitle("并行任务")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(6)
                .setTitle("更多高级用法")
                .build());
        return list;
    }

    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (service == null) {
            service = CloudSystem.getService(CloudUtilsTaskService.class);
        }
        if (service == null) {
            return;
        }
        switch (itemInfo.id) {
            case 0:
                currentThread(context);
                break;
            case 1:
                workThread(context);
                break;
            case 2:
                mainThread(context);
                break;
            case 3:
                timerTask(context);
                break;
            case 4:
                continueTask(context, button);
                break;
            case 5:
                parallel(context);
                break;
            case 6:
                Toast.makeText(context, "通过组合，可以完成各种各样复杂的串行、并行都存在的任务流，以及随时切换主、子线程", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }

    private AtomicBoolean doubleClick = new AtomicBoolean(false);

    public void currentThread(Context context) {
        service.call(new Task<Object>() {
            @Override
            public Object call() throws Exception {
                logger.debug("当前线程");
                service.callUiThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Toast.makeText(context, "当前线程", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
                return null;
            }
        });
    }

    public void workThread(Context context) {
        service.callWorkThread(new Task<Object>() {
            @Override
            public Object call() throws Exception {
                logger.debug("工作线程");
                service.callUiThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Toast.makeText(context, "工作线程", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
                return null;
            }
        });
    }

    public void mainThread(Context context) {
        service.callUiThread(new Task<Object>() {
            @Override
            public Object call() throws Exception {
                logger.debug("主线程");
                Toast.makeText(context, "主线程", Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    public void timerTask(Context context) {
        if (!doubleClick.compareAndSet(false, true)) {
            Toast.makeText(context, "正在执行中...", Toast.LENGTH_SHORT).show();
            return;
        }

        long time = System.currentTimeMillis();
        logger.debug(time + "");

        Toast.makeText(context, "计时3秒", Toast.LENGTH_SHORT).show();
        service.delay(3000, TimeUnit.MILLISECONDS)
                .mainThread()
                .call(new OneTaskCallable<Object, String>() {
                    @Override
                    public String then(ITask<Object> iTasks) throws Exception {
                        logger.debug(System.currentTimeMillis() - time + "");
                        Toast.makeText(context, "3秒时间到", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                })
                .workThread()
                .call(new OneTaskCallable<String, Integer>() {
                    @Override
                    public Integer then(ITask<String> iTasks) throws Exception {
                        logger.debug("当前线程 : " + Thread.currentThread().getName() + " : " + Thread.currentThread().getId());
                        doubleClick.set(false);
                        return null;
                    }
                });
    }

    public void continueTask(Context context, int button) {
        if (button == HelperItemInfo.BUTTON_RIGHT) {
            service.cancel();
            doubleClick.set(false);
            return;
        } else if (!doubleClick.compareAndSet(false, true)) {
            Toast.makeText(context, "正在执行中...", Toast.LENGTH_SHORT).show();
            return;
        }

        long time = System.currentTimeMillis();
        Toast.makeText(context, "开始循环任务", Toast.LENGTH_SHORT).show();
        service.workThread()
                .continueWhile(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Thread.sleep(1000);
                        return true;
                    }
                }, new TaskCallable<Object, String>() {
                    @Override
                    public String then(ITask<Object>[] iTasks) throws Exception {
                        long timeDiff = System.currentTimeMillis() - time;
                        service.callUiThread(new Task<Object>() {
                            @Override
                            public Object call() throws Exception {
                                logger.debug("timeDiff : " + timeDiff);
                                Toast.makeText(context, timeDiff / 1000 + " 秒", Toast.LENGTH_SHORT).show();
                                return null;
                            }
                        });
                        return null;
                    }
                });
    }

    public void parallel(Context context) {
        service.whenAll(
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(1000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, Object>() {
                    @Override
                    public Object then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务一执行完毕");
                        Toast.makeText(context, "任务一执行完毕", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }),
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(2000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, Object>() {
                    @Override
                    public Object then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务二执行完毕");
                        Toast.makeText(context, "任务二执行完毕", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }),
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(3000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, Object>() {
                    @Override
                    public Object then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务三执行完毕");
                        Toast.makeText(context, "任务三执行完毕", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }),
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(4000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, Object>() {
                    @Override
                    public Object then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务四执行完毕");
                        Toast.makeText(context, "任务四执行完毕", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                })
        )
                .workThread()
                .call(new TaskCallable<Object, Object>() {
                    @Override
                    public Object then(ITask<Object>[] iTasks) throws Exception {
                        Thread.sleep(1000);
                        return null;
                    }
                })
                .mainThread()
                .call(new TaskCallable<Object, Object>() {
                    @Override
                    public Object then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("四个任务全部执行完毕");
                        Toast.makeText(context, "四个任务全部执行完毕", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
    }
}
