
<br/>

```
    Android 微服务架构，特点：兼容、解耦、无限扩展
```

<br/>

[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

一套快速开发架构，致力于在最短时间内从0到1的完成一个Android工程

<br/>

<br/>

## 基础库

[<font size='6' color='#528DFB' >基础库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Cloud-Api/README.md)

如果要定制自己的扩展能力，请关注这个基础库，例如：组件化、插件化等等

<br/>

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| 基础库 | 0.0.3 | com.proxy.service:Cloud-Api:“版本号” | api |
| 编译库 | 0.0.4 | com.proxy.service:Cloud-Compiler:“版本号” | annotationProcessor |


## 网络库

<font size='4' color='F00' >开发中</font>

[<font size='6' color='#528DFB' >网络库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Service-NetWork/README.md)

提供网络能力，可以自由选择底层实现方式

<br/>

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| OkHttp |  | com.proxy.service:Service-OkHttp:“版本号” | api |
| Volley |  | com.proxy.service:Service-Volley:“版本号” | api |
| Retrofit |  | com.proxy.service:Service-Retrofit:“版本号” | api |


## UI 库

[<font size='6' color='#528DFB' >UI 库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Service-UI/README.md)

提供ui开发中常用的能力

<br/>

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| UI | 0.0.3 | com.proxy.service:Service-UI-Info:“版本号” | api |


## 进程库
<font size='4' color='f00' >开发中</font>

[<font size='6' color='#528DFB' >进程库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Service-Process/README.md)

提供多进程能力

<br/>

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| Process |  | com.proxy.service:Service-Process:“版本号” | api |


## 工具库

[<font size='6' color='#528DFB' >工具库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Service-Utils/README.md)

提供工具库能力

<br/>

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| Utils | 0.0.2 | com.proxy.service:Service-Utils-Info:“版本号” | api |


## 基本用法
1、首先通过 CloudSystem 对框架进行初始化，2、按需注册拦截器，3、通过 CloudSystem 获取服务，4、触发拦截器，执行拦截器逻辑（如果存在对应拦截器）

### 一、初始化
通过 CloudSystem.init(@NonNull Context context, boolean isDebug) 进行初始化，初始化之后即可正常使用本框架

### 二、服务
本框架中的服务代表一个个的功能，例如：file文件处理、bitmap图片处理、数据校验、网络请求等等。具体的服务对应的 class 类型与 tag 值，请查看对应的文档
<br/>

而获取服务的方式有以下几种：

| 方法 | 参数 | 效果 |
| :--: | :--: | :-- |
| CloudSystem.getService() | tag：服务对应的 tag | 通过 tag 获取单个/多个服务 |
| CloudSystem.getServiceWithUuid() | 1、uuid：唯一ID，用于触发对应拦截器。 2、tag：服务对应的 tag | 通过 tag 获取单个/多个服务 |
| CloudSystem.getService() | tClass：服务的 class 类型 | 通过 class 类型获取单个/多个服务 |
| CloudSystem.getServiceWithUuid() | 1、uuid：唯一ID，用于触发对应拦截器。 2、tClass：服务的 class 类型 | 通过 class 类型获取单个/多个服务 |


### 三、拦截器
拦截器分为两种，全局拦截器与定向拦截器
<br/>

1、全局拦截器
| 方法 | 参数 | 效果 |
| :--: | :--: | :-- |
| CloudSystem.addConverter() | 1、tClass：服务的 class 类型。 2、converter：拦截器接口对象 | 注册完成拦截器之后，只要通过 CloudSystem 获取服务就会触发注册好的拦截器(获取的服务类型必须和拦截器准备拦截的类型相同) |


1、定向拦截器
| 方法 | 参数 | 效果 |
| :--: | :--: | :-- |
| CloudSystem.addConverter() | 1、uuid：唯一ID，用于匹配在获取服务时传入的 uuid。 2、tClass：服务的 class 类型。 3、converter：拦截器接口对象 | 注册完成拦截器之后，只要通过 CloudSystem 获取服务，并且 uuid 相同就会触发注册好的拦截器(获取的服务类型必须和拦截器准备拦截的类型相同) |








