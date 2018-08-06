package com.duelit.utils;

import android.util.Log;

import com.android.volley.VolleyError;

import com.duelit.BuildConfig;

/**
 * Created by Romina on 6/2/16.
 */
public class LogHelper {

    private static final String TAG = "LogHelper";
    private static final String NETWORK_TAG = "NETWORK";
    private static final String NO_TAG = "NO_TAG";

    public static void onLogin(String mail, String pass) {
        Log.d(TAG, "onLogin");
        /*if (isRelease()) {
            Bugfender.setDeviceString("user.email", mail);
            Bugfender.setDeviceString("user.pass", pass);
        }*/
    }

    private static boolean isRelease() {
        return !BuildConfig.DEBUG;
    }


    public static void w(String tag, String title, Exception e) {
        w(tag, getExceptionMessage(title, e));
    }

    public static void w(VolleyError error) {
        w(NETWORK_TAG, error.getMessage());
    }

    public static void w(String message) {
        w(NO_TAG, message);
    }

    public static void w(String tag, String message) {
        if (tag != null && !tag.isEmpty() && message != null && !message.isEmpty())
            /*if (isRelease()) {
                Bugfender.w(tag, message);
            } else {*/
                Log.d(tag, message);
            //}
    }


    public static void w(String tag, Exception e) {
        String message = getExceptionMessage(e);
        /*if (isRelease()) {
            Bugfender.w(tag, message);
        } else {*/
            Log.d(tag, message);
        //}
    }


    public static void e(Exception e) {
        /*if (isRelease()) {
            Bugfender.e(NO_TAG, getExceptionMessage(e));
        } else {*/
            e.printStackTrace();
        //}
    }

    public static void e(String title, Exception ex) {
        /*if (isRelease()) {
            Bugfender.e(NO_TAG, getExceptionMessage(title, ex));
        } else {*/
            ex.printStackTrace();
        //}
    }

    private static String getExceptionMessage(Exception e) {
        return getExceptionMessage("", e);
    }

    private static String getExceptionMessage(String title, Exception e) {
        String error = title + " ";
        if (e != null) {
            error += e.toString();
            error += Log.getStackTraceString(e);
        }
        return error;
    }
}
