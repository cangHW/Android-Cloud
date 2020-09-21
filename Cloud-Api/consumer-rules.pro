-keepattributes *Annotation*
-keepattributes Signature

-keep public class * extends android.support.annotation.**

-keep class com.cloud.service.**{*;}
-keep class * implements com.proxy.service.api.service.listener.Converter{
   *** converter(***);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

########################   CloudApiNewInstance   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.annotations.CloudApiNewInstance class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.annotations.CloudApiNewInstance <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.annotations.CloudApiNewInstance <fields>;
}

########################   CloudApiService   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.annotations.CloudApiService class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.annotations.CloudApiService <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.annotations.CloudApiService <fields>;
}

########################   CloudUiTabHostReward   ###########################
# 不混淆使用了注解的类及类成员
-keep @com.proxy.service.annotations.CloudUiTabHostReward class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.annotations.CloudUiTabHostReward <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @com.proxy.service.annotations.CloudUiTabHostReward <fields>;
}