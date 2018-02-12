/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import ua.sergeimunovarov.tcalc.main.ops.Result;


@Entity(
        tableName = "history",
        indices = {@Index(value = "ts")}
)
@TypeConverters(Converters.class)
public class Entry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private long id;

    @ColumnInfo(name = "expr")
    private String expression;

    @ColumnInfo(name = "res_typ")
    private Result.ResultType resultType;

    @ColumnInfo(name = "res_val")
    private String resultValue;

    @ColumnInfo(name = "ts")
    private long timestamp;


    public Entry(@NonNull String expression,
                 @NonNull Result.ResultType resultType,
                 @NonNull String resultValue,
                 @IntRange(from = 0) long timestamp) {
        this.expression = expression;
        this.resultType = resultType;
        this.resultValue = resultValue;
        this.timestamp = timestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry item = (Entry) o;

        if (id != item.id) return false;
        if (timestamp != item.timestamp) return false;
        if (!expression.equals(item.expression)) return false;
        if (resultType != item.resultType) return false;
        return resultValue.equals(item.resultValue);
    }


    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + expression.hashCode();
        result = 31 * result + resultType.hashCode();
        result = 31 * result + resultValue.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getExpression() {
        return expression;
    }


    public void setExpression(String expression) {
        this.expression = expression;
    }


    public Result.ResultType getResultType() {
        return resultType;
    }


    public void setResultType(Result.ResultType resultType) {
        this.resultType = resultType;
    }


    public String getResultValue() {
        return resultValue;
    }


    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }


    public long getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
