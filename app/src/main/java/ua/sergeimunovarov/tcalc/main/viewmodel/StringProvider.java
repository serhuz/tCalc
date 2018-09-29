/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;


public class StringProvider {

    private final Context mContext;


    public StringProvider(Context context) {
        mContext = context;
    }


    public String provideFormattedString(@StringRes int stringId, @NonNull String arg) {
        return mContext.getString(stringId, arg);
    }
}
