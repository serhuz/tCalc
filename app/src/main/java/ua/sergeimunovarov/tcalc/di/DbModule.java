/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.sergeimunovarov.tcalc.BuildConfig;
import ua.sergeimunovarov.tcalc.main.history.db.DbOnCreateCallback;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDatabase;


@Module
public class DbModule {

    @Provides
    @Singleton
    protected ExecutorService provideIOExecutor() {
        return Executors.newSingleThreadExecutor();
    }


    @Provides
    @Singleton
    protected HistoryDatabase provideHistoryDatabase(Context context) {
        return Room.databaseBuilder(
                context,
                HistoryDatabase.class,
                BuildConfig.DB_NAME
        ).addCallback(new DbOnCreateCallback()).build();
    }


    @Provides
    @Singleton
    protected EntryDao provideRoomHistoryItemDao(HistoryDatabase historyDatabase) {
        return historyDatabase.roomHistoryItemDao();
    }
}
