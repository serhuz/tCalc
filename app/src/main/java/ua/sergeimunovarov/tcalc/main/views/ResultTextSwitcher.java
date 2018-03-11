/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.Context;
import android.util.AttributeSet;

import ua.sergeimunovarov.tcalc.R;


public class ResultTextSwitcher extends TextSwitcher {

    public ResultTextSwitcher(Context context) {
        super(context);
    }


    public ResultTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected int getFactoryLayoutResource() {
        return R.layout.view_result;
    }
}
