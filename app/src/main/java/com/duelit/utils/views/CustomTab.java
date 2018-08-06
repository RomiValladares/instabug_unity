package com.duelit.utils.views;

import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * Created by romin on 23/2/2016.
 */
public class CustomTab {
    private int unselectedIcon;
    private int selectedIcon;
    private String text;
    private int selectedTextColor;
    private int unselectedTextColor;
    private View view;

    public CustomTab(String text, int unselectedIcon, int selectedIcon, int unselectedTextColor, int selectedTextColor) {
        this.text = text;

        this.unselectedIcon = unselectedIcon;
        this.selectedIcon = selectedIcon;

        this.unselectedTextColor = unselectedTextColor;
        this.selectedTextColor = selectedTextColor;
    }

    public CustomTab(String text) {
        this(text, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public int getUnselectedIcon() {
        return unselectedIcon;
    }

    public void setUnselectedIcon(@DrawableRes int unselectedIcon) {
        this.unselectedIcon = unselectedIcon;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(@DrawableRes int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public int getUnselectedTextColor() {
        return unselectedTextColor;
    }

    public void setView(View view) {
        this.view = view;
    }
}
