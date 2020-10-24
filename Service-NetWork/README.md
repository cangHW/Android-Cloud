```
    Android 微服务架构，具有无限扩展性，特点：兼容、解耦
```
<br/>
<br/>

# 网络库文档
本文档主要介绍网络库中包含的功能以及用法，关于如何获取服务，请看框架整体文档

## 一、服务

| | 类名 | tag | 说明 |
| :--: | :-- | :-- | :-- |
| 1 | CloudNetWorkInitService | CloudServiceTagNetWork.NET_WORK_INIT | 网络模块初始化相关操作 |
| 2 | CloudNetWorkRequestService | CloudServiceTagNetWork.NET_WORK_REQUEST | 网络模块请求操作 |
| 3 | CloudNetWorkDownloadService | CloudServiceTagNetWork.NET_WORK_DOWNLOAD | 下载相关操作 |


## 二、介绍

1. CloudNetWorkInitService   网络模块初始化相关操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| setBaseUrl | baseUrl：baseUrl | 设置 BaseUrl |
| setBaseUrls | 1、id：baseUrl 对应的 Id <br/> 2、baseUrl：baseUrl | 设置多个 BaseUrl，方便进行 baseUrl 的动态替换 |
| setRequestTimeout | 1、timeout：超时时间 <br/> 2、unit：时间粒度 | 设置请求超时，包括重试时间 |
| setReadTimeout | 1、timeout：超时时间 <br/> 2、unit：时间粒度 | 设置读取超时 |
| setWriteTimeout | 1、timeout：超时时间 <br/> 2、unit：时间粒度 | 设置写入超时 |
| setConnectTimeout | 1、timeout：超时时间 <br/> 2、unit：时间粒度 | 设置连接超时 |
| setGlobalRequestCallback | callback：全局回调接口 | 设置全局请求回调 |
| addConverterFactory | factory：转换器工厂对象 | 设置自定义转换器工厂 |
| addCallAdapterFactory | factory：回调接口适配器工厂对象 | 设置回调接口适配器工厂 |
| addInterceptor | interceptor：拦截器对象 | 添加拦截器 |
| addNetWorkInterceptor | interceptor：拦截器对象 | 添加网络拦截器，生效于真实请求之前与真实请求之后 |
| setProxy | proxy：网络代理对象 | 设置代理 |
| setCookieJar | cookieJar：网络 cookie | 设置 cookie |
| setCache | cache：网络缓存 | 设置网络缓存 |
| setMock | mock：网络模拟 | 设置 mock 数据 |
| setSslSocket | 1、sslSocket：安全套接层 <br/> 2、manager：信任证书 | 设置 https 安全套接层 |
| setRetryCount | count：重试次数 | 设置重试次数，请求失败自动重试，默认不重试 |
| build |  | 构建网络模块，使设置生效 |
</br>

    例如：
    CloudNetWorkInitService initService = CloudSystem.getService(CloudServiceTagNetWork.NET_WORK_INIT);
    if (initService == null) {
        return;
    }
    initService.setBaseUrl("https://hao.360.com")
            .setBaseUrls(NetWorkFragment.BASE_URL_ID, "https://hao.360.com")
            .setMock(mock)
            .setGlobalRequestCallback(globalCallback)
            .addNetWorkInterceptor(logInterceptor)
            .build();

2. CloudNetWorkRequestService   网络模块请求操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| setRetryCount | count：重试次数 | 设置本次请求重试次数 |
| removeCookie |  | 移除本次请求的 cookie |
| create | service：请求接口类 class 对象 | 创建网络请求 |
| create | 1、tag：身份信息，用于标示本次请求，一对多，一个 tag 可以绑定多个请求 <br/> 2、service：请求接口类 class 对象 | 创建网络请求，并绑定 tag |
| cancelByTag | tag：身份信息 | 通过 tag 取消请求 |
| cancelAllOfApp |  | 取消所有请求 |
| cancelAllOfService |  | 取消通过当前 service 创建的请求 |

    相关注解
    
