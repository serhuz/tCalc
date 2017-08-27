package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnit4.class)
public class StackMachineTest {

    @Test
    public void testAdd() throws Exception {
        Token a = Token.create(Token.TokenType.TIME_UNIT, String.valueOf(10 * 60 * 1000));
        Token result = StackMachine.add(a, a);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo(String.valueOf(10 * 60 * 1000 * 2));
    }


    @Test
    public void testAdd2() throws Exception {
        Token a = Token.create(Token.TokenType.VALUE, String.valueOf(2));
        Token result = StackMachine.add(a, a);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(String.valueOf(4.0));
    }


    @Test
    public void testSubtract() throws Exception {
        Token a = Token.create(Token.TokenType.TIME_UNIT, String.valueOf(10 * 60 * 1000));
        Token result = StackMachine.subtract(a, a);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo(String.valueOf(0));
    }


    @Test
    public void testSubtract2() throws Exception {
        Token a = Token.create(Token.TokenType.VALUE, String.valueOf(3));
        Token b = Token.create(Token.TokenType.VALUE, String.valueOf(2));
        Token result = StackMachine.subtract(a, b);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(String.valueOf(1.0));
    }


    @Test
    public void testMultiply() throws Exception {
        Token a = Token.create(Token.TokenType.TIME_UNIT, String.valueOf(10 * 60 * 1000));
        Token b = Token.create(Token.TokenType.VALUE, String.valueOf(2));
        Token result = StackMachine.multiply(a, b);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo(String.valueOf(10 * 60 * 1000 * 2));
    }


    @Test
    public void testMultiply2() throws Exception {
        Token b = Token.create(Token.TokenType.VALUE, String.valueOf(2));
        Token result = StackMachine.add(b, b);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(String.valueOf(4.0));
    }


    @Test
    public void testDivide() throws Exception {
        Token a = Token.create(Token.TokenType.TIME_UNIT, String.valueOf(10 * 60 * 1000));
        Token b = Token.create(Token.TokenType.VALUE, String.valueOf(2));
        Token result = StackMachine.divide(a, b);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.TIME_UNIT);
        assertThat(result.value()).isEqualTo(String.valueOf(5 * 60 * 1000));
    }


    @Test
    public void testDivide2() throws Exception {
        Token b = Token.create(Token.TokenType.VALUE, String.valueOf(2));
        Token result = StackMachine.divide(b, b);
        assertThat(result.type()).isNotNull().isEqualTo(Token.TokenType.VALUE);
        assertThat(result.value()).isEqualTo(String.valueOf(1.0));
    }
}
