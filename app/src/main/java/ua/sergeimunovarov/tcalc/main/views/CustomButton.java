/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import ua.sergeimunovarov.tcalc.R;


/**
 * An {@link AppCompatButton} subclass with custom font.
 */
public class CustomButton extends AppCompatButton {

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
        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.lato_light);
        this.setTypeface(tf, Typeface.BOLD);
    }
}