| 注解 | 说明 |
| :-- | :-- |    
| CloudNetWorkBaseUrl | 设置本次请求的 BaseUrl |
| CloudNetWorkBaseUrlId | 设置本次请求的 BaseUrl 的 id，方便对 BaseUrl 进行动态替换 |
| CloudNetWorkField | 设置参数字段 |
| CloudNetWorkFormUrlEncoded | 设置本次请求参数需要进行 UrlEncoded |
| CloudNetWorkGet | 设置本次请求为 get 请求，并传入 url path |
| CloudNetWorkPost | 设置本次请求为 post 请求，并传入 url path |
| CloudNetWorkHeader | 为本次请求添加 header |
| CloudNetWorkHeaders | 为本次请求添加多个 header |
| CloudNetWorkUrl | 设置本次请求完整的 url |
| CloudNetWorkTag | 为本次请求添加自定义 tag |

    例如：
    public interface RequestService {
    
        @CloudNetWorkBaseUrl(BaseApplication.BASE_URL)
        @CloudNetWorkGet(BaseApplication.PATH_URL1)
        CloudNetWorkCall<KuaiDiBean> normal(@CloudNetWorkField("postid") String postid);
    
    }
    
    CloudNetWorkInitService initService = CloudSystem.getService(CloudNetWorkInitService.class);
    if (initService == null) {
          return;
    }
    initService.setBaseUrls(BASE_URL_ID, BaseApplication.BASE_URL_1);
    RequestService requestService = mRequestService.create(RequestService.class);
    requestService.baseUrl("北京", "json", "11ffd27d38deda622f51c9d314d46b17").enqueue(new CloudNetWorkCallback<WeatherBean>() {
        @Override
        public void onResponse(CloudNetWorkResponse<WeatherBean> response) {
            if (response.isSuccessful()) {
                Logger.Error("onResponse 成功 : " + response.response().getMessage());
            } else {
                Logger.Error("onResponse 失败");
            }
        }
    
        @Override
        public void onFailure(Throwable t) {
            Logger.Error("onFailure : " + t.getMessage());
        }
    });


3. CloudNetWorkDownloadService   下载相关操作

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| setGlobalMaxConcurrent | maxCount：最大数量 | 设置最大同时下载数量，默认为 5 |
| setGlobalDownloadDir | fileDir：下载路径 | 设置下载路径，可能被具体任务路径替换，如果具体任务设置了路径 |
| setGlobalDownloadCacheDir | fileCacheDir：下载路径 | 设置下载缓存路径，可能被具体任务缓存路径替换，如果具体任务设置了缓存路径 |
| setGlobalMultiProcessEnable | enable：是否使用多进程 | 设置是否使用多进程进行下载，默认使用 |
| setGlobalDownloadCallback | callback：下载回调 | 设置下载回调，可能被具体任务回调替换，如果具体任务设置了回调 |
| setGlobalNotificationEnable | enable：通知是否显示 | 设置通知是否显示，默认显示，可能被具体任务设置替换，如果具体任务进行了设置 |
| setGlobalNotificationCallback | callback：Notification 回调 | 设置 Notification 回调，可能被具体任务回调替换，如果具体任务设置了回调 |
| setGlobalNotificationBuilder | info：通知构建参数 | 设置通知构建参数 |
| start | info：下载任务参数 | 开始下载，小于 0 代表失败 |
| pause | downloadId：下载任务 id | 暂停下载 |
| continues | downloadId：下载任务 id | 恢复下载 |
| cancel | downloadId：下载任务 id | 取消下载 |
| delete | downloadId：下载任务 id | 删除下载，同时删除任务记录 |
| getDownloadState | downloadId：下载任务 id | 获取下载状态 |

    相关对象
    CloudNetWorkDownloadInfo：下载任务参数
    
CloudNetWorkDownloadInfo

| 方法名 | 参数 | 说明 |
| :-- | :-- | :-- |
| setTaskName | taskName：任务名称 | 设置任务名称(为空则使用文件名称) |
| setFilePath | filePath：文件路径 | 设置文件路径(为空则自动创建) |
| setFileCachePath | fileCachePath：缓存路径 | 设置缓存路径(为空则自动创建) |
| setFileName | fileName：文件名称 | 设置文件名称(为空则从下载地址获取或自动创建) |
| setFileUrl | fileUrl：下载地址 | 设置下载地址(不能为空) |
| setFileMd5 | fileMd5：文件 md5 值 | 设置文件 md5 值(为空则不校验 md5) |
| setFileSize | fileSize：文件大小 | 设置文件大小(为空则从 sever 获取) |
| setDownloadCallback | downloadCallback：下载回调 | 设置下载回调(为空则使用全局下载回调) |
| setNotificationEnable | notificationEnable：是否显示通知 | 设置是否显示通知(为空则使用全局设置，默认显示) |
| setNotificationCallback | notificationCallback：通知回调 | 设置通知回调(为空则使用全局通知回调)，弱引用，注意数据回收，回收后无法接收回调 |
| setTag | tag：任务 tag | 设置任务 tag(自定义数据) |

    例如：
    service = CloudSystem.getService(CloudServiceTagNetWork.NET_WORK_DOWNLOAD);
    if (service == null) {
        return;
    }
    
    service.setGlobalNotificationBuilder(
                CloudNetWorkNotificationInfo.builder()
                .setChannelId("1222")
                .setChannelName("下载")
                .setChannelLevel(CloudNetWorkNotificationInfo.ChannelLevel.LOW)
                .build()
    ).setGlobalMultiProcessEnable(false);

    CloudNetWorkDownloadInfo.Builder builder = CloudNetWorkDownloadInfo.builder()
            .setDownloadCallback(downloadCallback)
            .setNotificationEnable(true)
            .setNotificationCallback(notificationCallback)
            .setTaskName("手助")
            .setFileName("app-develop-release.apk")
            .setFileUrl("https://app.api.sj.360.cn/url/download/id/4050514/from/web_detail");
    int downloadId = service.start(builder.build());




