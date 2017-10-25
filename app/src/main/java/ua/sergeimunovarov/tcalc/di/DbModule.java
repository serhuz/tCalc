/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDbHelper;


@Module
public class DbModule {

    @Provides
    @Singleton
    protected HistoryDbHelper provideHistoryDbHelper(Context context) {
        return new HistoryDbHelper(context.getApplicationContext());
    }
}
