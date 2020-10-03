```
    Android 微服务架构，具有无限扩展性，特点：兼容、解耦
```
<br/>
<br/>

# 工具库文档
本文档主要介绍工具库中包含的功能以及用法，关于如何获取服务，请看框架整体文档

## 一、服务

| | 类名 | tag | 说明 |
| :--: | :-- | :-- | :-- |
| 1 | CloudUtilsTaskService | CloudServiceTagUtils.UTILS_TASK | 线程切换相关操作 |
| 2 | CloudUtilsAppService | CloudServiceTagUtils.UTILS_APP | 获取当前应用相关信息 |
| 3 | CloudUtilsBitmapService | CloudServiceTagUtils.UTILS_BITMAP | bitmap 相关操作 |
| 4 | CloudUtilsDisplayService | CloudServiceTagUtils.UTILS_DISPLAY | 获取像素转换相关信息 |
| 5 | CloudUtilsEditTextService | CloudServiceTagUtils.UTILS_EDIT_TEXT | 控制输入框相关功能 |
| 6 | CloudUtilsFileService | CloudServiceTagUtils.UTILS_FILE | 文件操作相关功能 |
| 7 | CloudUtilsInstallService | CloudServiceTagUtils.UTILS_INSTALL | 应用安装相关操作 |
| 8 | CloudUtilsNetWorkService | CloudServiceTagUtils.UTILS_NET_WORK | 网络状态相关操作 |
| 9 | CloudUtilsPermissionService | CloudServiceTagUtils.UTILS_PERMISSION | 权限管理相关操作 |
| 10 | CloudUtilsSecurityService | CloudServiceTagUtils.UTILS_SECURITY | 加解密相关操作 |
| 11 | CloudUtilsShareService | CloudServiceTagUtils.UTILS_SHARE | 系统分享相关功能 |
| 12 | CloudUtilsReceiverService | CloudServiceTagUtils.UTILS_RECEIVER | 广播接收器相关操作 |
| 13 | CloudUtilsSystemPageService | CloudServiceTagUtils.UTILS_SYSTEM_PAGE | 跳转常用系统页面 |


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
| compressBitmapBySize | 1、bitmap：准备压缩的bitmap。2、maxWidth：最大宽度。3、maxHeight：最大高度。4、isAdjust：是否自动调整尺寸 | 压缩图片,尺寸压缩 |
| compressBitmapByQuality | 1、bitmap：准备压缩的bitmap。2、maxKb：图片最大保留多少 kb | 压缩图片,质量压缩 适用于图片上传 |
| rotate | 1、bitmap：准备旋转的bitmap。2、degrees：旋转角度(90为顺时针旋转,-90为逆时针旋转) | 旋转图片 |
| zoom | 1、bitmap：准备放大或缩小的bitmap。2、ratio：放大或缩小的倍数，大于1表示放大，小于1表示缩小 | 放大或缩小图片 |
| printWord | 1、bitmap：需要印文字的bitmap。2、text：需要印上去的文字。3、message：字体信息 | 在图片上印字 |
| overlap | 1、src：源图片。2、dst：准备合并绘制的图片。3、left：左边起点坐标。4、top：顶部起点坐标 | 图片重叠绘制，可以用于给图片加水印等 |
| captcha | 1、width：图片宽度(如果小于等于0，则使用默认值：200)。2、height：图片高度(如果小于等于0，则使用默认值：80)。3、keyCode：验证码内容(如果为空，则自动生成随机内容)。4、textSize：文字大小(如果小于等于0，则使用默认值：30) | 创建图形验证码（默认 4 位数字与字母） |
</br>

4、CloudUtilsDisplayService  获取像素转换相关信息
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| px2dp | pxValue：需要转化的px数值 | px转dp |
| dp2px | dpValue：需要转化的dp数值 | dp转px |
| px2sp | pxValue：需要转化的px数值 | px转sp |
| sp2px | spValue：需要转化的sp数值 | sp转px |
</br>

5、CloudUtilsEditTextService  EditText控件管理类
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| with | editText：EditText | 通过 editText 对象创建管理类 |
| showSoftInput | editText：EditText | 弹出键盘 |
| hideSoftInput | view：任意 view | 隐藏键盘 |
| hideInputContent | editText：EditText | 密文模式 |
| showInputContent | editText：EditText | 明文模式 |

    返回对象中特有方法
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| clearInputType |  | 清除当前输入框的输入格式，危险，如果随后没有设置允许输入格式，将导致无法输入 |
| addTextChangedCallback | callback：文字改变监听回调 | 设置文字改变监听 |
| banEmoji |  | 禁止输入表情 |
| banNumber |  | 禁止输入数字 |
| banLetter |  | 禁止输入英文 |
| banLetterLowerCase |  | 禁止输入小写英文 |
| banLetterUpperCase |  | 禁止输入大写英文 |
| banDigits | digits：自定义内容 | 自定义禁止输入的内容 |
| banMatcher | regex：正则表达式 | 自定义禁止输入，正则 |
| banDigits |  | 自定义禁止输入的内容 |
| allowNumber |  | 设置允许输入数字 |
| allowLetter |  | 设置允许输入英文 |
| allowLetterUpperCase |  | 设置允许输入大写英文 |
| allowLetterLowerCase |  | 设置允许输入小写英文 |
| allowDigits | digits：自定义内容 | 自定义允许输入的内容 |
| allowMatcher | regex：正则表达式 | 自定义允许输入，正则 |

5、CloudUtilsFileService  文件操作相关功能
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| createFile | path：文件地址 | 创建 file，自动创建相关文件夹与文件 |
| deleteFile | path：文件地址 | 删除文件 |
| read | path：文件地址 | 读文件，同步执行 |
| readLines | path：文件地址 | 读文件，同步执行 |
| write | 1、path：文件地址。2、data：内容。3、append：是否续写 | 写文件，同步执行 |
| writeLines | 1、path：文件地址。2、datas：内容。3、append：是否续写 | 写文件，同步执行 |
| write | 1、oldFile：旧位置或旧名称。2、newFile：新位置或新名称 | 写文件，同步执行 |
| write | 1、is：文件流。2、localFile：本地文件。3、seek：偏移位置。4、callback：进度回调 | 写文件，同步执行 |



















