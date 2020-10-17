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

1. CloudUtilsTaskService   线程切换相关操作

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

2. CloudUtilsAppService   获取当前应用相关信息

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

3. CloudUtilsBitmapService  bitmap 相关操作

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

4. CloudUtilsDisplayService  获取像素转换相关信息

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| px2dp | pxValue：需要转化的px数值 | px转dp |
| dp2px | dpValue：需要转化的dp数值 | dp转px |
| px2sp | pxValue：需要转化的px数值 | px转sp |
| sp2px | spValue：需要转化的sp数值 | sp转px |
</br>

5. CloudUtilsEditTextService  EditText控件管理类

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
</br>

6. CloudUtilsFileService  文件操作相关功能

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
</br>

7. CloudUtilsInstallService  应用安装相关操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| addInstallCallback | 1、cloudInstallCallback：安装状态回调接口。2、statusEnums：准备接收的状态类型 | 添加安装状态回调 |
| isInstallApp | packageName：包名 | 对应包名的app是否安装 |
| addProviderResourcePath | filePath：允许共享的安全路径 | 添加允许通过 provider 共享的文件路径 |
| installApp | apkPath：安装包路径 | 安装应用 |
| getPackageName | apkPath：安装包路径 | 获取对应apk的包名 |
| unInstallApp | packageName：包名 | 卸载应用 |
| getAllInstallAppsInfo |  | 获取所有已安装应用 |
| openApp | packageName：包名 | 打开对应包名的app |
</br>

8. CloudUtilsNetWorkService  网络状态相关操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| addNetWorkStatusCallback | callback：网络状态变化回调接口 | 添加网络状态变化回调 |
| removeNetWorkStatusCallback | callback：网络状态变化回调接口 | 移除网络状态变化回调 |
| isConnected |  | 是否有网络 |
| getNetworkType |  | 获取网络类型 |
| getWifiBssId |  | 获取当前 wifi 的站点名称 |
| getWifiRssI |  | 获取当前 wifi 的信号强度 |
| getWifiSsId |  | 获取当前 wifi 的名称 |
| getScanWifiInfoList |  | 获取扫描到的 Wi-Fi 信息列表 |
| getIpv6Address |  | 获取 ipv6 地址 |
</br>

9. CloudUtilsPermissionService  权限管理相关操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| isPermissionGranted | permission：权限名称 | 是否具有对应权限 |
| selfPermissionGranted | permission：权限名称 | 自动获取对应权限 |
</br>

10. CloudUtilsSecurityService  加解密相关操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| md5Encode | stream：准备加密的流 | md5 加密 |
| md5Encode | string：准备加密的字符串 | md5 加密 |
| aes |  | Aes 加密 |

    返回对象中特有方法
| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| setSecretKeySpec | key：密钥 | 设置密钥 |
| setIvParameterSpec | spec：偏移值 | 设置偏移量 |
| setTransFormation | formation：加密方案 | 设置加密方案 |
| encryptString | data：待加密对象 | 加密 |
| encryptString | bytes：待加密对象 | 加密 |
| encryptByte | data：待加密对象 | 加密 |
| encryptByte | bytes：待加密对象 | 加密 |
| decryptString | data：待解密对象 | 解密 |
| decryptString | bytes：待解密对象 | 解密 |
| decryptByte | data：待解密对象 | 解密 |
| decryptByte | bytes：待解密对象 | 解密 |
</br>


11. CloudUtilsShareService  系统分享相关功能

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| getUriForFile | file：文件流 | 获取允许共享的 uri |
| openSystemShareTxt | info：分享文字内容 | 打开系统分享，文字 |
| openSystemShareImg | imgPaths：图片地址集合 | 打开系统分享，图片 |
</br>

12. CloudUtilsReceiverService  广播接收器相关操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| addReceiverListener | 1、receiverInfo：接收器信息。2、receiverListener：接收器 | 添加全局接收器并设置接收范围 |
| addReceiverListener | 1、sendPermission：。自定义发送方需要具有的权限。2、receiverInfo：接收器信息。3、receiverListener：接收器 | 添加全局接收器并设置接收范围 |
| addLocalReceiverListener | 1、receiverInfo：接收器信息。2、receiverListener：接收器 | 添加本地接收器并设置接收范围 |
| removeReceiverListener | receiverListener：接收器 | 取消接收器 |
| sendBroadcast | intent：意图 | 发送全局广播 |
| sendBroadcast | 1、receiverPermission：自定义接收方需要具有的权限。2、intent：意图 | 发送全局广播并对接收方权限进行校验 |
| sendLocalBroadcast | intent：意图 | 发送本地广播 |
</br>

13. CloudUtilsSystemPageService  跳转常用系统页面

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| openAppSetting | packageName：包名 | 打开应用设置页面 |
| openNotificationSetting | 1、packageName：包名。2、uid：应用的uid | 打开应用通知设置页面 |
| openCall | phoneNumber：准备拨打的电话号码 | 打电话 |
</br>

