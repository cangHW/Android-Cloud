########################   CloudUiCheckBoolean   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.annotations.CloudUiCheckBoolean class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckBoolean <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckBoolean <fields>;
}

########################   CloudUiCheckBooleans   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.annotations.CloudUiCheckBooleans class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckBooleans <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckBooleans <fields>;
}

########################   CloudUiCheckNumber   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.annotations.CloudUiCheckNumber class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckNumber <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckNumber <fields>;
}

########################   CloudUiCheckNumbers   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.annotations.CloudUiCheckNumbers class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckNumbers <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckNumbers <fields>;
}

########################   CloudUiCheckString   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.annotations.CloudUiCheckString class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckString <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckString <fields>;
}

########################   CloudUiCheckStrings   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.annotations.CloudUiCheckStrings class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckStrings <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.CloudUiCheckStrings <fields>;
}

########################   TabHostRewardSelectFrom   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.api.annotations.TabHostRewardSelectFrom class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.TabHostRewardSelectFrom <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.api.annotations.TabHostRewardSelectFrom <fields>;
}
