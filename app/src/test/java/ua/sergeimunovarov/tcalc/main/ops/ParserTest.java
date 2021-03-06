/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;


public class ParserTest {

    private Parser mParser;


    @Before
    public void setUp() {
        mParser = new Parser();
    }


    @Test
    public void parseExpression1() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("1+2");

        Token<Double> one = Token.create(Type.VALUE, 1d);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(one, two, plus);
    }


    @Test
    public void parseExpression2() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("1-2");

        Token<Double> one = Token.create(Type.VALUE, 1d);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> minus = Token.create(Type.MINUS, "-");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(one, two, minus);
    }


    @Test
    public void parseExpression3() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("1*2");

        Token<Double> one = Token.create(Type.VALUE, 1d);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(one, two, mul);
    }


    @Test
    public void parseExpression4() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("1/2");

        Token<Double> one = Token.create(Type.VALUE, 1d);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> div = Token.create(Type.DIV, "/");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(one, two, div);
    }


    @Test
    public void parseExpression5() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("0.5");

        Token<Double> dotFive = Token.create(Type.VALUE, 0.5d);

        assertThat(tokens).isNotNull().hasSize(1).containsExactly(dotFive);
    }


    @Test
    public void parseExpression6() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("01:00");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);

        assertThat(tokens).isNotNull().hasSize(1).containsExactly(hour);
    }


    @Test
    public void parseExpression7() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("1:0");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);

        assertThat(tokens).isNotNull().hasSize(1).containsExactly(hour);
    }


    @Test
    public void parseExpression8() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("01:00*2");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(hour, two, mul);
    }


    @Test
    public void parseExpression9() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("01:00/2");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> div = Token.create(Type.DIV, "/");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(hour, two, div);
    }


    @Test
    public void parseExpression10() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("01:00+2");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(hour, two, plus);
    }


    @Test
    public void parseExpression11() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("01:00-2");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> minus = Token.create(Type.MINUS, "-");

        assertThat(tokens).isNotNull().hasSize(3).containsExactly(hour, two, minus);
    }


    @Test
    public void parseExpression12() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("2+2*2");

        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(5).containsExactly(two, two, two, mul, plus);
    }


    @Test
    public void parseExpression13() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("2/2*2");

        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> div = Token.create(Type.DIV, "/");
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(5).containsExactly(two, two, div, two, mul);
    }


    @Test
    public void parseExpression14() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("2+2-2");

        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> minus = Token.create(Type.MINUS, "-");

        assertThat(tokens).isNotNull().hasSize(5).containsExactly(two, two, plus, two, minus);
    }


    @Test
    public void parseExpression15() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("(2+2)*2");

        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(5).containsExactly(two, two, plus, two, mul);
    }


    @Test
    public void parseExpression16() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("(1:00+1:00)*2");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(5).containsExactly(hour, hour, plus, two, mul);
    }


    @Test
    public void parseExpression17() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("(1:00+1:00)*(2+2)");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(7).containsExactly(hour, hour, plus, two, two, plus, mul);
    }


    @Test
    public void parseExpression18() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("(1:00+1:00)/(2+2)");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> div = Token.create(Type.DIV, "/");

        assertThat(tokens).isNotNull().hasSize(7).containsExactly(hour, hour, plus, two, two, plus, div);
    }


    @Test
    public void parseExpression19() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("(1:00+(1:00*2))/(2+2)");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> div = Token.create(Type.DIV, "/");
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(9).containsExactly(hour, hour, two, mul, plus, two, two, plus, div);
    }


    @Test
    public void parseExpression20() {
        LinkedList<Token> tokens = mParser.tokenizeExpression("(1:00+(1:00*2))/(2+2/2)");

        Token<Long> hour = Token.create(Type.TIME_UNIT, (long) 60 * 60 * 1000);
        Token<Double> two = Token.create(Type.VALUE, 2d);
        Token<String> plus = Token.create(Type.PLUS, "+");
        Token<String> div = Token.create(Type.DIV, "/");
        Token<String> mul = Token.create(Type.MUL, "*");

        assertThat(tokens).isNotNull().hasSize(11).containsExactly(hour, hour, two, mul, plus, two, two, two, div, plus, div);
    }


    @Test
    public void parseExpression21() {
        assertThatThrownBy(() -> mParser.tokenizeExpression("abc*2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("abc");
    }


    @Test
    public void parseExpression22() {
        assertThatThrownBy(() -> mParser.tokenizeExpression("2a*2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2a");
    }


    @Test
    public void parseExpression23() {
        assertThatThrownBy(() -> mParser.tokenizeExpression("a2*2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("a2");
    }


    @Test
    public void parseExpression24() {
        assertThatThrownBy(() -> mParser.tokenizeExpression("2*2a"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2a");
    }
}
