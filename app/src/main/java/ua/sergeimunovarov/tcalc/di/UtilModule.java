/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.sergeimunovarov.tcalc.main.views.TypefaceHolder;


@Module
public class UtilModule {

    @Provides
    @Singleton
    protected TypefaceHolder provideTypefaceHolder(Context context) {
        return TypefaceHolder.create(context);
    }
}
