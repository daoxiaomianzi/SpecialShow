# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xuyong/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-ignorewarnings
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keep public class * extends android.os.IInterface

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}

-keepclasseswithmembernames class * { # 保持 native 方法不被混淆
 native <methods>;
}

-keepclasseswithmembers class * { # 保持自定义控件类不被混淆
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
 public void *(android.view.View);
}

-keepclassmembers enum * { # 保持枚举 enum 类不被混淆
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
 public static final android.os.Parcelable$Creator *;
}

#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#---------------------------------1.实体类---------------------------------
-keep class com.show.specialshow.model.** { *; }


#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------
#---------------xutils----------------
-keep class com.lidroid.xutils.** { *; }
-keep public class * extends com.lidroid.xutils.**
-keep public interface com.lidroid.xutils.** {*;}
-dontwarn com.lidroid.xutils.**
#---------------xutils----------------

#----------------------commons-lang3----------------------------
-keep class org.apache.commons.lang3.** { *; }
-keep public class * extends org.apache.commons.lang3.**
-keep public interface org.apache.commons.lang3.** {*;}
-dontwarn org.apache.commons.lang3.**
#-----------------------------commons-lang3-----------------------

#-----------------------------fastjson------------------------------------
-keep class com.alibaba.fastjson.** { *; }
-keep public class * extends com.alibaba.fastjson.**
-keep public interface com.alibaba.fastjson.** {*;}
-dontwarn com.alibaba.fastjson.**
#-------------------------------fastjson----------------------------------

#-----------------------------pinyin4android------------------------------------
-keep class com.pinyin4android.** { *; }
-keep public class * extends com.pinyin4android.**
-keep public interface com.pinyin4android.** {*;}
-dontwarn com.pinyin4android.**
#-------------------------------pinyin4android----------------------------------

#-------------------pinyin4j--------------------------------------------
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}
-keep class com.hp.** { *;}
#-------------------pinyin4j--------------------------------------------

#------------------------zxing--------------------------------------------
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**
#------------------------zxing--------------------------------------------




#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------



#----------------------------------------------------------------------------




#v4包
-dontwarn android.support.**
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment
#v7包

-dontwarn android.support.**
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.v7.app.Fragment


#---------------------------------友盟微社区---------------------------------------
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontpreverify
-dontwarn com.umeng.commm.**
-dontwarn com.umeng.commm.**
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-keep class  activeandroid.** {*;}
-keep class com.umeng.** {*;}
-keep class android.** {*;}
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes Signature


-ignorewarnings
-keep class org.apache.http.** {*;}
-dontwarn  org.apache.http.**
-keep class org.apache.http.* {*;}
-dontwarn  org.apache.http.*
-keep,allowshrinking class org.android.agoo.service.* {
    public <fields>;
    public <methods>;
}
-keep,allowshrinking class com.umeng.message.* {
    public <fields>;
    public <methods>;
}

-keep public class com.umeng.community.example.R$*{
    *;
}

-keep class com.umeng.commm.push.UmengPushImpl {
    public * ;
}
-keep class android.support.v4.** {*;}
-dontwarn android.webkit.WebView

-dontwarn com.tencent.weibo.sdk.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes SourceFile,LineNumberTable
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep public class com.tencent.** {*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

# adding push
-keep class com.umeng.message.* {
        public <fields>;
        public <methods>;
}

-keep class com.umeng.message.protobuffer.* {
        public <fields>;
        public <methods>;
}

-keep class com.squareup.wire.* {
        public <fields>;
        public <methods>;
}

-keep class com.umeng.message.local.* {
        public <fields>;
        public <methods>;
}
-keep class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}

-dontwarn com.xiaomi.**

-dontwarn com.ut.mini.**

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class com.umeng.community.example.R$*{
    public static final int *;
}
-keepattributes Exceptions,InnerClasses,Signature,EnclosingMethod
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*

#分享相关混淆
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes SourceFile,LineNumberTable
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep public class com.tencent.** {*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
#---------------------------------友盟微社区---------------------------------------


#-----------------------------高德地图---------------------------------
#如果有其它包有warning，在报出warning的包加入下面类似的-dontwarn 报名
-dontwarn com.amap.api.**
-dontwarn com.aps.**
# 3D 地图
-keep   class com.amap.api.mapcore.**{*;}
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
-keep   class com.aps.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

#讯飞语音
-keep class com.iflytek.**{*;}

#-----------------------------高德地图---------------------------------

#-----------------------------环信----------------------------------------
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.easemob.**
#2.0.9后的不需要加下面这个keep
#-keep class org.xbill.DNS.** {*;}
#另外，demo中发送表情的时候使用到反射，需要keep SmileUtils
-keep class com.easemob.chatuidemo.utils.SmileUtils {*;}
#注意前面的包名，如果把这个类复制到自己的项目底下，比如放在com.example.utils底下，应该这么写（实际要去掉#）
#-keep class com.example.utils.SmileUtils {*;}
#如果使用EaseUI库，需要这么写
-keep class com.easemob.easeui.utils.EaseSmileUtils {*;}

#2.0.9后加入语音通话功能，如需使用此功能的API，加入以下keep
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**
-keep class org.ice4j.** {*;}
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}
#-----------------------------环信----------------------------------------
