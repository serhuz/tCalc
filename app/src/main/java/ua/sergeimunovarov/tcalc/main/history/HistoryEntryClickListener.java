/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;


public interface HistoryEntryClickListener {

    void onInsert(Entry item);
}
