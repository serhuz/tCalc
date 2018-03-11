/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.Context;
import android.util.AttributeSet;

import ua.sergeimunovarov.tcalc.R;


public class FormatTextSwitcher extends TextSwitcher {

    public FormatTextSwitcher(Context context) {
        super(context);
    }


    public FormatTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected int getFactoryLayoutResource() {
        return R.layout.view_indicator_format;
    }
}
