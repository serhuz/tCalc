/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.arch.persistence.room.TypeConverter;

import ua.sergeimunovarov.tcalc.main.ops.Result;


public final class Converters {

    @TypeConverter
    public String fromResultType(Result.ResultType resultType) {
        return resultType.name();
    }


    @TypeConverter
    public Result.ResultType toResultType(String resultType) {
        return Result.ResultType.valueOf(resultType);
    }
}
