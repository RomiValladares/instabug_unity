package com.duelit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;

import com.duelit.utils.NetworkChangeReceiver;
import com.instabug.library.Instabug;
import com.instabug.library.InstabugColorTheme;
import com.instabug.library.InstabugCustomTextPlaceHolder;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.ui.onboarding.WelcomeMessage;
import com.instacart.library.truetime.TrueTime;

import net.danlew.android.joda.JodaTimeAndroid;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * Created by romin on 22/2/2016.
 */
public class AppController extends android.support.multidex.MultiDexApplication {
    private static final String TAG = "AppController";
    private static AppController mInstance;
    private static Context mAppContext;
    private int trueTimeRetries = 0;

    public static AppController getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {
        AppController.mAppContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        this.setAppContext(getApplicationContext());

        // You can change the invocation event to NONE, FLOATING_BUTTON,
        // SCREENSHOT_GESTURE, or TWO_FINGER_SWIPE_LEFT.
//
//        new Instabug.Builder(this, "f236fec69d216c88416b4fdc59222b6e")
        new Instabug.Builder(this, "48ad905e141bc665d064945f423aa414")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT_GESTURE,
                        InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT)
                .build();

        //Choosing instabug theme
        Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeLight);
        //Choosing type of attachments allowed
        //1. initial screenshot, 2. extra screenshot, 3. image from gallery, 4. voice note
        //5. screen record
        Instabug.setWelcomeMessageState(WelcomeMessage.State.LIVE);
        Instabug.setLocale(Locale.ENGLISH);

        //To show instabug debug logs if necessary
        Instabug.setDebugEnabled(true);

        //Settings custom strings to replace instabug's strings
        InstabugCustomTextPlaceHolder placeHolder = new InstabugCustomTextPlaceHolder();
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.REPORT_FEEDBACK, "Send Feedback");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.REPORT_BUG, "Send Bug Report");

        Instabug.setCustomTextPlaceHolders(placeHolder);

        //Instabug.setUserAttribute("USER_TYPE", "instabug user");

        Instabug.setAutoScreenRecordingEnabled(true);

        Locale.setDefault(Locale.ENGLISH);
        //JODA
        JodaTimeAndroid.init(this);

        if (BuildConfig.DEBUG) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Log.d(TAG, "device DisplayMetrics " + metrics.toString());
            Configuration config = getResources().getConfiguration();

            Log.d(TAG, "device Configuration " + metrics.toString());
        }

        initializeTrueTime(getBaseContext());
    }

    private void initializeTrueTime(final Context context) {
        trueTimeRetries++;

        if (trueTimeRetries < 60) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        TrueTime.build().withSharedPreferences(context).withNtpHost("time.apple.com").initialize();
                    } catch (IOException e) {
                        e.printStackTrace();
                        initializeTrueTime(context);
                    }
                }
            };

            thread.start();
        }
    }

    public boolean isOfflineOverlayShown() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            int overlayId = R.id.no_connection_overlay;
            Window window = currentActivity.getWindow();
            ViewGroup rootView = (ViewGroup) window.findViewById(overlayId);
            if (rootView != null && rootView.getParent() instanceof ViewGroup)
                return true;
        }
        return false;
    }

    public Activity getCurrentActivity() {
        return getActivity();
    }

    private Activity getActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null)
                return null;

            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
