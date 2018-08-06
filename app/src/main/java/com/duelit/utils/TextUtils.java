package com.duelit.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.android.device.DeviceName;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by romin on 22/2/2016.
 */
public class TextUtils {

    public static void setTypeface(Context c, String fontFile, View... params) {
        Typeface myTypeface = getAppTypeface(c);
        for (View v : params) {
            if (v instanceof TextView) {
                TextView finalV = (TextView) v;
                finalV.setTypeface(myTypeface);
            } else if (v instanceof Button) {
                Button finalV = (Button) v;
                finalV.setTypeface(myTypeface);
            } else if (v instanceof EditText) {
                EditText finalV = (EditText) v;
                finalV.setTypeface(myTypeface);
            }
        }
    }

    public static Typeface getAppTypeface(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/" + "Nexa_Bold.otf");
    }

    public static void setTypeface(Context c, View... params) {
        setTypeface(c, "Nexa_Bold.otf", params);
    }

    public static String trim(String s) {
        return s.replace(String.valueOf((char) 160), " ").trim();
    }

    public static boolean isValidEmail(String email) {
        return !android.text.TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void setTypeface(Context c, List<View> views) {
        setTypeface(c, "Nexa_Bold.otf", views.toArray(new View[views.size()]));
    }

    public static boolean isValidPassword(String pass) {
        if (android.text.TextUtils.isEmpty(pass))
            return false;
        if (pass.length() < 8)
            return false;
        return containsDigit(pass);
    }

    public static final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static String getNumberAsOrdinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }

    /**
     * @return a valid html string with bold styling, using {@param textColor}
     */
    public static String makeBoldHtml(String text, @ColorRes int textColor, Context context) {
        String formattedText = "<b>";
        formattedText += makeRegularHtml(text, textColor, context);
        formattedText += "</b>";

        return formattedText;
    }

    /**
     * @return a valid html string using {@param textColor}
     */
    public static String makeRegularHtml(String text, @ColorRes int textColor, Context context) {
        String formattedText = "";
        if (textColor > 0) {
            formattedText += "<font color='#" + BitmapUtils.getHexColor(textColor, context) + "'>";
        }
        formattedText += text;
        //TODO improve
        if (textColor > 0) {
            formattedText += "</font>";
        }

        return formattedText;
    }

    public static String[] getISOCountries() {
        //getResources().getStringArray(R.array.countries);

        String[] isoCountryCodes = Locale.getISOCountries();
        String[] countriesNames = new String[isoCountryCodes.length];

        int i = 0;
        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry();
            countriesNames[i++] = countryName;
        }

        return countriesNames;
    }

    /**
     * checks if the string has letters with accent marks or anything that doesnt match a-z
     *
     * @return
     */
    public static boolean hasSpecialCharacters(String text) {
        if (text != null && !text.isEmpty()) {
            Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(text);
            boolean hasSpecialChars = m.find();

            return hasSpecialChars;
        }
        return false;
    }

    /*  public static String getPreferredDateFormat(Context context) {
        if (android.text.TextUtils.isEmpty(format)) {
            Locale current = context.getResources().getConfiguration().locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                format = android.text.format.DateFormat.getBestDateTimePattern(current, "");
            }else{
                format = android.text.format.DateFormat.getMediumDateFormat(context).
            }
        }

        return dateFormat;
    }*/

    public static Locale getCurrentLocale(Context c) {
        return c.getResources().getConfiguration().locale;
    }

    public static DateFormat getPreferredDateFormat(Context context) {
        String format = Settings.System.getString(context.getContentResolver(), Settings.System.DATE_FORMAT);
        DateFormat dateFormat;
        if (android.text.TextUtils.isEmpty(format)) {
            dateFormat = android.text.format.DateFormat.getMediumDateFormat(context.getApplicationContext());
        } else {
            dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext()); // Gets system date format
        }

        if (dateFormat == null)
            dateFormat = new SimpleDateFormat(format);

        return dateFormat;
    }

    public static String getPreferredDateTimeFormatString(Context context, List<DateFields> dateFields) {
        String dateFormat = getPreferredDateFormatString(context, dateFields);
        String timeFormat = getPreferredTimeFormatString(context, dateFields);

        return dateFormat + " " + timeFormat;
    }

    public static String getPreferredDateFormatString(Context context, List<DateFields> dateFields) {
        Format dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());
        return getStringFromDateFormat(dateFormat, dateFields);
    }

    public static String getPreferredTimeFormatString(Context context, List<DateFields> dateFields) {
        Format dateFormat = android.text.format.DateFormat.getTimeFormat(context.getApplicationContext());
        return getStringFromDateFormat(dateFormat, dateFields);
    }

    private static String getStringFromDateFormat(Format dateFormat, List<DateFields> dateFields) {
        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

        if (dateFields != null) {
            for (DateFields field : DateFields.values()) {
                if (!dateFields.contains(field)) {
                    pattern = pattern.replaceAll(field.code, "");
                }
            }
        }

        //matches any leftover slashes
        String trailingSlashRegx = "^\\/|\\/$";

        return pattern.replaceAll(trailingSlashRegx, "");
    }

    /**
     * returns a format for month + day
     */
    public static String getPreferredSimpleDateFormat(Context context) {
        String result = getPreferredDateFormatString(context, new ArrayList<DateFields>(
                Arrays.asList(TextUtils.DateFields.MONTH, TextUtils.DateFields.DAY)));
        return result;
    }

    /**
     * @return the date separator which is most likely a /, or .
     */
    public static String getDateSeparator() {
        String dateString = DateFormat.getInstance().format(new java.util.Date());
        Matcher matcher = Pattern.compile("[^\\w]").matcher(dateString);
        if (!matcher.find()) return null;
        return matcher.group(0);
    }

    public static String formatWithoutDecimals(double value) {
        return formatWithDecimals(value, 0);
    }

    public static String formatWithDecimals(double value, int cantDecimals) {
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(cantDecimals);

        return nf.format(value);
    }

    public static String removeExtraSpaces(String text) {
        if (text != null) {
            return text.trim().replaceAll("\\s+", " ");
        }
        return text;
    }

    public static boolean isAnyStringNullOrEmpty(String... strings) {
        for (String s : strings)
            if (s == null || s.trim().isEmpty())
                return true;
        return false;
    }

    public static String getDeviceName() {
        return DeviceName.getDeviceName();
    }

    public enum DateFields {
        MONTH("M"),
        DAY("d"),
        YEAR("y"),
        HOUR("h"),
        MINUTE("m"),
        SECOND("s");

        private final String code;

        DateFields(String s) {
            code = s;
        }

        public boolean equalsName(String otherName) {
            return otherName != null && code.equals(otherName);
        }

        public String toString() {
            return this.code;
        }
    }
}
