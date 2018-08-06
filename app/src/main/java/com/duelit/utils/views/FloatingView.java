package com.duelit.utils.views;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;

import com.melnykov.fab.FloatingActionButton;

import com.duelit.R;

/**
 * Created by romin on 29/2/2016.
 */
public class FloatingView extends FloatingActionButton {
    public FloatingView(Context context) {
        super(context);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //TODO
        //int size = getDimension(
        //        mType == TYPE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
        int width = getDimension(R.dimen.friends_button_width);
        int height = getDimension(R.dimen.friends_button_height);

        setMeasuredDimension(width, height);
    }

    private int getDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }
}
