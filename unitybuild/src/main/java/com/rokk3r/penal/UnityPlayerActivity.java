package com.rokk3r.penal;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import com.unity3d.player.UnityPlayer;

public class UnityPlayerActivity extends Activity {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        if (mUnityPlayer == null) {
            mUnityPlayer = new UnityPlayer(this);
            setContentView(mUnityPlayer);
            mUnityPlayer.requestFocus();
        }
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("instabug", "UnityPlayerActivity.dispatchKeyEvent: " + event);
        //ignore back pressed!
        if (shouldHandleEvent(event))
            if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
                return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    /**
     * @return false if you want KEYCODE_BACK to call onBackPressed
     */
    protected boolean shouldHandleBackPressed() {
        return false;
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("instabug", "UnityPlayerActivity.onKeyUp: " + event);
        if (shouldHandleEvent(event))
            return mUnityPlayer.injectEvent(event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("instabug", "UnityPlayerActivity.onKeyDown: " + event);
        if (shouldHandleEvent(event))
            return mUnityPlayer.injectEvent(event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("instabug", "UnityPlayerActivity.onTouchEvent: " + event);
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        Log.d("instabug", "UnityPlayerActivity.onGenericMotionEvent: " + event);
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean shouldHandleEvent(KeyEvent event) {
        //ignore back pressed!
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return shouldHandleBackPressed();
        }
        return true;
    }
}
