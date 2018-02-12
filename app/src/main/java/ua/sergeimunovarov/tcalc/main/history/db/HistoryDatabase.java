/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = Entry.class, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {

    public abstract EntryDao roomHistoryItemDao();
}
