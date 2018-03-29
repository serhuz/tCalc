/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ContextModule {

    private Context mContext;


    public ContextModule(Context context) {
        this.mContext = context;
    }


    @Provides
    @Singleton
    protected Context provideContext() {
        return mContext.getApplicationContext();
    }
}