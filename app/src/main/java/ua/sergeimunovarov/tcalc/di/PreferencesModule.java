/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;

@Module
public class PreferencesModule {

    @Provides
    @Singleton
    protected ApplicationPreferences provideApplicationPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new ApplicationPreferences(sharedPreferences);
    }
}
