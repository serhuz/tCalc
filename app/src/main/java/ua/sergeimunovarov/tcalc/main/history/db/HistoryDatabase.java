/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = Entry.class, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {

    public abstract EntryDao roomHistoryItemDao();
}
