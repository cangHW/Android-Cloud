```
    Android 微服务架构，具有无限扩展性，特点：兼容、解耦
```
<br/>
<br/>

# UI 库文档
本文档主要介绍 UI 库中包含的功能以及用法，关于如何获取服务，请看框架整体文档

    添加依赖和配置：
    
    dependencies {
        //替换成最新版本
        api 'com.proxy.service:Service-UI-Info:x.x.x'
        ...
    }    

## 一、服务
|| 类名 | tag | 说明 |
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
| setConditions | 1、id：同一标记下面的唯一ID。2、checkBooleanInfo：检测条件  | 设置 boolean 检测条件 |
| setConditions | 1、id：同一标记下面的唯一ID。2、checkNumberInfo：检测条件  | 设置 number 检测条件 |
| setConditions | 1、id：同一标记下面的唯一ID。2、checkStringInfo：检测条件  | 设置 string 检测条件 |
| check | markId：标记id，标记检测条件  | 发起检测 |
| check | 1、markId：标记id，标记检测条件。2、content：待检测的内容  | 发起检测 |
| runMain | runnable：检测成功后执行 | 检测成功执行，主线程 |
| runWork | runnable：检测成功后执行 | 检测成功执行，子线程 |
<br/>

    例如：
    
    private static final String ACCOUNT = "account";
    
    @CloudUiCheckStrings({
            @CloudUiCheckString(markId = ACCOUNT, notEmpty = true, message = "请输入用户名"),
            @CloudUiCheckString(markId = ACCOUNT, notBlank = true, message = "用户名不能为空格"),
            @CloudUiCheckString(markId = ACCOUNT, maxLength = 8, notBlank = true, stringId = R.string.long_account),
            @CloudUiCheckString(markId = ACCOUNT, minLength = 4, notBlank = true, message = "用户名太短"),
            @CloudUiCheckString(markId = ACCOUNT, notWithRegex = "[01abc]", message = "用户名不能包含 01abc")
    })
    private String mAccountText = "";
    
    CloudUiFieldCheckService service = CloudSystem.getService(CloudServiceTagUi.UI_FIELD_CHECK);
    if (service == null) {
        return;
    }
    //初始化
    service.init(this);
    service.check(ACCOUNT)
            .runMain(() -> {
                MainActivity.launch(LoginActivity.this);
                finish();
            });
            
    或者
    
    private static final String CAPTCHA = "captcha";
    private String mCaptchaText = "";
    
    CloudUiFieldCheckService service = CloudSystem.getService(CloudServiceTagUi.UI_FIELD_CHECK);
    if (service == null) {
        return;
    }
    //初始化
    service.init(this);
    service.setConditions(1, CloudUiCheckStringInfo.builder(CAPTCHA)
            .setNotEmpty(true)
            .setMessage("图形验证码不能为空")
            .build());
            
    service.check(CAPTCHA, mCaptchaText)
            .runMain(() -> {
                MainActivity.launch(LoginActivity.this);
                finish();
            });        

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

    用法：
    1、添加依赖和配置
    
    android {
        defaultConfig {
            ...
            javaCompileOptions {
                annotationProcessorOptions {
                    arguments = [CLOUD_MODULE_NAME: project.getName()]
                }
            }
        }
    }
    
    dependencies {
        // 替换成最新版本
        annotationProcessor 'com.proxy.service:Cloud-Compiler:x.x.x'
        ...
    }
    
    2、添加注解
    
    //在需要使用tab切换功能的模块中，创建类并继承父类：CloudUiTabHostFragmentReward 或 CloudUiTabHostViewReward
    //添加注解，rewardTag 主要用于筛选要展示的页面(可不传)
    @CloudUiTabHostReward(rewardTag = "main")
    public class LibraryRewardImpl extends CloudUiTabHostFragmentReward {
        ...
    }
    
    3、展示页面
    
    CloudUiTabHostService service = CloudSystem.getService(CloudServiceTagUi.UI_TAB_HOST);
    if (service != null) {
        service.setActivity(activity)
                .setContentSpace(contentLayout)
               .setTabSpace(bottomLayout)
               .addEventCallback(this)
               .setSelect(0)
               .showWithTag("main");
    }
    









