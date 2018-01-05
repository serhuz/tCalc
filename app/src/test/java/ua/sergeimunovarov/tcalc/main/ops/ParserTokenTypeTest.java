/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class ParserTokenTypeTest {

    @Test
    public void parseOpeningBracket() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_OPEN.getPattern().matcher("(");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseOpeningBracket1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_OPEN.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseOpeningBracket2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_OPEN.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseOpeningBracket3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_OPEN.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseClosingBracket() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_CLOSE.getPattern().matcher(")");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseClosingBracket1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_CLOSE.getPattern().matcher("(");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseClosingBracket2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_CLOSE.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseClosingBracket3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_BRACKET_CLOSE.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parsePlus() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_PLUS.getPattern().matcher("+");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParsePlus1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_PLUS.getPattern().matcher("-");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParsePlus2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_PLUS.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParsePlus3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_PLUS.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseMinus() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MINUS.getPattern().matcher("-");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseMinus1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MINUS.getPattern().matcher("*");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMinus2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MINUS.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMinus3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MINUS.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseMultiplication() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MUL.getPattern().matcher("*");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseMultiplication1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MUL.getPattern().matcher("-");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMultiplication2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MUL.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMultiplication3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MUL.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseDivision() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DIV.getPattern().matcher("/");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseDivision1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DIV.getPattern().matcher("-");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDivision2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DIV.getPattern().matcher("10");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDivision3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DIV.getPattern().matcher(")");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseHMS1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HMS.getPattern().matcher("00:05:30");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseHMS2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HMS.getPattern().matcher("00:05:30.321");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseHMS1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HMS.getPattern().matcher("00:-05:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HMS.getPattern().matcher("00:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HMS.getPattern().matcher("1230");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS4() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HMS.getPattern().matcher("*");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHMS5() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HMS.getPattern().matcher("10:30.123");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseHM() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HM.getPattern().matcher("00:30");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseHM1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HM.getPattern().matcher("10:30:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HM.getPattern().matcher("10:30:30.123");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HM.getPattern().matcher("1230");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM4() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HM.getPattern().matcher("/");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseHM5() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_HM.getPattern().matcher("10:30.123");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseMS() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MSS.getPattern().matcher("10:30.123");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseMS1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MSS.getPattern().matcher("20:40");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMS2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MSS.getPattern().matcher("20:40:15");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMS3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MSS.getPattern().matcher("1015");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseMS4() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_MSS.getPattern().matcher("*");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseDHMS1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("2d. 10:20:30.123");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseDHMS2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("2d. 10:20:30");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseDHMS1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("10:20:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("20:30.234");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("20:30");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS4() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("20:30:00.111");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS5() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseDHMS6() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_DHMS.getPattern().matcher("2020");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseValue1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE.getPattern().matcher("2020");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseValue2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE.getPattern().matcher("1.000777");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseValue1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseValue2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE.getPattern().matcher("10:20.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseValue3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE.getPattern().matcher("10:15");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseValue4() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE.getPattern().matcher("10:15:00.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void parseNegativeValue1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("#2020");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void parseNegativeValue2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("#1.000777");

        assertThat(matcher.matches()).isTrue();
    }


    @Test
    public void notParseNegativeValue1() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("+");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue2() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("10:20.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue3() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("10:15");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue4() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("10:15:00.222");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue5() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("456");

        assertThat(matcher.matches()).isFalse();
    }


    @Test
    public void notParseNegativeValue6() throws Exception {
        Matcher matcher = ParserTokenType.TYPE_VALUE_NEGATIVE.getPattern().matcher("456.789000");

        assertThat(matcher.matches()).isFalse();
    }
}
