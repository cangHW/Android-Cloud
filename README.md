
<br/>

```
    Android 微服务架构，特点：兼容、解耦、无限扩展
```

<br/>

[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

一套快速开发架构，致力于在最短时间内从0到1的完成一个Android工程

<br/>

目前 Android Studio 默认支持 mavenCentral，如果您使用的版本没有默认添加 mavenCentral 库，请在项目根目录的 build.gradle 中添加以下仓库：

    buildscript {
        repositories {
            mavenCentral()
            。
            。
            。
        }
    }

如果已经添加，则忽略。
<br/>

## 一、基础库

[<font size='6' color='#528DFB' >基础库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Cloud-Api/README.md)

如果要定制自己的扩展能力，请关注这三个基础库，例如：组件化、插件化等等

### 1、功能库 

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cangHW/Cloud-Api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.cangHW%22%20AND%20a:%22Cloud-Api%22)

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| 功能库 | 1.1.4 | io.github.cangHW:Cloud-Api:“版本号” | api |

### 2、编译库 

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cangHW/Cloud-Compiler.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.cangHW%22%20AND%20a:%22Cloud-Compiler%22)

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| 编译库 | 1.0.1 | io.github.cangHW:Cloud-Compiler:“版本号” | annotationProcessor |

### 3、Gradle 插件

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cangHW/Cloud-Plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.cangHW%22%20AND%20a:%22Cloud-Plugin%22)

| 模块 | 最新版本号 | 地址 | 引用位置 |
| :--: | :--: | :-- | :--: |
| 插件库 | 1.1.3.2 | io.github.cangHW:Cloud-Plugin:“版本号” | 项目根目录的 build.gradle 文件中，在 dependencies 节点下添加引用 |

<br/>

## 二、UI 库

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cangHW/Service-UI-Info.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.cangHW%22%20AND%20a:%22Service-UI-Info%22)

[<font size='6' color='#528DFB' >UI 库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Service-UI/README.md)

提供ui开发中常用的能力

<br/>

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| UI | 1.2.1 | io.github.cangHW:Service-UI-Info:“版本号” | api |

<br/>

## 三、工具库

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cangHW/Service-Utils-Info.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.cangHW%22%20AND%20a:%22Service-Utils-Info%22)

[<font size='6' color='#528DFB' >工具库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Service-Utils/README.md)

提供工具库能力

<br/>

| 模块 | 最新版本号 | 地址 | 引用方式 |
| :--: | :--: | :-- | :--: |
| Utils | 1.2.2 | io.github.cangHW:Service-Utils-Info:“版本号” | api |

<br/>

<br/>

## 四、使用方式
1. 首先通过 CloudSystem 对框架进行初始化
2. 注册服务
3. 服务自动注册(可选，建议添加)
4. 获取服务
5. 拦截器的注册以及触发

### 1、初始化
通过 CloudSystem.init(@NonNull Context context, boolean isDebug) 进行初始化，初始化之后即可正常使用本框架

### 2、注册服务

<br/>

1. 注册新服务

支持注册一个新的服务供其他模块使用，关于如何开发一个新服务，请查看[<font size='6' color='#528DFB' >基础库文档</font>](https://github.com/cangHW/Android-Cloud/blob/master/Cloud-Api/README.md)，
如果我们不需要服务的自动注册，那么我们只需要在服务设计并开发完成后通过以下方法进行注册即可

    例如：
    ArrayList<BaseService> services = new ArrayList<>();
    services.add(new XXXService());
    CloudSystem.registerServices(services);

2. 替换旧服务

假如我们使用到的三方库中，有一个或多个服务不能满足我们的需求或者存在一些问题，这时就可以通过替换服务的方式来解决，避免了因一个小的错误导致需要替换整个三方库的尴尬处境

    例如：
    ArrayList<BaseService> services = new ArrayList<>();
    services.add(new XXXService());//继承自同一个服务接口并拥有相同服务tag的服务实现类
    CloudSystem.registerServices(services);

这时，我们获取服务时，即可获取到替换后的服务

### 3、服务自动注册(可选，建议添加)

<br/>

    apply plugin: 'com.cloud.service'

    buildscript {
        repositories {
            mavenCentral()
        }

        dependencies {
            //替换成最新版本
            classpath "io.github.cangHW:Cloud-Plugin:x.x.x"
        }
    }

目前 DexFile 相关 API 已被 Android 高版本标记为过时，如果后续 Android SDK 移除这个 API ，将可能导致注册失败。
建议使用插件方式进行自动注册，目前有两大优势：1、可以有效加快初始化速度，2、可以避免 DexFile 被移除或某些加固软件导致的初始化失败。

### 4、获取服务
本框架中的服务代表一个个的功能，例如：file文件处理、bitmap图片处理、数据校验、网络请求等等。具体的服务对应的 class 类型与 tag 值，请查看对应的文档
<br/>

而获取服务的方式有以下几种：

| 方法 | 参数 | 效果 |
| :--: | :--: | :-- |
| CloudSystem.getService() | tag：服务对应的 tag | 通过 tag 获取单个/多个服务 |
| CloudSystem.getServiceWithUuid() | 1、uuid：唯一ID，用于触发对应拦截器。 <br/> 2、tag：服务对应的 tag | 通过 tag 获取单个/多个服务 |
| CloudSystem.getService() | tClass：服务的 class 类型 | 通过 class 类型获取单个/多个服务 |
| CloudSystem.getServiceWithUuid() | 1、uuid：唯一ID，用于触发对应拦截器。 <br/> 2、tClass：服务的 class 类型 | 通过 class 类型获取单个/多个服务 |

    例如：
    CloudUtilsTaskService taskService = CloudSystem.getService(CloudUtilsTaskService.class);
    或者
    CloudUtilsTaskService taskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);

