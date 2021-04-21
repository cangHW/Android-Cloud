```
    Android 微服务架构，具有无限扩展性，特点：兼容、解耦
```
<br/>

限制进步的唯一敌人 ———— 脑洞

<br/>

# 基础库文档
本文档主要介绍基础库中包含的功能以及用法，以及如何根据基础库定制出属于自己的特性库

## 一、添加依赖和配置

    android {
        defaultConfig {
            ...
             //用于服务自动注册
            javaCompileOptions {
                annotationProcessorOptions {
                    arguments = [CLOUD_MODULE_NAME: project.getName()]
                }
            }
        }
    }
    
    dependencies {
        // 替换成最新版本, 需要注意的是api
        // 要与compiler匹配使用，均使用最新版可以保证兼容
        compile 'io.github.cangHW:Cloud-Api:x.x.x'
        //用于服务自动注册
        annotationProcessor 'io.github.cangHW:Cloud-Compiler:x.x.x'
        ...
    }

## 二、相关注解

| 注解 | 说明 |
| :-- | :-- |   
| CloudApiNewInstance | 标示一个服务类每次使用时都会创建一个新的对象 |
| CloudApiService | 标示一个类为对外提供的服务实现类 |

## 三、创建服务
这里建议使用接口，方便后续扩展以及维护

    定义接口：
    
    //必须继承或实现 BaseService 接口
    public interface XXXService extends BaseService {
        //定义服务所需要提供的功能
        ...
    }

    实现接口：
    
    @CloudApiNewInstance //按需添加
    @CloudApiService(serviceTag = "xxxx") //必须添加
    public class XXXServiceImpl implements XXXService {
        //提供具体的功能
        ...
    }

## 四、使用服务
编译并运行后，服务即可自动注册进当前框架中，随后正常使用服务即可，运行后即可以看到我们自定义的服务已经完美的运行了起来

## 四、最后

到了这里，我们就可以把这个库打包成 aar 或者 jar 发布到网上供别人使用，也可以考虑开源让更多的开发者参与进来




