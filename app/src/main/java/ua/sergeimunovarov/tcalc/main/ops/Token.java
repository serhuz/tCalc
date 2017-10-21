/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;


@AutoValue
public abstract class Token<T> {

    public static <T> Token<T> create(@NonNull TokenType type, @NonNull T value) {
        return new AutoValue_Token<T>(type, value);
    }


    @NonNull
    public abstract TokenType type();

    @NonNull
    public abstract T value();


    public enum TokenType {
        BRACKET_OPEN,
        BRACKET_CLOSE,
        PLUS,
        MINUS,
        MUL,
        DIV,
        VALUE,
        TIME_UNIT
    }
}
