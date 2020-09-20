```
    Android 微服务架构，具有无限扩展性，特点：兼容、解耦
```
<br/>
<br/>

# UI 库文档
本文档主要介绍 UI 库中包含的功能以及用法，关于如何获取服务，请看框架整体文档

## 一、服务
|| 服务 | tag | 说明 |
| :--: | :-- | :-- | :-- |
| 1 | CloudUiFieldCheckService | CloudServiceTagUi.UI_FIELD_CHECK | 主要用于数据检测 |
| 2 | CloudUiTabHostService | CloudServiceTagUi.UI_TAB_HOST | 主要用于 tab 切换页面 |

## 二、介绍
1、CloudUiFieldCheckService   主要用于数据校验
| 方法 | tag | 说明 |
| :-- | :-- | :-- |
| init | object：申请检测的类对象  | 初始化，整理当前类所需要检测的数据 |
| setGlobalErrorToastCallback | callback：检查失败回调  | 设置全局检查失败回调，注意 context 泄漏 |
| setErrorToastCallback | object：申请检测的类对象  | 设置本次检查失败回调 |
| check | markId：标记id，标记检测条件  | 发起检测 |
| runMain | runnable：检测成功后执行 | 检测成功执行，主线程 |
| runWork | runnable：检测成功后执行 | 检测成功执行，子线程 |
<br/>

2、CloudUiTabHostService    主要用于 tab 切换页面
| 方法 | tag | 说明 |
| :-- | :-- | :-- |
| setActivity | fragmentActivity：当前依赖的 Activity | 设置依赖的 Activity |
| setFragment | fragment：当前依赖的 Fragment | 设置依赖的 Fragment |
| setContentSpace | viewGroup：用于展示内容  | 设置内容区域 |
| setTabSpace | viewGroup：用于展示tab | 设置tab区域 |
| addEventCallback | cloudUiEventCallback：用于接收事件回调的监听 | 添加事件回调 |
| setSelect | tabIndex：tab 的 index | 设置选中的tab |
| refresh |  | 刷新数据，针对某些特殊情况，例如：viewpager |
| show |  | 开始展示ui |
| showWithTag | rewardTag：用于筛选将要进行展示的ui | 开始展示ui |











