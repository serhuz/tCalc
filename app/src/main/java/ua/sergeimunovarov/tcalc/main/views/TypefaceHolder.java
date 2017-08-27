/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TypefaceHolder {

    private static final String TYPEFACE_LATO_LIGHT = "fonts/Lato-Light.ttf";

    public static TypefaceHolder create(@NonNull Context context) {
        return new AutoValue_TypefaceHolder(
                Typeface.createFromAsset(context.getAssets(), TYPEFACE_LATO_LIGHT)
        );
    }

    @NonNull
    public abstract Typeface getTypeface();
}
