package com.duelit.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.duelit.AppController;
import com.duelit.R;
import com.duelit.model.Game;
import com.duelit.ui.game.playgame.PlayGameActivity;
import com.greysonparrelli.permiso.Permiso;

/**
 * Created by romin on 3/3/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected boolean confirmedBack = false;
    private int openedDialogs;
    private AlertDialog currentDialog;

    public void showAlertDialog(Activity caller, String title,
                                String message, DialogInterface.OnClickListener acceptListener,
                                DialogInterface.OnClickListener cancelListener,
                                String btnCancelText, String btnConfirmText, boolean cancelable) {
        if (!caller.isFinishing()) {
            if (!hasOpenedDialogs()) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(caller, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(title);
                builder.setMessage(message);
                if (!TextUtils.isEmpty(btnCancelText)) {
                    builder.setNegativeButton(btnCancelText, cancelListener);
                }
                if (!TextUtils.isEmpty(btnConfirmText)) {
                    builder.setPositiveButton(btnConfirmText, acceptListener);
                }
                builder.setCancelable(cancelable);

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        openedDialogs--;
                        currentDialog = null;
                    }
                });

                currentDialog = builder.create();
                builder.show();

                openedDialogs++;

                currentDialog.setCanceledOnTouchOutside(cancelable);
                currentDialog.setCancelable(cancelable);
            }
        }
    }


    public void showErrorDialog(String msg) {
        showErrorDialog(msg, null);
    }

    public void showErrorDialog(String msg, DialogInterface.OnClickListener onOkListener) {
        showAlertDialog(this, "Error", msg, null, onOkListener, "Ok", null, false);
    }


    private void closeCurrentDialog() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
    }

    /**
     * @param btnCancelText  pass null to not show a button; pass text to display with received text
     * @param btnConfirmText pass null to not show a button; pass text to display with received text
     */
    public void showAlertDialog(String title, String message, DialogInterface.OnClickListener listener, String btnCancelText, String btnConfirmText, boolean cancelable) {
        showAlertDialog(this, title, message, listener, null, btnCancelText, btnConfirmText, cancelable);
    }


    protected View getToolbarBackButton() {
        return toolbar.findViewById(R.id.btn_toolbar_back);
    }

    protected void setBackToolbar(@ColorInt int toolbarColor) {
        setBackToolbar();
        toolbar.setBackgroundColor(toolbarColor);
    }

    /**
     * sets a toolbar without altering its color
     */
    protected void setBackToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getToolbarBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (AppController.getInstance().isOfflineOverlayShown()) {
            return;
        }
        super.onBackPressed();
        confirmedBack = false;
    }

    public void goToLogin(String email) {

    }


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Permiso.getInstance().setActivity(this);
    }

    public void trackScreenView(String screenName) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        Permiso.getInstance().setActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private boolean hasOpenedDialogs() {
        Log.d("openedDialogs", "openedDialogs = " + openedDialogs);
        return openedDialogs > 0;
    }

    public void goToPlayGameActivity(Game game, PlayGameActivity.GameMode gameMode) {
        final Intent i = new Intent(this, PlayGameActivity.class);
        i.putExtra(PlayGameActivity.BUNDLE_GAME, game);
        i.putExtra(PlayGameActivity.BUNDLE_GAME_MODE, gameMode);

        goToPlayGameActivity(i, null);
    }


    private void goToPlayGameActivity(final Intent i, final OnPlayGameActivityListener listener) {
        if (listener != null) {
            listener.onPlayGameActivityStarted();
        }
        startActivity(i);
    }


    public interface OnPlayGameActivityListener {

        void onPlayGameActivityStarted();
    }
}
