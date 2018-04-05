/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class InputPatternTest {

    @Test
    public void parseOpeningBracket() {
        Matcher matcher = InputPattern.BRACKET_OPEN.getPattern().matcher("(");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseOpeningBracket1() {
        Matcher matcher = InputPattern.BRACKET_OPEN.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseOpeningBracket2() {
        Matcher matcher = InputPattern.BRACKET_OPEN.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseOpeningBracket3() {
        Matcher matcher = InputPattern.BRACKET_OPEN.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseClosingBracket() {
        Matcher matcher = InputPattern.BRACKET_CLOSE.getPattern().matcher(")");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseClosingBracket1() {
        Matcher matcher = InputPattern.BRACKET_CLOSE.getPattern().matcher("(");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseClosingBracket2() {
        Matcher matcher = InputPattern.BRACKET_CLOSE.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseClosingBracket3() {
        Matcher matcher = InputPattern.BRACKET_CLOSE.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parsePlus() {
        Matcher matcher = InputPattern.PLUS.getPattern().matcher("+");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParsePlus1() {
        Matcher matcher = InputPattern.PLUS.getPattern().matcher("-");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParsePlus2() {
        Matcher matcher = InputPattern.PLUS.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParsePlus3() {
        Matcher matcher = InputPattern.PLUS.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseMinus() {
        Matcher matcher = InputPattern.MINUS.getPattern().matcher("-");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseMinus1() {
        Matcher matcher = InputPattern.MINUS.getPattern().matcher("*");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMinus2() {
        Matcher matcher = InputPattern.MINUS.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMinus3() {
        Matcher matcher = InputPattern.MINUS.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseMultiplication() {
        Matcher matcher = InputPattern.MUL.getPattern().matcher("*");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseMultiplication1() {
        Matcher matcher = InputPattern.MUL.getPattern().matcher("-");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMultiplication2() {
        Matcher matcher = InputPattern.MUL.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMultiplication3() {
        Matcher matcher = InputPattern.MUL.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseDivision() {
        Matcher matcher = InputPattern.DIV.getPattern().matcher("/");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseDivision1() {
        Matcher matcher = InputPattern.DIV.getPattern().matcher("-");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDivision2() {
        Matcher matcher = InputPattern.DIV.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDivision3() {
        Matcher matcher = InputPattern.DIV.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseHMS1() {
        Matcher matcher = InputPattern.HMS.getPattern().matcher("00:05:30");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseHMS2() {
        Matcher matcher = InputPattern.HMS.getPattern().matcher("00:05:30.321");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseHMS1() {
        Matcher matcher = InputPattern.HMS.getPattern().matcher("00:-05:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS2() {
        Matcher matcher = InputPattern.HMS.getPattern().matcher("00:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS3() {
        Matcher matcher = InputPattern.HMS.getPattern().matcher("1230");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS4() {
        Matcher matcher = InputPattern.HMS.getPattern().matcher("*");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS5() {
        Matcher matcher = InputPattern.HMS.getPattern().matcher("10:30.123");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseHM() {
        Matcher matcher = InputPattern.HM.getPattern().matcher("00:30");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseHM1() {
        Matcher matcher = InputPattern.HM.getPattern().matcher("10:30:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM2() {
        Matcher matcher = InputPattern.HM.getPattern().matcher("10:30:30.123");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM3() {
        Matcher matcher = InputPattern.HM.getPattern().matcher("1230");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM4() {
        Matcher matcher = InputPattern.HM.getPattern().matcher("/");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM5() {
        Matcher matcher = InputPattern.HM.getPattern().matcher("10:30.123");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseMS() {
        Matcher matcher = InputPattern.MSS.getPattern().matcher("10:30.123");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseMS1() {
        Matcher matcher = InputPattern.MSS.getPattern().matcher("20:40");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMS2() {
        Matcher matcher = InputPattern.MSS.getPattern().matcher("20:40:15");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMS3() {
        Matcher matcher = InputPattern.MSS.getPattern().matcher("1015");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMS4() {
        Matcher matcher = InputPattern.MSS.getPattern().matcher("*");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseDHMS1() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("2d. 10:20:30.123");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseDHMS2() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("2d. 10:20:30");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseDHMS1() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("10:20:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS2() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("20:30.234");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS3() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("20:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS4() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("20:30:00.111");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS5() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS6() {
        Matcher matcher = InputPattern.DHMS.getPattern().matcher("2020");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseValue1() {
        Matcher matcher = InputPattern.VALUE.getPattern().matcher("2020");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseValue2() {
        Matcher matcher = InputPattern.VALUE.getPattern().matcher("1.000777");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseValue1() {
        Matcher matcher = InputPattern.VALUE.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseValue2() {
        Matcher matcher = InputPattern.VALUE.getPattern().matcher("10:20.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseValue3() {
        Matcher matcher = InputPattern.VALUE.getPattern().matcher("10:15");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseValue4() {
        Matcher matcher = InputPattern.VALUE.getPattern().matcher("10:15:00.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseNegativeValue1() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("#2020");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseNegativeValue2() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("#1.000777");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseNegativeValue1() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue2() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("10:20.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue3() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("10:15");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue4() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("10:15:00.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue5() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("456");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue6() {
        Matcher matcher = InputPattern.VALUE_NEGATIVE.getPattern().matcher("456.789000");

        assertThat(matcher.matches()).isFalse();
    }
}
