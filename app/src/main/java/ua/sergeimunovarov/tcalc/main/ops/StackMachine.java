/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Stack;

public class StackMachine {

    // Arithmetic operations ----------------------------------------------------------------------
    private static final String OP_PLUS = "+";
    private static final String OP_MINUS = "-";
    private static final String OP_MUL = "*";
    private static final String OP_DIV = "/";
    // --------------------------------------------------------------------------------------------

    // Errors -------------------------------------------------------------------------------------
    private static final String ERR_V_PLUS_T = "V+T";
    private static final String ERR_T_PLUS_V = "T+V";
    private static final String ERR_V_MINUS_T = "V-T";
    private static final String ERR_T_MINUS_V = "T-V";
    private static final String ERR_T_MUL_T = "T*T";
    private static final String ERR_V_DIV_T = "V/T";
    private static final String ERR_DIV_BY_ZERO = "/0";
    // --------------------------------------------------------------------------------------------

    private final Stack<Token> stack;

    public StackMachine() {
        stack = new Stack<>();
    }

    public Token evaluate(LinkedList<Token> list) {
        while (!list.isEmpty()) {
            Token current = list.pop();
            Token.TokenType type = current.type();
            switch (type) {
                case VALUE:
                case TIME_UNIT:
                    stack.push(current);
                    break;
                default:
                    try {
                        Token right = stack.pop();
                        Token left = stack.pop();
                        stack.push(performOperation(left, right, type));
                    } catch (IllegalStateException ex) {
                        throw new IllegalArgumentException(current.value(), ex);
                    }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalStateException();
        }
        return stack.pop();
    }

    private static Token performOperation(@NonNull Token left,
                                          @NonNull Token right,
                                          @NonNull Token.TokenType type) {
        switch (type) {
            case PLUS:
                return add(left, right);
            case MINUS:
                return subtract(left, right);
            case MUL:
                return multiply(left, right);
            case DIV:
                return divide(left, right);
            default:
                throw new IllegalStateException();
        }
    }

    static Token add(@NonNull Token left, @NonNull Token right) {
        Token.TokenType type;
        String result;

        if (left.type() == Token.TokenType.VALUE) {
            switch (right.type()) {
                case VALUE:
                    result = String.valueOf(
                            Double.parseDouble(left.value()) + Double.parseDouble(right.value())
                    );
                    type = Token.TokenType.VALUE;
                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_V_PLUS_T);
                default:
                    throw new IllegalArgumentException(OP_PLUS + right.value());
            }
        } else if (left.type() == Token.TokenType.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    throw new IllegalArgumentException(ERR_T_PLUS_V);
                case TIME_UNIT:
                    result = String.valueOf(
                            Long.parseLong(left.value()) + Long.parseLong(right.value())
                    );
                    type = Token.TokenType.TIME_UNIT;
                    break;
                default:
                    throw new IllegalArgumentException(OP_PLUS + right.value());
            }
        } else {
            throw new IllegalArgumentException(left.value());
        }

        return Token.create(type, result);
    }

    static Token subtract(@NonNull Token left, @NonNull Token right) {
        Token.TokenType type;
        String result;

        if (left.type() == Token.TokenType.VALUE) {
            switch (right.type()) {
                case VALUE:
                    result = String.valueOf(
                            Double.parseDouble(left.value()) - Double.parseDouble(right.value())
                    );
                    type = Token.TokenType.VALUE;
                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_V_MINUS_T);
                default:
                    throw new IllegalArgumentException(OP_MINUS + right.value());
            }
        } else if (left.type() == Token.TokenType.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    throw new IllegalArgumentException(ERR_T_MINUS_V);
                case TIME_UNIT:
                    result = String.valueOf(
                            Long.parseLong(left.value()) - Long.parseLong(right.value())
                    );
                    type = Token.TokenType.TIME_UNIT;
                    break;
                default:
                    throw new IllegalArgumentException(OP_MINUS + right.value());
            }
        } else {
            throw new IllegalArgumentException(left.value());
        }

        return Token.create(type, result);
    }

    static Token multiply(@NonNull Token left, @NonNull Token right) {
        Token.TokenType type;
        String result;

        if (left.type() == Token.TokenType.VALUE) {
            switch (right.type()) {
                case VALUE:
                    result = String.valueOf(
                            Double.parseDouble(left.value()) * Double.parseDouble(right.value())
                    );
                    type = Token.TokenType.VALUE;
                    break;
                case TIME_UNIT:
                    double value = Double.parseDouble(left.value());
                    long millis = Long.parseLong(right.value());
                    result = String.valueOf(Math.round(value * millis));
                    type = Token.TokenType.TIME_UNIT;
                    break;
                default:
                    throw new IllegalArgumentException(OP_MUL + right.value());
            }
        } else if (left.type() == Token.TokenType.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    long millis = Long.parseLong(left.value());
                    double value = Double.parseDouble(right.value());
                    result = String.valueOf(Math.round(millis * value));
                    type = Token.TokenType.TIME_UNIT;
                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_T_MUL_T);
                default:
                    throw new IllegalArgumentException(OP_MUL + right.value());
            }
        } else {
            throw new IllegalArgumentException(left.value());
        }

        return Token.create(type, result);
    }

    static Token divide(@NonNull Token left, @NonNull Token right) {
        Token.TokenType type;
        String result;

        if (left.type() == Token.TokenType.VALUE) {
            switch (right.type()) {
                case VALUE:
                    double leftValue = Double.parseDouble(left.value());
                    double rightValue = Double.parseDouble(right.value());

                    if (rightValue != 0) {
                        result = String.valueOf(leftValue / rightValue);
                        type = Token.TokenType.VALUE;
                    } else {
                        throw new ArithmeticException(ERR_DIV_BY_ZERO);
                    }

                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_V_DIV_T);
                default:
                    throw new IllegalArgumentException(OP_DIV + right.value());
            }

        } else if (left.type() == Token.TokenType.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    long millis = Long.parseLong(left.value());
                    double value = Double.parseDouble(right.value());

                    if (value != 0) {
                        result = String.valueOf(Math.round(millis / value));
                        type = Token.TokenType.TIME_UNIT;
                    } else {
                        throw new ArithmeticException(ERR_DIV_BY_ZERO);
                    }

                    break;
                case TIME_UNIT:
                    long leftMillis = Long.parseLong(left.value());
                    long rightMillis = Long.parseLong(right.value());

                    if (rightMillis != 0) {
                        result = String.valueOf((double) leftMillis / rightMillis);
                        type = Token.TokenType.VALUE;
                    } else {
                        throw new ArithmeticException(ERR_DIV_BY_ZERO);
                    }

                    break;
                default:
                    throw new IllegalArgumentException(OP_DIV + right.value());
            }
        } else {
            throw new IllegalArgumentException(left.value());
        }
        return Token.create(type, result);
    }
}
