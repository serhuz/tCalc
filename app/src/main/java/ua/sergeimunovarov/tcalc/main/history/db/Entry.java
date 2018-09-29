/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
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

    @ColumnInfo(name = "fid")
    private int formatId;


    public Entry(@NonNull String expression,
                 @NonNull Result.ResultType resultType,
                 @NonNull String resultValue,
                 @IntRange(from = 0) long timestamp,
                 @IntRange(from = 0, to = 3) int formatId) {
        this.expression = expression;
        this.resultType = resultType;
        this.resultValue = resultValue;
        this.timestamp = timestamp;
        this.formatId = formatId;
    }


    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + expression.hashCode();
        result = 31 * result + resultType.hashCode();
        result = 31 * result + resultValue.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + formatId;
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        if (id != entry.id) return false;
        if (timestamp != entry.timestamp) return false;
        if (formatId != entry.formatId) return false;
        if (!expression.equals(entry.expression)) return false;
        if (resultType != entry.resultType) return false;
        return resultValue.equals(entry.resultValue);
    }


    public boolean sameContents(@NonNull Entry other) {
        if (this.getResultType() == Result.ResultType.RESULT_OK) {
            return this.getExpression().equals(other.getExpression())
                    && this.getResultValue().equals(other.getResultValue())
                    && this.getResultType().equals(other.getResultType())
                    && this.getFormatId() == other.getFormatId();
        } else if (this.getResultType() == Result.ResultType.RESULT_OK_VALUE) {
            return this.getExpression().equals(other.getExpression())
                    && this.getResultValue().equals(other.getResultValue())
                    && this.getResultType().equals(other.getResultType());
        } else {
            throw new IllegalArgumentException();
        }
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


    public int getFormatId() {
        return formatId;
    }


    public void setFormatId(int formatId) {
        this.formatId = formatId;
    }
}
