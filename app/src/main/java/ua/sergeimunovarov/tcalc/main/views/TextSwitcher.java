/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.LayoutRes;


public abstract class TextSwitcher extends android.widget.TextSwitcher {

    private boolean mInitialized = false;


    {
        // When setting factory using binding adapter,
        // generated binding actually sets factory after applying text from view model.
        // This behavior results in NPE, so setting factory inside the view itself
        // is a workaround for it.
        setFactory(new TextViewFactory(this, getFactoryLayoutResource()));
    }


    public TextSwitcher(Context context) {
        super(context);
    }


    public TextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setText(CharSequence text) {
        if (mInitialized) {
            super.setText(text);
        } else {
            super.setCurrentText(text);
            mInitialized = true;
        }
    }


    @LayoutRes
    protected abstract int getFactoryLayoutResource();
}
