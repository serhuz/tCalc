/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import ua.sergeimunovarov.tcalc.Application;


/**
 * A {@link Button} subclass with custom font.
 */
@SuppressLint("AppCompatCustomView")
public class CustomButton extends Button {

    public CustomButton(Context context) {
        super(context);
    }


    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    {
        init();
        // Remove shadow under button on devices with Lollipop and newer versions of Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setStateListAnimator(null);
        }
    }


    private void init() {
        Typeface tf = Application.getAppComponent().typefaceHolder().getTypeface();
        this.setTypeface(tf, Typeface.BOLD);
    }
}
