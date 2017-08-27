/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

/**
 * Contains regular expression patterns for input parsing.
 */
public final class Patterns {

    public static final String BRACKET_OPEN = "\\(";

    public static final String BRACKET_CLOSE = "\\)";

    public static final String PLUS = "\\+";

    public static final String MINUS = "-";

    public static final String MUL = "\\*";

    public static final String DIV = "/";

    public static final String VALUE = "\\d+(\\.\\d+)?";

    public static final String VALUE_NEGATIVE = "#\\d+(\\.\\d+)?";

    public static final String FORMAT_HMS = "\\d+:\\d+:\\d+(\\.\\d{3})?";

    public static final String FORMAT_HM = "\\d+:\\d+";

    public static final String FORMAT_MSS = "\\d+:\\d+\\.\\d{3}";

    public static final String FORMAT_DHMS = "(\\dd\\. \\d+:\\d+:\\d+(\\.\\d{3})?)";

    public static final String PREFIX_SHARP = "#";

    private Patterns() {
        throw new AssertionError();
    }
}
