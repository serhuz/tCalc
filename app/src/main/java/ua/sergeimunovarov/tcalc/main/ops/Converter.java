/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Converter {

    static final int MILLIS_IN_DAY = 86400000;
    private static final int MILLIS_IN_SECOND = 1000;
    private static final int MILLIS_IN_MINUTE = 60000;
    private static final int MILLIS_IN_HOUR = 3600000;

    private static final char CHAR_COLON = ':';
    private static final char CHAR_MINUS = '-';
    private static final char CHAR_DOT = '.';

    private static final DecimalFormat HMS_FORMAT = new DecimalFormat("00");
    private static final DecimalFormat MILLIS_FORMAT = new DecimalFormat("000");


    private Converter() {
        throw new AssertionError();
    }


    /**
     * Parses given token and calculates amount of milliseconds.
     *
     * @param timeUnit Formatted time unit token
     * @return Amount of milliseconds in the given token
     */
    public static long toMillis(String timeUnit) {
        boolean match = false;
        long millis = 0;

        for (TimeUnit unit : TimeUnit.values()) {
            Matcher matcher = unit.getPattern().matcher(timeUnit);
            if (matcher.find()) {
                match = true;
                switch (unit) {
                    case UNIT_MS:
                        millis = Long.parseLong(matcher.group(2)) * MILLIS_IN_MINUTE
                                + Long.parseLong(matcher.group(3)) * MILLIS_IN_SECOND
                                + Long.parseLong(matcher.group(5));
                        break;
                    case UNIT_HM:
                        millis = Long.parseLong(matcher.group(2)) * MILLIS_IN_HOUR
                                + Long.parseLong(matcher.group(3)) * MILLIS_IN_MINUTE;
                        break;
                    case UNIT_HMS:
                        millis = Long.parseLong(matcher.group(2)) * MILLIS_IN_HOUR
                                + Long.parseLong(matcher.group(3)) * MILLIS_IN_MINUTE
                                + Long.parseLong(matcher.group(4)) * MILLIS_IN_SECOND;
                        try {
                            millis += Long.parseLong(matcher.group(6));
                        } catch (NumberFormatException ex) {
                            // millis could not be parsed, therefore nothing is added
                        }
                        break;
                    case UNIT_DHMS:
                        millis = Long.parseLong(matcher.group(2)) * MILLIS_IN_DAY
                                + Long.parseLong(matcher.group(3)) * MILLIS_IN_HOUR
                                + Long.parseLong(matcher.group(4)) * MILLIS_IN_MINUTE
                                + Long.parseLong(matcher.group(5)) * MILLIS_IN_SECOND;
                        try {
                            millis += Long.parseLong(matcher.group(7));
                        } catch (NumberFormatException ex) {
                            // millis could not be parsed, therefore nothing is added
                        }
                        break;
                }
                break;
            }
        }

        if (!match) {
            throw new IllegalArgumentException(timeUnit);
        }
        return millis;
    }


    /**
     * Converts given milliseconds to mm:ss.SSS format.
     *
     * @param millis amount of milliseconds
     * @return Formatted time unit string
     */
    public static String formatDurationMs(long millis) {
        StringBuilder sb = new StringBuilder();

        if (millis < 0) {
            sb.append(CHAR_MINUS);
            millis = Math.abs(millis);
        }

        long minutes;
        long seconds;

        minutes = millis / MILLIS_IN_MINUTE;
        millis -= minutes * MILLIS_IN_MINUTE;

        seconds = millis / MILLIS_IN_SECOND;
        millis -= seconds * MILLIS_IN_SECOND;

        sb.append(HMS_FORMAT.format(minutes)).append(CHAR_COLON).append(HMS_FORMAT.format(seconds));

        if (millis != 0) {
            if (millis < 0) {
                millis = Math.abs(millis);
                sb.insert(0, CHAR_MINUS);
            }
            sb.append(CHAR_DOT).append(MILLIS_FORMAT.format(millis));
        }

        return sb.toString();
    }


    /**
     * Converts given milliseconds to [hh:mm:ss.SSS]mod24 format.
     *
     * @param millis amount of milliseconds
     * @return Formatted time unit string
     */
    public static String formatDurationHmsMod(long millis) {
        long result;
        if (millis >= 0) {
            result = millis % MILLIS_IN_DAY;
        } else {
            result = millis;
            while (result < MILLIS_IN_DAY) {
                result += MILLIS_IN_DAY;
            }
            result %= MILLIS_IN_DAY;
        }
        return formatDurationHms(result);
    }


    /**
     * Converts given milliseconds to hh:mm:ss.SSS format.
     *
     * @param millis amount of milliseconds
     * @return Formatted time unit string
     */
    public static String formatDurationHms(long millis) {
        StringBuilder sb = new StringBuilder();

        if (millis < 0) {
            sb.append(CHAR_MINUS);
            millis = Math.abs(millis);
        }

        long hours;
        long minutes;
        long seconds;

        hours = millis / MILLIS_IN_HOUR;
        millis -= hours * MILLIS_IN_HOUR;

        minutes = millis / MILLIS_IN_MINUTE;
        millis -= minutes * MILLIS_IN_MINUTE;

        seconds = millis / MILLIS_IN_SECOND;
        millis -= seconds * MILLIS_IN_SECOND;

        sb.append(HMS_FORMAT.format(hours)).append(CHAR_COLON).append(HMS_FORMAT.format(minutes)).
                append(CHAR_COLON).append(HMS_FORMAT.format(seconds));

        if (millis != 0) {
            sb.append(CHAR_DOT).append(MILLIS_FORMAT.format(millis));
        }

        return sb.toString();
    }


    /**
     * Converts given milliseconds to DDd. hh:mm:ss.SSS format.
     *
     * @param millis amount of milliseconds
     * @return Formatted time unit string
     */
    public static String formatDurationDhms(@NonNull String dayString, long millis) {
        StringBuilder sb = new StringBuilder();

        if (millis < 0) {
            sb.append(CHAR_MINUS);
            millis = Math.abs(millis);
        }

        long days;
        long hours;
        long minutes;
        long seconds;

        days = millis / MILLIS_IN_DAY;
        millis -= days * MILLIS_IN_DAY;

        hours = millis / MILLIS_IN_HOUR;
        millis -= hours * MILLIS_IN_HOUR;

        minutes = millis / MILLIS_IN_MINUTE;
        millis -= minutes * MILLIS_IN_MINUTE;

        seconds = millis / MILLIS_IN_SECOND;
        millis -= seconds * MILLIS_IN_SECOND;


        sb.append(String.valueOf(days))
                .append(dayString)
                .append(HMS_FORMAT.format(hours))
                .append(CHAR_COLON)
                .append(HMS_FORMAT.format(minutes))
                .append(CHAR_COLON)
                .append(HMS_FORMAT.format(seconds));

        if (millis != 0) {
            sb.append(CHAR_DOT).append(MILLIS_FORMAT.format(millis));
        }

        return sb.toString();
    }


    /**
     * Formats given value to 5 digits after decimal separator (if present).
     *
     * @param value     string representation of some number
     * @param precision amount of digits after decimal separator
     * @return Formatted value string
     * @throws NumberFormatException if given value cannot be parsed as double
     */
    public static String formatValue(String value, @IntRange(from = 1, to = 5) int precision) {
        String formatPattern = "#.";
        for (int i = 0; i < precision; i++) {
            formatPattern += "#";
        }

        DecimalFormat decimalFormat = new DecimalFormat(formatPattern);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(CHAR_DOT);

        decimalFormat.setDecimalFormatSymbols(symbols);
        try {
            return decimalFormat.format(Double.parseDouble(value));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.getMessage());
        }
    }


    private enum TimeUnit {
        UNIT_MS("(\\d+):(\\d+)(\\.(\\d{3})){1}"),
        UNIT_HM("(\\d+):(\\d+)"),
        UNIT_HMS("(\\d+):(\\d+):(\\d+)(\\.(\\d{3}))?"),
        UNIT_DHMS("(\\d+)d\\. (\\d+):(\\d+):(\\d+)(\\.(\\d{3}))?");

        private final Pattern pattern;


        TimeUnit(String pattern) {
            this.pattern = Pattern.compile("^(" + pattern + ")$");
        }


        public Pattern getPattern() {
            return pattern;
        }
    }
}
