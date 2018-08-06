package com.duelit.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.duelit.R;

/**
 * Created by romin on 23/2/2016.
 * custom view for displaying a text view with a font
 */
public class FontButton extends Button {

    private final String DEFAULT_FONT = "Nexa_Bold.otf";

    public FontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(attrs);
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(attrs);

    }

    public FontButton(Context context) {
        super(context);
        if (!isInEditMode())
            init(null);
    }

    private void init(AttributeSet attrs) {
        String fontName = "";
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontButton);
            fontName = a.getString(R.styleable.FontButton_FontButtonFontName);

            a.recycle();
        }
        if (fontName == null) {
            fontName = DEFAULT_FONT;
        }
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
        setTypeface(myTypeface);

    }

}