/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.listeners.HistoryEntryClickListener;


public class HistoryEntryViewModel {

    private final HistoryEntryClickListener mClickListener;


    public HistoryEntryViewModel(HistoryEntryClickListener clickListener) {
        mClickListener = clickListener;
    }


    public void insert(Entry entry) {
        mClickListener.onInsert(entry);
    }
}
