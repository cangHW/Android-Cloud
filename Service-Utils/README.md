```
    Android 微服务架构，具有无限扩展性，特点：兼容、解耦
```
<br/>
<br/>

# 工具库文档
本文档主要介绍工具库中包含的功能以及用法，关于如何获取服务，请看框架整体文档

## 一、服务

| 类名 | tag | 说明 |
| :-- | :-- | :-- |
| CloudUtilsTaskService | CloudServiceTagUtils.UTILS_TASK | 线程切换相关操作 |
| CloudUtilsAppService | CloudServiceTagUtils.UTILS_APP | 获取当前应用相关信息 |
| CloudUtilsBitmapService | CloudServiceTagUtils.UTILS_BITMAP | bitmap 相关操作 |
| CloudUtilsDisplayService | CloudServiceTagUtils.UTILS_DISPLAY | 获取像素转换相关信息 |
| CloudUtilsEditTextService | CloudServiceTagUtils.UTILS_EDIT_TEXT | 控制输入框相关功能 |
| CloudUtilsFileService | CloudServiceTagUtils.UTILS_FILE | 文件操作相关功能 |
| CloudUtilsInstallService | CloudServiceTagUtils.UTILS_INSTALL | 应用安装相关操作 |
| CloudUtilsNetWorkService | CloudServiceTagUtils.UTILS_NET_WORK | 网络状态相关操作 |
| CloudUtilsPermissionService | CloudServiceTagUtils.UTILS_PERMISSION | 权限管理相关操作 |
| CloudUtilsSecurityService | CloudServiceTagUtils.UTILS_SECURITY | 加解密相关操作 |
| CloudUtilsShareService | CloudServiceTagUtils.UTILS_SHARE | 系统分享相关功能 |
| CloudUtilsSystemInfoService | CloudServiceTagUtils.UTILS_SYSTEM_INFO | 获取设备相关信息 |
| CloudUtilsSystemPageService | CloudServiceTagUtils.UTILS_SYSTEM_PAGE | 跳转常用系统页面 |


## 二、介绍

1、CloudUtilsTaskService   线程切换相关操作
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| isMainThread |  | 判断是否在主线程 |
| mainThread |  | 切换到主线程 |
| workThread |  | 切换到子线程 |
| delay | 1、timeOut：等待时间。2、unit：时间格式 | 等待多久时间后执行 |
| whenAll | functions：前置任务 | 等待前置任务全部执行完成后执行 |
| whenAll | 1、timeOut：等待时间。2、unit：时间格式。3、functions：前置任务 | 等待前置任务全部执行完成或超出等待时间后执行 |
| whenAny | functions：前置任务 | 等待某一个前置任务执行完成后执行 |
| whenAny | 1、timeOut：等待时间。2、unit：时间格式。3、functions：前置任务 | 等待某一个前置任务执行完成或超出等待时间后执行 |
| continueWhile | 1、callable：回调接口，返回 true 继续执行，false 跳出循环。2、task：任务体 | 循环执行任务 |
| call | task：任务体 | 在当前线程或上一个线程执行 |
| callUiThread | task：任务体 | 在 ui 线程执行 |
| callWorkThread | task：任务体 | 在工作线程执行 |
| cancel |  | 取消未执行与正在执行的任务 |

    返回对象中特有方法
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| isSuccess |  | 上一个任务是否执行成功 |
| getResponse |  | 获取上一个任务返回值 |
| getThrowable |  | 获取上一个任务异常信息(如果存在异常) |
</br>

2、CloudUtilsAppService   获取当前应用相关信息
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| getTargetSdkVersion |  | 获取当前app的目标设备SDK版本 |
| getUid |  | 获取当前app的uid |
| getPackageName |  | 获取当前app包名 |
| getIcon |  | 获取当前app图标 |
| getVersionCode |  | 获取当前app版本code |
| getLongVersionCode |  | 获取当前app版本code |
| getVersionName |  | 获取当前app版本name |
| isBackground |  | 判断当前app是否处于后台 |
</br>

3、CloudUtilsBitmapService  bitmap 相关操作
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| toBitmap | Drawable：准备转换成 bitmap 的 drawable 对象 | Drawable 转 bitmap |
| toBitmap | @DrawableRes drawableId：准备转换成 bitmap 的 drawable id | Drawable 转 bitmap |
</br>

4、CloudUtilsDisplayService  获取像素转换相关信息
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| px2dp | pxValue：需要转化的px数值 | px转dp |
| dp2px | dpValue：需要转化的dp数值 | dp转px |
| px2sp | pxValue：需要转化的px数值 | px转sp |
| sp2px | spValue：需要转化的sp数值 | sp转px |
















