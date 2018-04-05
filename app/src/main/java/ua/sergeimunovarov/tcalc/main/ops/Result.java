/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import ua.sergeimunovarov.tcalc.main.Editor;


@AutoValue
public abstract class Result implements Parcelable {

    public static Result create(@NonNull ResultType resultType,
                                @NonNull String value,
                                @NonNull String expression) {
        return new AutoValue_Result(resultType, value, expression);
    }


    public abstract ResultType type();


    @NonNull
    public String formattedValue() {
        String value = value();
        if (value().startsWith("-")) {
            value = Editor.PAR_LEFT + value + Editor.PAR_RIGHT;
        }
        return value;
    }


    public abstract String value();


    public abstract String expression();


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
