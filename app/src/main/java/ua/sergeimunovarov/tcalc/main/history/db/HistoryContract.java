/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.provider.BaseColumns;


public final class HistoryContract {

    private HistoryContract() {
        throw new AssertionError();
    }


    public static final class HistoryEntry implements BaseColumns {

        public HistoryEntry() {
            throw new AssertionError();
        }


        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_TIMESTAMP = "ts";
        public static final String COLUMN_NAME_RESULT_TYPE = "res_typ";
        public static final String COLUMN_NAME_RESULT_VALUE = "res_val";
        public static final String COLUMN_NAME_EXPRESSION = "expr";
    }
}
