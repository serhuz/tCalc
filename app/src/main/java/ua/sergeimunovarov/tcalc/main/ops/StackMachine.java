/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Stack;


public class StackMachine {

    // Errors -------------------------------------------------------------------------------------
    static final String ERR_V_PLUS_T = "V+T";
    static final String ERR_T_PLUS_V = "T+V";
    static final String ERR_V_MINUS_T = "V-T";
    static final String ERR_T_MINUS_V = "T-V";
    static final String ERR_T_MUL_T = "T*T";
    static final String ERR_V_DIV_T = "V/T";
    static final String ERR_DIV_BY_ZERO = "/0";
    // --------------------------------------------------------------------------------------------

    // Arithmetic operations ----------------------------------------------------------------------
    private static final String OP_PLUS = "+";
    private static final String OP_MINUS = "-";
    private static final String OP_MUL = "*";
    private static final String OP_DIV = "/";
    // --------------------------------------------------------------------------------------------

    private final Stack<Token> mStack = new Stack<>();


    public Token evaluate(LinkedList<Token> list) {
        while (!list.isEmpty()) {
            Token current = list.pop();
            Type type = current.type();
            switch (type) {
                case VALUE:
                case TIME_UNIT:
                    mStack.push(current);
                    break;
                default:
                    try {
                        Token right = mStack.pop();
                        Token left = mStack.pop();
                        mStack.push(performOperation(left, right, type));
                    } catch (IllegalStateException ex) {
                        throw new IllegalArgumentException(current.value().toString(), ex);
                    }
            }
        }

        if (mStack.size() != 1) {
            throw new IllegalStateException();
        }
        return mStack.pop();
    }


    private static Token performOperation(@NonNull Token left,
                                          @NonNull Token right,
                                          @NonNull Type type) {
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
        Type type;
        Object result;

        if (left.type() == Type.VALUE) {
            switch (right.type()) {
                case VALUE:
                    result = Double.class.cast(left.value()) + Double.class.cast(right.value());
                    type = Type.VALUE;
                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_V_PLUS_T);
                default:
                    throw new IllegalArgumentException(OP_PLUS + right.value());
            }
        } else if (left.type() == Type.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    throw new IllegalArgumentException(ERR_T_PLUS_V);
                case TIME_UNIT:
                    result = Long.class.cast(left.value()) + Long.class.cast(right.value());
                    type = Type.TIME_UNIT;
                    break;
                default:
                    throw new IllegalArgumentException(OP_PLUS + right.value());
            }
        } else {
            throw new IllegalArgumentException(left.value().toString());
        }

        return Token.create(type, result);
    }


    static Token subtract(@NonNull Token left, @NonNull Token right) {
        Type type;
        Object result;

        if (left.type() == Type.VALUE) {
            switch (right.type()) {
                case VALUE:
                    result = Double.class.cast(left.value()) - Double.class.cast(right.value());
                    type = Type.VALUE;
                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_V_MINUS_T);
                default:
                    throw new IllegalArgumentException(OP_MINUS + right.value());
            }
        } else if (left.type() == Type.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    throw new IllegalArgumentException(ERR_T_MINUS_V);
                case TIME_UNIT:
                    result = Long.class.cast(left.value()) - Long.class.cast(right.value());
                    type = Type.TIME_UNIT;
                    break;
                default:
                    throw new IllegalArgumentException(OP_MINUS + right.value());
            }
        } else {
            throw new IllegalArgumentException(left.value().toString());
        }

        return Token.create(type, result);
    }


    static Token multiply(@NonNull Token left, @NonNull Token right) {
        Type type;
        Object result;

        if (left.type() == Type.VALUE) {
            switch (right.type()) {
                case VALUE:
                    result = Double.class.cast(left.value()) * Double.class.cast(right.value());
                    type = Type.VALUE;
                    break;
                case TIME_UNIT:
                    result = Math.round(Double.class.cast(left.value()) * Long.class.cast(right.value()));
                    type = Type.TIME_UNIT;
                    break;
                default:
                    throw new IllegalArgumentException(OP_MUL + right.value().toString());
            }
        } else if (left.type() == Type.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    result = Math.round(Long.class.cast(left.value()) * Double.class.cast(right.value()));
                    type = Type.TIME_UNIT;
                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_T_MUL_T);
                default:
                    throw new IllegalArgumentException(OP_MUL + right.value().toString());
            }
        } else {
            throw new IllegalArgumentException(left.value().toString());
        }

        return Token.create(type, result);
    }


    static Token divide(@NonNull Token left, @NonNull Token right) {
        Type type;
        Object result;

        if (left.type() == Type.VALUE) {
            switch (right.type()) {
                case VALUE:
                    double leftValue = Double.class.cast(left.value());
                    double rightValue = Double.class.cast(right.value());

                    if (rightValue != 0) {
                        result = leftValue / rightValue;
                        type = Type.VALUE;
                    } else {
                        throw new ArithmeticException(ERR_DIV_BY_ZERO);
                    }

                    break;
                case TIME_UNIT:
                    throw new IllegalArgumentException(ERR_V_DIV_T);
                default:
                    throw new IllegalArgumentException(OP_DIV + right.value().toString());
            }
        } else if (left.type() == Type.TIME_UNIT) {
            switch (right.type()) {
                case VALUE:
                    long millis = Long.class.cast(left.value());
                    double value = Double.class.cast(right.value());

                    if (value != 0) {
                        result = Math.round(millis / value);
                        type = Type.TIME_UNIT;
                    } else {
                        throw new ArithmeticException(ERR_DIV_BY_ZERO);
                    }

                    break;
                case TIME_UNIT:
                    long leftMillis = Long.class.cast(left.value());
                    long rightMillis = Long.class.cast(right.value());

                    if (rightMillis != 0) {
                        result = (double) leftMillis / rightMillis;
                        type = Type.VALUE;
                    } else {
                        throw new ArithmeticException(ERR_DIV_BY_ZERO);
                    }

                    break;
                default:
                    throw new IllegalArgumentException(OP_DIV + right.value().toString());
            }
        } else {
            throw new IllegalArgumentException(left.value().toString());
        }
        return Token.create(type, result);
    }
}
