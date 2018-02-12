/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.util.List;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.main.BaseObservableViewModel;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;


public class HistoryBottomSheetViewModel extends BaseObservableViewModel {

    public final ObservableList<Entry> mEntries = new ObservableArrayList<>();
    public final LiveData<List<Entry>> mLiveHistoryItems;

    @Inject
    EntryDao mDao;


    public HistoryBottomSheetViewModel() {
        Application.getAppComponent().inject(this);
        mLiveHistoryItems = mDao.getAll();
    }
}
