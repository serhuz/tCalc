/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import androidx.annotation.NonNull;



public class Parser {

    private static final String PREFIX_NEGATIVE = "#";
    private static final String REPLACEMENT_MINUS = "-";

    private final LinkedList<Token> mOutput = new LinkedList<>();
    private final Stack<Token> mOperatorStack = new Stack<>();


    public LinkedList<Token> tokenizeExpression(@NonNull String expression) {
        String s = expression.replaceAll(" ", "");
        s = s.replaceAll("\\(-(\\d+(\\.\\d+)?)\\)", "#$1");
        StringTokenizer tokenizer = new StringTokenizer(s, "()+-*/", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            boolean matchFound = false;
            for (InputPattern inputPattern : InputPattern.values()) {
                if (!inputPattern.supportsParser()) continue;

                Matcher matcher = inputPattern.getPattern().matcher(token);
                if (matcher.matches()) {
                    matchFound = true;
                    switch (inputPattern.getType()) {
                        case BRACKET_OPEN:
                            mOperatorStack.push(Token.create(Type.BRACKET_OPEN, token));
                            break;
                        case BRACKET_CLOSE:
                            processClosingBracket();
                            break;
                        case PLUS:
                            processLowerOrderOperation(token, Type.PLUS);
                            break;
                        case MINUS:
                            processLowerOrderOperation(token, Type.MINUS);
                            break;
                        case MUL:
                            processHigherOrderOperation(token, Type.MUL);
                            break;
                        case DIV:
                            processHigherOrderOperation(token, Type.DIV);
                            break;
                        case TIME_UNIT:
                            mOutput.add(Token.create(
                                    Type.TIME_UNIT, Converter.toMillis(token)
                            ));
                            break;
                        case VALUE:
                            if (token.startsWith(PREFIX_NEGATIVE)) {
                                token = token.replaceFirst(PREFIX_NEGATIVE, REPLACEMENT_MINUS);
                            }
                            mOutput.add(Token.create(Type.VALUE, Double.parseDouble(token)));
                            break;
                    }
                    // Since match was found there is no need to iterate further.
                    break;
                }
            }

            if (!matchFound) {
                throw new IllegalArgumentException(token);
            }
        }

        while (!mOperatorStack.isEmpty()) {
            mOutput.add(mOperatorStack.pop());
        }
        return mOutput;
    }


    private void processClosingBracket() {
        boolean openingBracketPresent = false;
        while (!mOperatorStack.isEmpty()) {
            if (mOperatorStack.peek().type() != Type.BRACKET_OPEN) {
                mOutput.add(mOperatorStack.pop());
            } else {
                openingBracketPresent = true;
                mOperatorStack.pop();
                break;
            }
        }
        if (!openingBracketPresent) throw new NoSuchElementException("(");
    }


    private void processLowerOrderOperation(@NonNull String token, @NonNull Type type) {
        if (type != Type.PLUS && type != Type.MINUS) {
            throw new IllegalStateException();
        }
        while (!mOperatorStack.isEmpty()) {
            if (mOperatorStack.peek().type() != Type.BRACKET_OPEN) {
                mOutput.add(mOperatorStack.pop());
            } else {
                break;
            }
        }
        mOperatorStack.push(Token.create(type, token));
    }


    private void processHigherOrderOperation(@NonNull String token, @NonNull Type type) {
        if (type != Type.MUL && type != Type.DIV) {
            throw new IllegalStateException();
        }
        while (!mOperatorStack.isEmpty()) {
            Type peekType = mOperatorStack.peek().type();
            if (peekType == Type.MUL || peekType == Type.DIV) {
                mOutput.add(mOperatorStack.pop());
            } else {
                break;
            }
        }
        mOperatorStack.push(Token.create(type, token));
    }
}
