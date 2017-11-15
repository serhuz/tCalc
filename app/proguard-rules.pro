-keep class ua.sergeimunovarov.tcalc.*

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class **.R$*

-dontwarn java.lang.invoke.*

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
