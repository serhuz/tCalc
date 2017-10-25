/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.listeners;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;


public interface InsertListener {

    void onInsert(Entry entry);
}
