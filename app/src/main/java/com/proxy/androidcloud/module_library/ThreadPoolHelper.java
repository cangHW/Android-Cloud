package com.proxy.androidcloud.module_library;

import android.content.Context;
import android.widget.Toast;

import com.proxy.androidcloud.helper.AbstractHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.TaskCallableOnce;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.task.TaskCallable;
import com.proxy.service.api.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2020/08/13  10:00 PM
 */
public class ThreadPoolHelper extends AbstractHelper {

    private Logger logger = Logger.create("thread-pool");
    private CloudUtilsTaskService service;

    public ThreadPoolHelper() {
        service = CloudSystem.getService(CloudUtilsTaskService.class);
    }

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
                .setTitle("当前线程循环任务")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(6)
                .setTitle("并行任务")
                .build());
        list.add(HelperItemInfo.builder()
                .setId(7)
                .setTitle("更多高级用法")
                .build());
        return list;
    }

    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
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
                continueTask();
                break;
            case 6:
                parallel(context);
                break;
            case 7:
                Toast.makeText(context, "通过组合，可以完成各种各样复杂的串行、并行都存在的任务流，以及随时切换主、子线程", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }

    private AtomicBoolean doubleClick = new AtomicBoolean(false);

    public void currentThread(Context context) {
        service.call(new Task<String>() {
            @Override
            public String call() throws Exception {
                logger.debug("当前线程");
                service.callUiThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Toast.makeText(context, "主线程吐司", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
                return "后续任务";
            }
        }).call(new TaskCallableOnce<String, Object>() {
            @Override
            public Object then(ITask<String> iTask) throws Exception {
                logger.debug(iTask.getResponse());
                return null;
            }
        });
    }

    public void workThread(Context context) {
        service.callWorkThread(new Task<String>() {
            @Override
            public String call() throws Exception {
                logger.debug("工作线程");
                service.callUiThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Toast.makeText(context, "工作线程", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
                return "asd";
            }
        }).call(new TaskCallableOnce<String, Object>() {
            @Override
            public Object then(ITask<String> iTask) throws Exception {
                logger.debug(iTask.getResponse());
                return null;
            }
        });
    }

    public void mainThread(Context context) {
        service.callUiThread(new Task<String>() {
            @Override
            public String call() throws Exception {
                logger.debug("主线程");
                Toast.makeText(context, "主线程", Toast.LENGTH_SHORT).show();
                return "qwe";
            }
        }).call(new TaskCallableOnce<String, Object>() {
            @Override
            public Object then(ITask<String> iTask) throws Exception {
                logger.debug(iTask.getResponse());
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
                .call(new TaskCallableOnce<Object, String>() {
                    @Override
                    public String then(ITask<Object> iTasks) throws Exception {
                        logger.debug(System.currentTimeMillis() - time + "");
                        Toast.makeText(context, "3秒时间到", Toast.LENGTH_SHORT).show();
                        return "time";
                    }
                })
                .workThread()
                .call(new TaskCallableOnce<String, Integer>() {
                    @Override
                    public Integer then(ITask<String> iTask) throws Exception {
                        logger.debug(iTask.getResponse());
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

        AtomicInteger count = new AtomicInteger(0);
        Toast.makeText(context, "开始循环任务", Toast.LENGTH_SHORT).show();
        service.workThread()
                .continueWhile(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Thread.sleep(1000);
                        return count.incrementAndGet() <= 10;
                    }
                }, new TaskCallable<Object, String>() {
                    @Override
                    public String then(ITask<Object>[] iTasks) throws Exception {
                        service.callUiThread(new Task<Object>() {
                            @Override
                            public Object call() throws Exception {
                                logger.debug("timeDiff : " + count.get());
                                Toast.makeText(context, count.get() + " 秒", Toast.LENGTH_SHORT).show();
                                return null;
                            }
                        });
                        return count.get() + "";
                    }
                })
                .call(new TaskCallable<String, Object>() {
                    @Override
                    public Object then(ITask<String>[] iTasks) throws Exception {
                        for (ITask<String> iTask : iTasks) {
                            logger.debug(iTask.getResponse());
                        }
                        doubleClick.set(false);
                        return null;
                    }
                });
    }

    public void continueTask() {
        AtomicInteger count = new AtomicInteger(0);
        logger.debug("开始循环任务");
        service.continueWhile(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Thread.sleep(200);
                return count.incrementAndGet() <= 10;
            }
        }, new Task<Object>() {
            @Override
            public Object call() throws Exception {
                logger.debug("count : " + count.get());
                return null;
            }
        });
        logger.debug("结束任务");
    }

    public void parallel(Context context) {
        if (!doubleClick.compareAndSet(false, true)) {
            Toast.makeText(context, "正在执行中...", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(context, "开始并行四个任务，串行两个任务", Toast.LENGTH_SHORT).show();
        service.whenAll(
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(1000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, String>() {
                    @Override
                    public String then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务一执行完毕");
                        Toast.makeText(context, "任务一执行完毕", Toast.LENGTH_SHORT).show();
                        return "一";
                    }
                }),
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(2000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, String>() {
                    @Override
                    public String then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务二执行完毕");
                        Toast.makeText(context, "任务二执行完毕", Toast.LENGTH_SHORT).show();
                        return "二";
                    }
                }),
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(3000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, String>() {
                    @Override
                    public String then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务三执行完毕");
                        Toast.makeText(context, "任务三执行完毕", Toast.LENGTH_SHORT).show();
                        return "三";
                    }
                }),
                service.callWorkThread(new Task<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(4000);
                        return null;
                    }
                }).mainThread().call(new TaskCallable<Object, String>() {
                    @Override
                    public String then(ITask<Object>[] iTasks) throws Exception {
                        logger.debug("任务四执行完毕");
                        Toast.makeText(context, "任务四执行完毕", Toast.LENGTH_SHORT).show();
                        return "四";
                    }
                })
        ).workThread().call(new TaskCallable<Object, String>() {
            @Override
            public String then(ITask<Object>[] iTasks) throws Exception {
                for (ITask<Object> iTask : iTasks) {
                    logger.debug((String) iTask.getResponse());
                }
                Thread.sleep(1000);
                return null;
            }
        }).mainThread().call(new TaskCallable<String, Object>() {
            @Override
            public Object then(ITask<String>[] iTasks) throws Exception {
                logger.debug("四个任务全部执行完毕");
                Toast.makeText(context, "四个任务全部执行完毕", Toast.LENGTH_SHORT).show();
                doubleClick.set(false);
                return null;
            }
        });
    }
}
