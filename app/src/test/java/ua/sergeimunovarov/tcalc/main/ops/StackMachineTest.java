package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SuppressWarnings("UnnecessaryLocalVariable")
@RunWith(JUnit4.class)
public class StackMachineTest {


    private static final Token TOKEN_TIME_10_MINS = Token.create(Token.TokenType.TIME_UNIT, (long) 10 * 60 * 1000);
    private static final Token TOKEN_VALUE_2 = Token.create(Token.TokenType.VALUE, 2d);
    private static final Token TOKEN_BRACKET_CLOSE = Token.create(Token.TokenType.BRACKET_CLOSE, ")");
    private static final Token TOKEN_BRACKET_OPEN = Token.create(Token.TokenType.BRACKET_OPEN, "(");
    private static final Token TOKEN_MUL = Token.create(Token.TokenType.MUL, "*");
    private static final Token TOKEN_DIV = Token.create(Token.TokenType.DIV, "/");


    @Test
    public void testAdd() throws Exception {
        Token a = TOKEN_TIME_10_MINS;

        Token result = StackMachine.add(a, a);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo((long) 10 * 60 * 1000 * 2);
    }


    @Test
    public void testAdd2() throws Exception {
        Token a = TOKEN_VALUE_2;

        Token result = StackMachine.add(a, a);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(4.0);
    }


    @Test
    public void testAdd3() throws Exception {
        Token a = TOKEN_BRACKET_CLOSE;
        Token b = TOKEN_VALUE_2;

        assertThatThrownBy(() -> StackMachine.add(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(")");
    }


    @Test
    public void testAdd4() throws Exception {
        Token a = TOKEN_TIME_10_MINS;
        Token b = TOKEN_VALUE_2;

        assertThatThrownBy(() -> StackMachine.add(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(StackMachine.ERR_T_PLUS_V);
    }


    @Test
    public void testAdd5() throws Exception {
        Token a = TOKEN_VALUE_2;
        Token b = Token.create(Token.TokenType.TIME_UNIT, 0L);

        assertThatThrownBy(() -> StackMachine.add(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(StackMachine.ERR_V_PLUS_T);
    }


    @Test
    public void testAdd6() throws Exception {
        Token a = TOKEN_VALUE_2;
        Token b = TOKEN_BRACKET_CLOSE;

        assertThatThrownBy(() -> StackMachine.add(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("+)");
    }


    @Test
    public void testSubtract() throws Exception {
        Token a = TOKEN_TIME_10_MINS;

        Token result = StackMachine.subtract(a, a);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo(0L);
    }


    @Test
    public void testSubtract2() throws Exception {
        Token a = Token.create(Token.TokenType.VALUE, 3d);
        Token b = TOKEN_VALUE_2;

        Token result = StackMachine.subtract(a, b);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(1.0);
    }


    @Test
    public void testSubtract3() throws Exception {
        Token a = TOKEN_BRACKET_OPEN;
        Token b = TOKEN_VALUE_2;

        assertThatThrownBy(() -> StackMachine.subtract(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("(");
    }


    @Test
    public void testSubtract4() throws Exception {
        Token a = Token.create(Token.TokenType.TIME_UNIT, String.valueOf(0));
        Token b = TOKEN_VALUE_2;

        assertThatThrownBy(() -> StackMachine.subtract(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(StackMachine.ERR_T_MINUS_V);
    }


    @Test
    public void testSubtract5() throws Exception {
        Token a = TOKEN_VALUE_2;
        Token b = Token.create(Token.TokenType.TIME_UNIT, 0L);

        assertThatThrownBy(() -> StackMachine.subtract(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(StackMachine.ERR_V_MINUS_T);
    }


    @Test
    public void testSubtract6() throws Exception {
        Token a = TOKEN_VALUE_2;
        Token b = TOKEN_BRACKET_CLOSE;

        assertThatThrownBy(() -> StackMachine.subtract(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("-)");
    }


    @Test
    public void testMultiply() throws Exception {
        Token a = TOKEN_TIME_10_MINS;
        Token b = TOKEN_VALUE_2;

        Token result = StackMachine.multiply(a, b);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo((long) 10 * 60 * 1000 * 2);
    }


    @Test
    public void testMultiply2() throws Exception {
        Token a = TOKEN_VALUE_2;

        Token result = StackMachine.multiply(a, a);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(4.0);
    }


    @Test
    public void testMultiply3() throws Exception {
        Token a = TOKEN_TIME_10_MINS;

        assertThatThrownBy(() -> StackMachine.multiply(a, a))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(StackMachine.ERR_T_MUL_T);
    }


    @Test
    public void testMultiply4() throws Exception {
        Token a = TOKEN_TIME_10_MINS;
        Token b = TOKEN_DIV;

        assertThatThrownBy(() -> StackMachine.multiply(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("*/");
    }


    @Test
    public void testMultiply5() throws Exception {
        Token a = TOKEN_DIV;
        Token b = TOKEN_TIME_10_MINS;

        assertThatThrownBy(() -> StackMachine.multiply(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("/");
    }


    @Test
    public void testDivide() throws Exception {
        Token a = TOKEN_TIME_10_MINS;
        Token b = TOKEN_VALUE_2;

        Token result = StackMachine.divide(a, b);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo((long) 5 * 60 * 1000);
    }


    @Test
    public void testDivide2() throws Exception {
        Token a = TOKEN_VALUE_2;

        Token result = StackMachine.divide(a, a);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(1.0);
    }


    @Test
    public void testDivide3() throws Exception {
        Token a = TOKEN_VALUE_2;
        Token b = Token.create(Token.TokenType.VALUE, 0d);

        assertThatThrownBy(() -> StackMachine.divide(a, b))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage(StackMachine.ERR_DIV_BY_ZERO);
    }


    @Test
    public void testDivide4() throws Exception {
        Token a = TOKEN_TIME_10_MINS;

        Token result = StackMachine.divide(a, a);

        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(1.0);
    }


    @Test
    public void testDivide5() throws Exception {
        Token a = TOKEN_VALUE_2;
        Token b = TOKEN_TIME_10_MINS;

        assertThatThrownBy(() -> StackMachine.divide(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(StackMachine.ERR_V_DIV_T);
    }


    @Test
    public void testDivide6() throws Exception {
        Token a = TOKEN_MUL;
        Token b = TOKEN_TIME_10_MINS;

        assertThatThrownBy(() -> StackMachine.divide(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("*");
    }


    @Test
    public void testDivide7() throws Exception {
        Token a = TOKEN_TIME_10_MINS;
        Token b = TOKEN_MUL;

        assertThatThrownBy(() -> StackMachine.divide(a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("/*");
    }
}
