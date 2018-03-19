/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.databinding.BindingAdapter;
import android.widget.TextView;


public final class TextViewBindingAdapters {

    private TextViewBindingAdapters() {
        throw new AssertionError();
    }


    @BindingAdapter({"formats", "formatId"})
    public static void setStringFromArray(TextView view,
                                          String[] formats,
                                          int formatId) {
        view.setText(formats[formatId]);
    }
}
