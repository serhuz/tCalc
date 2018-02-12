/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface EntryDao {

    @Query("SELECT * FROM history ORDER BY ts DESC")
    LiveData<List<Entry>> getAll();

    @Insert
    void insertItem(Entry item);

    @Query("DELETE FROM history")
    void deleteAll();
}