### 5、拦截器
拦截器分为两种，全局拦截器与定向拦截器

拦截器的生效时机为获取服务，框架查找到对应服务之后将服务返回给获取方之前，会先执行拦截器的方法，在拦截器内部，你可以进行替换、代理、移除等等操作。
拦截器为顺序执行（添加的顺序），开发者可以利用该特性实现各种不可思议的特效，例如：针对某一个服务的实例对象进行链式处理。

<br/>

1. 全局拦截器

| 方法 | 参数 | 效果 |
| :--: | :--: | :-- |
| CloudSystem.addConverter() | 1、tClass：服务的 class 类型。 <br/> 2、converter：拦截器接口对象 | 注册完成拦截器之后，只要通过 CloudSystem 获取服务就会触发注册好的拦截器(获取的服务类型必须和拦截器准备拦截的类型相同) |

    例如：
    CloudSystem.addConverter(CloudUtilsTaskService.class, new Converter<CloudUtilsTaskService>() {
        @NonNull
        @Override
        public CloudUtilsTaskService converter(@NonNull CloudUtilsTaskService cloudUtilsTaskService) throws Throwable {
            //这里需要注意，如非必要，尽量不要返回 null，否则获取当前服务时，将只能获取到 null
            return cloudUtilsTaskService;
        }
    });
    
    CloudUtilsTaskService taskService = CloudSystem.getService(CloudUtilsTaskService.class);

2. 定向拦截器

| 方法 | 参数 | 效果 |
| :--: | :--: | :-- |
| CloudSystem.addConverter() | 1、uuid：唯一ID，用于匹配在获取服务时传入的 uuid。 <br/> 2、tClass：服务的 class 类型。 <br/> 3、converter：拦截器接口对象 | 注册完成拦截器之后，只要通过 CloudSystem 获取服务，并且 uuid 相同就会触发注册好的拦截器(获取的服务类型必须和拦截器准备拦截的类型相同) |

    例如：
    String uuid = "自定义唯一id";
    
    CloudSystem.addConverter(uuid, CloudUtilsTaskService.class, new Converter<CloudUtilsTaskService>() {
        @NonNull
        @Override
        public CloudUtilsTaskService converter(@NonNull CloudUtilsTaskService cloudUtilsTaskService) throws Throwable {
            //这里需要注意，如非必要，尽量不要返回 null，否则获取当前服务时，将只能获取到 null
            return cloudUtilsTaskService;
        }
    });
    
    CloudUtilsTaskService taskService = CloudSystem.getServiceWithUuid(uuid, CloudUtilsTaskService.class);

3. 移除拦截器

移除拦截器将会把所有符合条件的拦截器全部移除
<br/>

| 方法 | 参数 | 效果 |
| :--: | :--: | :-- |
| CloudSystem.removeConverterByClass() | tClass：服务的 class 类型 | 根据服务类型移除对应的转换器对象 |
| CloudSystem.removeConverterByUuid() | uuid：唯一ID | 根据服务类型移除对应的转换器对象 |

    例如：
    CloudSystem.removeConverterByClass(CloudUtilsTaskService.class);
    CloudSystem.removeConverterByUuid(CloudServiceTagUtils.UTILS_TASK);

<br/>

## 五、交流学习

    QQ   :  1163478116
    微信  :  1163478116






