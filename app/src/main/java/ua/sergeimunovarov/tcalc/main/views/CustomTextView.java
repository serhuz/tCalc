/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ua.sergeimunovarov.tcalc.Application;


/**
 * A {@link TextView} subclass with custom font.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
    }


    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    {
        init();
    }


    private void init() {
        Typeface tf = Application.getAppComponent().typefaceHolder().getTypeface();
        this.setTypeface(tf, Typeface.BOLD);
    }
}
