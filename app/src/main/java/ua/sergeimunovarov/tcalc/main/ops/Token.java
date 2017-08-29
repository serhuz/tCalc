/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;


@AutoValue
public abstract class Token {

    public static Token create(@NonNull TokenType type,
                               @NonNull String value) {
        return new AutoValue_Token(type, value);
    }


    public abstract TokenType type();

    public abstract String value();


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
