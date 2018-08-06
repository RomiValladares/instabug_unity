package com.duelit.utils.views;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * Created by Romina on 8/31/16.
 */
public class MatrixImageView extends ImageView {

    public MatrixImageView(Context context) {
        super(context);
    }

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatrixImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("anim", "onMeasure");
        //centerAndFillImageView(this, this.getMatrix());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        /*if (visibility == View.VISIBLE) {
            Log.d("anim", "onWindowVisibilityChanged");
            centerAndFillImageView(this, this.getMatrix());
        }*/
    }

    private void centerAndFillImageView(final ImageView imageView, final Matrix imageMatrix) {
        if (imageView.getDrawable() != null) {
            Matrix newMatrix = getCenterAndFillMatrix(imageView, imageMatrix);

            imageView.setImageMatrix(newMatrix);
            imageView.invalidate();
        }
    }

    private Matrix getCenterAndFillMatrix(final ImageView imageView, final Matrix imageMatrix) {
        //arbitrary number to make sure that no whitespace will be shown when scrolling
        int arbitraryDiff = 40;

        int drawableWidth = imageView.getDrawable().getIntrinsicWidth() - arbitraryDiff;
        int drawableHeight = imageView.getDrawable().getIntrinsicHeight() - arbitraryDiff;

        int viewWidth = imageView.getMeasuredWidth();
        int viewHeight = imageView.getMeasuredHeight();

        float scaleWidth = (float) viewWidth / (float) drawableWidth;
        float scaleHeight = (float) viewHeight / (float) drawableHeight;

        //to preserve ratio, must use the same scale
        float maxScale = Math.max(scaleWidth, scaleHeight);

        Matrix newMatrix = new Matrix();
        newMatrix.set(imageMatrix);

        newMatrix.preScale(maxScale, maxScale);

        TypedValue outValue = new TypedValue();
        //getContext().getResources().getValue(com.example.recyclerviewparallax.R.dimen.parallax_translate_y, outValue, true);
        float value = outValue.getFloat();
        newMatrix.postTranslate(-20, value);

        return newMatrix;
    }
}
