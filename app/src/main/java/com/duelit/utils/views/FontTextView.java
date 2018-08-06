package com.duelit.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.duelit.R;

/**
 * Created by romin on 23/2/2016.
 * custom view for displaying a text view with a font
 */
public class FontTextView extends TextView {

    private final String DEFAULT_FONT = "Nexa_Bold.otf";

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(attrs);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(attrs);

    }

    public FontTextView(Context context) {
        super(context);
        if (!isInEditMode())
            init(null);
    }

    private void init(AttributeSet attrs) {
        String fontName = "";
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
            fontName = a.getString(R.styleable.FontTextView_fontName);

            a.recycle();
        }
        if (fontName == null) {
            fontName = DEFAULT_FONT;
        }
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
        setTypeface(myTypeface);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fit) shrinkToFit();
    }


    protected void shrinkToFit() {
        try {
            int height = this.getHeight();
            int lines = this.getLineCount();
            Rect r = new Rect();
            int y1 = this.getLineBounds(0, r);
            int y2 = this.getLineBounds(lines - 1, r);

            float size = this.getTextSize();
            if (y2 > height && size >= 8.0f) {
                this.setTextSize(size - 2.0f);
                shrinkToFit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    boolean fit = false;

    public void setFitTextToBox(Boolean fit) {
        this.fit = fit;
    }

}