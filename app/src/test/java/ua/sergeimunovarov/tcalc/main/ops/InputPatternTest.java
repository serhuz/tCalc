/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(Enclosed.class)
public class InputPatternTest {

    @RunWith(Parameterized.class)
    public static class ExpectTrueTests {

        @Parameterized.Parameter
        public InputPattern pattern;

        @Parameterized.Parameter(1)
        public String given;


        @Test
        public void checkMatches() {
            Matcher matcher = pattern.getPattern().matcher(given);

            assertThat(matcher.matches()).isTrue();
        }


        @Parameterized.Parameters(name = "{0} matches \"{1}\"")
        public static Collection<Object[]> getParameters() {
            return Arrays.asList(
                    new Object[][]{
                            {InputPattern.BRACKET_OPEN, "("},
                            {InputPattern.BRACKET_CLOSE, ")"},
                            {InputPattern.PLUS, "+"},
                            {InputPattern.MINUS, "-"},
                            {InputPattern.MUL, "*"},
                            {InputPattern.DIV, "/"},
                            {InputPattern.HMS, "00:05:30"},
                            {InputPattern.HMS, "00:05:30.321"},
                            {InputPattern.HM, "00:30"},
                            {InputPattern.MSS, "10:30.123"},
                            {InputPattern.DHMS, "2d. 10:20:30.123"},
                            {InputPattern.DHMS, "2d. 10:20:30"},
                            {InputPattern.VALUE, "2020"},
                            {InputPattern.VALUE, "1.000777"},
                            {InputPattern.VALUE_NEGATIVE, "#2020"},
                            {InputPattern.VALUE_NEGATIVE, "#1.000777"},
                    }
            );
        }
    }


    @RunWith(Parameterized.class)
    public static class ExpectFalseTests {

        @Parameterized.Parameter
        public InputPattern pattern;

        @Parameterized.Parameter(1)
        public String given;


        @Test
        public void checkNotMatches() {
            Matcher matcher = pattern.getPattern().matcher(given);

            assertThat(matcher.matches()).isFalse();
        }


        @Parameterized.Parameters(name = "{0} not matches \"{1}\"")
        public static Collection<Object[]> getParameters() {
            return Arrays.asList(
                    new Object[][]{
                            {InputPattern.BRACKET_OPEN, ")"},
                            {InputPattern.BRACKET_OPEN, "10"},
                            {InputPattern.BRACKET_OPEN, "+"},
                            {InputPattern.BRACKET_CLOSE, "("},
                            {InputPattern.BRACKET_CLOSE, "10"},
                            {InputPattern.BRACKET_CLOSE, "+"},
                            {InputPattern.PLUS, "-"},
                            {InputPattern.PLUS, "10"},
                            {InputPattern.PLUS, ")"},
                            {InputPattern.MINUS, "*"},
                            {InputPattern.MINUS, "10"},
                            {InputPattern.MINUS, ")"},
                            {InputPattern.MUL, "-"},
                            {InputPattern.MUL, "10"},
                            {InputPattern.MUL, ")"},
                            {InputPattern.DIV, "-"},
                            {InputPattern.DIV, "10"},
                            {InputPattern.DIV, ")"},
                            {InputPattern.HMS, "00:-05:30"},
                            {InputPattern.HMS, "00:05:30.99999"},
                            {InputPattern.HMS, "00:30"},
                            {InputPattern.HMS, "1230"},
                            {InputPattern.HMS, "*"},
                            {InputPattern.HMS, "10:30.123"},
                            {InputPattern.HM, "10:30:30"},
                            {InputPattern.HM, "10:30:30.123"},
                            {InputPattern.HM, "1230"},
                            {InputPattern.HM, "/"},
                            {InputPattern.HM, "10:30.123"},
                            {InputPattern.MSS, "20:40"},
                            {InputPattern.MSS, "20:40:15"},
                            {InputPattern.MSS, "1015"},
                            {InputPattern.MSS, "*"},
                            {InputPattern.DHMS, "10:20:30"},
                            {InputPattern.DHMS, "20:30.234"},
                            {InputPattern.DHMS, "20:30"},
                            {InputPattern.DHMS, "20:30:00.111"},
                            {InputPattern.DHMS, "+"},
                            {InputPattern.DHMS, "2020"},
                            {InputPattern.VALUE, "+"},
                            {InputPattern.VALUE, "10:20.222"},
                            {InputPattern.VALUE, "10:15"},
                            {InputPattern.VALUE, "10:15:00.222"},
                            {InputPattern.VALUE, "10:15:00.222"},
                            {InputPattern.VALUE, "#2020"},
                            {InputPattern.VALUE_NEGATIVE, "+"},
                            {InputPattern.VALUE_NEGATIVE, "10:20.222"},
                            {InputPattern.VALUE_NEGATIVE, "10:15"},
                            {InputPattern.VALUE_NEGATIVE, "10:15:00.222"},
                            {InputPattern.VALUE_NEGATIVE, "10:15:00.222"},
                            {InputPattern.VALUE_NEGATIVE, "2020"},
                            {InputPattern.VALUE_NEGATIVE, "456"},
                            {InputPattern.VALUE_NEGATIVE, "456.789000"},
                    }
            );
        }
    }
}
