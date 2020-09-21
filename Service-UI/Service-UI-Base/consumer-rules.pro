########################   CloudUiCheckBoolean   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.request.annotations.CloudUiCheckBoolean class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckBoolean <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckBoolean <fields>;
}

########################   CloudUiCheckBooleans   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.request.annotations.CloudUiCheckBooleans class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckBooleans <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckBooleans <fields>;
}

########################   CloudUiCheckNumber   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.request.annotations.CloudUiCheckNumber class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckNumber <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckNumber <fields>;
}

########################   CloudUiCheckNumbers   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.request.annotations.CloudUiCheckNumbers class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckNumbers <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckNumbers <fields>;
}

########################   CloudUiCheckString   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.request.annotations.CloudUiCheckString class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckString <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckString <fields>;
}

########################   CloudUiCheckStrings   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.request.annotations.CloudUiCheckStrings class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckStrings <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.CloudUiCheckStrings <fields>;
}

########################   TabHostRewardSelectFrom   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.request.annotations.TabHostRewardSelectFrom class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.TabHostRewardSelectFrom <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.request.annotations.TabHostRewardSelectFrom <fields>;
}
