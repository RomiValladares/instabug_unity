package com.duelit.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.duelit.R;

/**
 * Created by romin on 8/3/2016.
 */
public class BackNextBottomBar extends LinearLayout implements View.OnClickListener {
    private boolean nextIsEnabled;
    private boolean backIsEnabled;
    private OnBackNextBottomBarClick listener;

    public enum BackNextBottomBarMode {NEXT_ONLY, BACK_ONLY, NEXT_AND_BACK}

    public enum BackNextBottomBarButton {NEXT, BACK}

    private BackNextBottomBarMode barMode;

    public BackNextBottomBar(Context context) {
        super(context);
        init(context);
    }

    public BackNextBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BackNextBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.bottom_bar_next, this, true);

        findViewById(R.id.btn_bottom_bar_back).setOnClickListener(this);
        findViewById(R.id.btn_bottom_bar_back_txt).setOnClickListener(this);

        findViewById(R.id.btn_bottom_bar_next).setOnClickListener(this);
        findViewById(R.id.btn_bottom_bar_next_txt).setOnClickListener(this);
    }

    public void setListener(OnBackNextBottomBarClick listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int id = v.getId();
            if (id == R.id.btn_bottom_bar_back || id == R.id.btn_bottom_bar_back_txt) {
                listener.onButtonClicked(findViewById(R.id.btn_bottom_bar_back), BackNextBottomBarButton.BACK, backIsEnabled);
            } else if (id == R.id.btn_bottom_bar_next || id == R.id.btn_bottom_bar_next_txt) {
                listener.onButtonClicked(findViewById(R.id.btn_bottom_bar_next), BackNextBottomBarButton.NEXT, nextIsEnabled);
            }
        }
    }

    public void setBarMode(BackNextBottomBarMode barMode) {
        this.barMode = barMode;
        refresh();
    }

    private void refresh() {
        switch (barMode) {
            case NEXT_ONLY:
                toggleBackVisibility(false);
                toggleNextVisibility(true);
                break;
            case BACK_ONLY:
                toggleBackVisibility(true);
                toggleNextVisibility(false);
                break;
            case NEXT_AND_BACK:
                toggleBackVisibility(true);
                toggleNextVisibility(true);
                break;
        }
    }

    public void toggleBackVisibility(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        findViewById(R.id.btn_bottom_bar_back).setVisibility(visibility);
        findViewById(R.id.btn_bottom_bar_back_txt).setVisibility(visibility);
    }

    public void toggleNextVisibility(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        findViewById(R.id.btn_bottom_bar_next).setVisibility(visibility);
        findViewById(R.id.btn_bottom_bar_next_txt).setVisibility(visibility);
    }

    public void toggleButtonState(BackNextBottomBarButton btn, boolean enable) {
        if (btn == BackNextBottomBarButton.BACK) {
            enableBack(enable);
        } else {
            enableNext(enable);
        }
    }

    public void enableNext(boolean enable) {
        nextIsEnabled = enable;
    }

    public void enableBack(boolean enable) {
        backIsEnabled = enable;
    }

    public void enableBoth() {
        enableBack(true);
        enableNext(true);
    }

    public interface OnBackNextBottomBarClick {
        void onButtonClicked(View v, BackNextBottomBarButton btn, boolean isEnabled);
    }
}
