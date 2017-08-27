/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Result implements Parcelable {

    public static Result create(@NonNull ResultType resultType,
                                @NonNull String value) {
        return new AutoValue_Result(resultType, value);
    }

    public abstract ResultType type();

    public abstract String value();

    /**
     * Defines type for calculation result
     */
    public enum ResultType {

        /**
         * Calculation error
         */
        RESULT_ERR,

        /**
         * Calculation done, result is a time unit
         */
        RESULT_OK,

        /**
         * Calculation done, result is a value
         */
        RESULT_OK_VALUE
    }
}
