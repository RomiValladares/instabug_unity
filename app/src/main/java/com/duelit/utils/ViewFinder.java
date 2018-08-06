package com.duelit.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.duelit.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by romin on 2/3/2016.
 * utility class for finding views in a viewgroup
 */
public class ViewFinder {
    public static final int RESULT_IMAGE_CROP = 150;
    public static final int RESULT_IMAGE_PICK = 290;
    public static final int RESULT_IMAGE_PICK_AND_CROP = 350;

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static ArrayList<View> getViewsByTagContains(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTagContains((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj instanceof String) {
                String tagString = (String) tagObj;
                if (tagString != null && tagString.contains(tag)) {
                    views.add(child);
                }
            }
        }
        return views;
    }

    /**
     * looks for a view as a direct child of {@param root}
     */
    public static View getViewByTagContains(ViewGroup root, String tag) {
        View views = null;
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (!(child instanceof ViewGroup)) {
                final Object tagObj = child.getTag();
                if (tagObj instanceof String) {
                    String tagString = (String) tagObj;
                    if (tagString != null && tagString.contains(tag)) {
                        views = child;
                    }
                }
            }
        }
        return views;
    }

    /**
     * looks for ANY kind of view that contains tag
     */
    public static View getAnyViewByTagContains(ViewGroup root, String tag) {
        View views = null;
        final int childCount = root.getChildCount();

        if (tagsMatch(root, tag)) {
            return root;
        }

        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);

            if (!(child instanceof ViewGroup)) {
                if (tagsMatch(child, tag)) {
                    views = child;
                }
            } else {
                views = getAnyViewByTagContains((ViewGroup) child, tag);
            }
            if (views != null) {
                break;
            }
        }
        return views;
    }

    private static boolean tagsMatch(View root, String tag) {
        final Object tagObj = root.getTag();
        if (tagObj instanceof String) {
            String tagString = (String) tagObj;
            if (tagString != null && tagString.contains(tag)) {
                return true;
            }
        }
        return false;
    }


    public static List<View> getViewsByType(ViewGroup root, Class viewType) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByType((ViewGroup) child, viewType));
            }

            if (viewType.isInstance(child)) {
                views.add(child);
            }
        }
        return views;
    }

    public static View getNextView(View prevView) {
        View nextView = null;
        if (prevView.getParent() instanceof ViewGroup) {
            ViewGroup root = (ViewGroup) prevView.getParent();
            int nextIndex = root.indexOfChild(prevView) + 1;
            if (root.getChildCount() > nextIndex) {
                nextView = root.getChildAt(nextIndex);
            }
        }
        return nextView;
    }

    public static ArrayAdapter<String> setUpAutoCompleteSpinner(Context c, final AutoCompleteTextView textView, String[] options) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                int color = ContextCompat.getColor(textView.getContext(), R.color.colorPrimaryAlternate);
                textView.setTextColor(color);
                TextUtils.setTypeface(getContext(), textView);
                return textView;
            }
        };

        textView.setAdapter(arrayAdapter);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                textView.showDropDown();
            }
        });

        if (textView.getRootView() instanceof ViewGroup) {
            View nextView = ViewFinder.getNextView(textView);
            if (nextView != null && nextView instanceof ImageView) {
                nextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.showDropDown();
                    }
                });
            }
        }
        return arrayAdapter;
    }

    public static void showImagePicker(android.support.v4.app.Fragment source, int outputX, int outputY) {
        Intent intent = getPickIntent();
        source.startActivityForResult(intent, RESULT_IMAGE_PICK);
    }

    public static void showImagePickerAndCrop(android.support.v4.app.Fragment source, int outputX, int outputY) {
        Intent intent = getPickAndCropIntent(outputX, outputY);
        source.startActivityForResult(intent, RESULT_IMAGE_PICK_AND_CROP);
    }

    // slide the view from below itself to the current position
    public static void slideUp(final View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getTranslationY(),  // fromYDelta
                -view.getHeight());                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public static void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    public static void hideSoftKeyboard(Activity caller) {
        try {
            //check that something has focus, otherwise keyboard is not being shown
            if (caller.getWindow().getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) caller.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(caller.getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * @param outputX must be a valid int, otherwise the called app will give an exception
     * @param outputY must be a valid int, otherwise the called app will give an exception
     */
    public static Intent getPickAndCropIntent(int outputX, int outputY) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);

        return intent;
    }

    /**
     * @return for selecting the image only.
     */
    public static Intent getPickIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        return photoPickerIntent;
    }

    public static Intent getCropIntent(int outputX, int outputY) {
        // Received an image from the picker, now send an Intent for cropping
        final String CROP_ACTION = "com.android.camera.action.CROP";
        Intent intent = new Intent(CROP_ACTION);

        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        return intent;
    }

    public static void showImageCropper(android.support.v4.app.Fragment source, int outputX, int outputY, Uri img) {
        Intent intent = getCropIntent(outputX, outputY, img);
        source.startActivityForResult(intent, RESULT_IMAGE_CROP);
    }

    private static Intent getCropIntent(int outputX, int outputY, Uri img) {
        // Received an image from the picker, now send an Intent for cropping
        final String CROP_ACTION = "com.android.camera.action.CROP";
        Intent intent = new Intent(CROP_ACTION);
        intent.setDataAndType(img, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, final View dataView, final View progressView, Context context) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            if (context != null) {
                int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

                dataView.setVisibility(show ? View.GONE : View.VISIBLE);
                dataView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dataView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                progressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            }
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            dataView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void simulateSwipe(View inst) {
        long downTime = SystemClock.uptimeMillis();
// event time MUST be retrieved only by this way!
        long eventTime = SystemClock.uptimeMillis();
// Just init your variables, or create your own coords logic 🙂
        float xStart = 200;
        float yStart = 200;
// simulating thick finger touch
        float x0 = xStart + 50;
        float y0 = yStart + 50;

        float x1 = x0 + 10;
        float y1 = y0 - 10;

        float x2 = x1 + 10;
        float y2 = y1 + 10;

// move finger to bottom and right
// increment previous coords
        float x3 = 245;
        float y3 = 255;
        float x4 = 247;
        float y4 = 275;
        float x5 = 252;
        float y5 = 300;
// proceed one more movement to bottom and left
        float x6 = 230;
        float y6 = 320;
        float x7 = 200;
        float y7 = 357;
        float x8 = 180;
        float y8 = 380;

        float x9 = 160;
        float y9 = 400;
// release finger, logically to use coords from last movent
        float x10 = x9;
        float y10 = y9;
        try {
// sending event – finger touched the screen
            MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xStart, yStart, 0);
            inst.onTouchEvent(event);
// sending events – finger is moving over the screen
            eventTime = SystemClock.uptimeMillis();

            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x1, y1, 0);
            inst.onTouchEvent(event);

// simulating pause by incrementing eventTime with 1 second
// finger is still touching the screen
            eventTime = SystemClock.uptimeMillis() + 1000;
// moving finger across the screen
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x3, y3, 0);
            inst.onTouchEvent(event);
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x6, y6, 0);
/*
// simulating one more pause
            eventTime = SystemClock.uptimeMillis() + 1000;
// moving finger again
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x7, y7, 0);
            inst.onTouchEvent(event);
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x8, y8, 0);
            inst.onTouchEvent(event);
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x9, y9, 0);
            inst.onTouchEvent(event);
// release finger, gesture is finished
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x10, y10, 0);
            */
            inst.onTouchEvent(event);
        } catch (Exception ex) {
// Handle exceptions if necessary
            ex.printStackTrace();
        }
    }

    /**
     * make sure you have the corresponding permissions before calling this
     *
     * @param caller
     */
    public static void startImageSharingIntent(final Activity caller) {
        Intent i = getScreenshotImageSharingIntent(caller);

        try {
            caller.startActivity(Intent.createChooser(i, "My Profile ..."));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean getPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @return an Intent for sharing a screenshot of caller
     */
    private static Intent getScreenshotImageSharingIntent(Activity caller) {
        Intent i = new Intent(Intent.ACTION_SEND);

        i.setType("image/*");

        i.putExtra(Intent.EXTRA_STREAM, getScreenshotUri(caller));

        return i;
    }

    public static Uri getScreenshotUri(Activity caller) {
        return BitmapUtils.getImageUri(caller, BitmapUtils.takeScreenshot(caller));
    }

    public static void startImageSharingIntent(Activity caller, Uri imgUri, @Nullable String extraText) {
        try {
            Intent chooserIntent = getImageSharingIntent(caller, imgUri, extraText);
            caller.startActivity(chooserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Intent getImageSharingIntent(Activity caller, Uri imgUri, String extraText) {
        List<Intent> targetedShareIntents = getPossibleSharingIntents(caller);
        return getImageSharingIntent(targetedShareIntents, imgUri, extraText);
    }

    /**
     * @return an intent ready to pass to a chooser startactivity
     */
    public static Intent getImageSharingIntent(List<Intent> targetedShareIntents, Uri imgUri, String extra) {
        try {
            if (targetedShareIntents != null && !targetedShareIntents.isEmpty()) {
                for (Intent info : targetedShareIntents) {
                    info.putExtra(Intent.EXTRA_STREAM, imgUri);
                    if (extra != null) {
                        info.putExtra(Intent.EXTRA_TEXT, extra);
                    }
                }
                ArrayList<Intent> targetedShareIntentsCopy = new ArrayList<>(targetedShareIntents);

                Intent chooserIntent = Intent.createChooser(targetedShareIntentsCopy.remove(0), "Share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntentsCopy.toArray(new Parcelable[]{}));

                return chooserIntent;
            } else {
                //default:show all apps
                Intent intentTemplate = new Intent(Intent.ACTION_SEND);
                intentTemplate.setType("image/*");
                intentTemplate.putExtra(Intent.EXTRA_STREAM, imgUri);
                if (extra != null) {
                    intentTemplate.putExtra(Intent.EXTRA_TEXT, extra);
                }

                return intentTemplate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return a list of intent of type image, without any extra data,
     * whose package name are in possiblePackagesNames
     */
    public static List<Intent> getPossibleSharingIntents(Activity caller) {
        try {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent intentTemplate = new Intent(Intent.ACTION_SEND);
            intentTemplate.setType("image/*");

            List<ResolveInfo> resInfo = caller.getPackageManager().queryIntentActivities(intentTemplate, 0);

            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                    targetedShare.setType("image/*");

                    targetedShare.setPackage(info.activityInfo.packageName);
                    //adding bottom line puts the app name in the intent
                    targetedShare.setClassName(info.activityInfo.packageName, info.activityInfo.name);

                    targetedShareIntents.add(targetedShare);
                }

                return targetedShareIntents;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Activity getForegroundActivity() {
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
