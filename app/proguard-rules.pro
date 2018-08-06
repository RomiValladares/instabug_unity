-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
# remove logging
-assumenosideeffects class android.util.Log {
    *;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-keep class java.applet.** { *; }
-dontwarn java.applet.**

-keep class java.awt.** { *; }
-dontwarn java.awt.**

-keep class org.w3c.dom.bootstrap.** { *; }
-dontwarn org.w3c.dom.bootstrap.**

-keep class org.dom4j.** { *; }
-dontwarn org.dom4j.**

-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**

-keep class org.apache.commons.** { *; }
-dontwarn org.apache.commons.**

-keep class com.sun.jdi.** { *; }
-dontwarn com.sun.jdi.**

-keep class javax.swing.** { *; }
-dontwarn javax.swing.**

-keep class sun.misc.** { *; }
-dontwarn sun.misc.**

-keep class javax.servlet.** { *; }
-dontwarn javax.servlet.**

-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**

-keep class in.intellicode.webservices.models.** { *; }
-keep class in.intellicode.models.** { *; }
-keep class in.intellicode.events.*{ *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

-keep class com.appsee.** { *; }
-dontwarn com.appsee.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-keepattributes SourceFile,LineNumberTable