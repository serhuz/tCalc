/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.content.Context;
import android.os.Vibrator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.VIBRATOR_SERVICE;


@Module
public class ApplicationModule {

    private Context mContext;


    public ApplicationModule(Context context) {
        mContext = context;
    }


    @Provides
    @Singleton
    protected Context provideContext() {
        return mContext.getApplicationContext();
    }


    @Provides
    protected Vibrator provideVibrator() {
        return (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
    }
}
