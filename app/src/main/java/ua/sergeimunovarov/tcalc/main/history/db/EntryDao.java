/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface EntryDao {

    @Query("SELECT * FROM history ORDER BY ts DESC")
    LiveData<List<Entry>> getAll();

    @Insert
    void insertItem(Entry item);

    @Query("DELETE FROM history")
    void deleteAll();

    @Query("SELECT * FROM history ORDER BY ts DESC LIMIT 1")
    Entry getLast();
}
