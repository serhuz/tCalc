/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.content.ContentValues;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;


@AutoValue
public abstract class Entry implements Parcelable {

    public static Entry create(@Nullable Long id,
                               @NonNull String expression,
                               @NonNull String resultType,
                               @NonNull String resultValue,
                               @IntRange(from = 0) long timestamp) {
        return new AutoValue_Entry(id, expression, resultType, resultValue, timestamp);
    }


    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_EXPRESSION, expression());
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_TYPE, resultType());
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE, resultValue());
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP, timestamp());
        return cv;
    }


    @Nullable
    public abstract Long id();

    @NonNull
    public abstract String expression();


    @NonNull
    public abstract String resultType();


    @NonNull
    public abstract String resultValue();


    public abstract long timestamp();
}
