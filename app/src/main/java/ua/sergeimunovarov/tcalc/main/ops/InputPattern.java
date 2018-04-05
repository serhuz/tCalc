/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;


public enum InputPattern {

    BRACKET_OPEN("\\(", Type.BRACKET_OPEN, false, true),
    BRACKET_CLOSE("\\)", Type.BRACKET_CLOSE, false, true),
    PLUS("\\+", Type.PLUS, false, true),
    MINUS("-", Type.MINUS, false, true),
    MUL("\\*", Type.MUL, false, true),
    DIV("/", Type.DIV, false, true),
    HMS("(\\d+):(\\d+):(\\d+)(\\.(\\d{3}))?", Type.TIME_UNIT, true, true),
    HM("(\\d+):(\\d+)", Type.TIME_UNIT, true, true),
    MSS("(\\d+):(\\d+)(\\.(\\d{3})){1}", Type.TIME_UNIT, true, true),
    DHMS("(\\d+)d\\. (\\d+):(\\d+):(\\d+)(\\.(\\d{3}))?", Type.TIME_UNIT, true, true),
    VALUE("\\d+(\\.\\d+)?", Type.VALUE, false, true),
    VALUE_NEGATIVE("#\\d+(\\.\\d+)?", Type.VALUE, false, true);


    private final Pattern mPattern;
    private final Type mType;
    private final boolean mSupportsConverter;
    private final boolean mSupportsParser;


    InputPattern(@NonNull String regex, @NonNull Type type, boolean supportsConverter, boolean supportsParser) {
        mPattern = java.util.regex.Pattern.compile("^(" + regex + ")$");
        mType = type;
        mSupportsConverter = supportsConverter;
        mSupportsParser = supportsParser;
    }


    public Pattern getPattern() {
        return mPattern;
    }


    public Type getType() {
        return mType;
    }


    public boolean supportsConverter() {
        return mSupportsConverter;
    }


    public boolean supportsParser() {
        return mSupportsParser;
    }
}
