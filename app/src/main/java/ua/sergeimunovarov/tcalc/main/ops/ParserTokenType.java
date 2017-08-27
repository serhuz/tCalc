/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

import static ua.sergeimunovarov.tcalc.Patterns.BRACKET_CLOSE;
import static ua.sergeimunovarov.tcalc.Patterns.BRACKET_OPEN;
import static ua.sergeimunovarov.tcalc.Patterns.DIV;
import static ua.sergeimunovarov.tcalc.Patterns.FORMAT_DHMS;
import static ua.sergeimunovarov.tcalc.Patterns.FORMAT_HM;
import static ua.sergeimunovarov.tcalc.Patterns.FORMAT_HMS;
import static ua.sergeimunovarov.tcalc.Patterns.FORMAT_MSS;
import static ua.sergeimunovarov.tcalc.Patterns.MINUS;
import static ua.sergeimunovarov.tcalc.Patterns.MUL;
import static ua.sergeimunovarov.tcalc.Patterns.PLUS;
import static ua.sergeimunovarov.tcalc.Patterns.VALUE;
import static ua.sergeimunovarov.tcalc.Patterns.VALUE_NEGATIVE;

public enum ParserTokenType {

    TYPE_BRACKET_OPEN(BRACKET_OPEN, Token.TokenType.BRACKET_OPEN),
    TYPE_BRACKET_CLOSE(BRACKET_CLOSE, Token.TokenType.BRACKET_CLOSE),
    TYPE_PLUS(PLUS, Token.TokenType.PLUS),
    TYPE_MINUS(MINUS, Token.TokenType.MINUS),
    TYPE_MUL(MUL, Token.TokenType.MUL),
    TYPE_DIV(DIV, Token.TokenType.DIV),
    TYPE_HMS(FORMAT_HMS, Token.TokenType.TIME_UNIT),
    TYPE_HM(FORMAT_HM, Token.TokenType.TIME_UNIT),
    TYPE_MSS(FORMAT_MSS, Token.TokenType.TIME_UNIT),
    TYPE_DHMS(FORMAT_DHMS, Token.TokenType.TIME_UNIT),
    TYPE_VALUE(VALUE, Token.TokenType.VALUE),
    TYPE_VALUE_NEGATIVE(VALUE_NEGATIVE, Token.TokenType.VALUE);

    private final Pattern pattern;
    private final Token.TokenType type;

    ParserTokenType(@NonNull String pattern, @NonNull Token.TokenType type) {
        this.pattern = Pattern.compile(pattern);
        this.type = type;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Token.TokenType getType() {
        return type;
    }
}
