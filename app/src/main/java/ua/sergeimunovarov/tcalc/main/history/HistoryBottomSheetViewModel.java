/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import java.util.List;

import ua.sergeimunovarov.tcalc.main.BaseObservableViewModel;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;


public class HistoryBottomSheetViewModel extends BaseObservableViewModel {

    public final ObservableList<Entry> mEntries = new ObservableArrayList<>();
    public final LiveData<List<Entry>> mLiveHistoryItems;


    @SuppressWarnings("WeakerAccess")
    public HistoryBottomSheetViewModel(@NonNull EntryDao dao) {
        mLiveHistoryItems = dao.getAll();
    }


    public static class Factory implements ViewModelProvider.Factory {

        private final EntryDao mDao;


        public Factory(@NonNull EntryDao dao) {
            mDao = dao;
        }


        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new HistoryBottomSheetViewModel(mDao);
        }
    }
}
