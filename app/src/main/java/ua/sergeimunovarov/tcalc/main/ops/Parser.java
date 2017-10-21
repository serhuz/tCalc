/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import ua.sergeimunovarov.tcalc.BuildConfig;
import ua.sergeimunovarov.tcalc.Patterns;

import static ua.sergeimunovarov.tcalc.Patterns.MINUS;


public class Parser {

    private static final String TAG = Parser.class.getSimpleName();

    private final LinkedList<Token> mOutput;
    private final Stack<Token> mOperatorStack;


    public Parser() {
        mOutput = new LinkedList<>();
        mOperatorStack = new Stack<>();
    }


    public LinkedList<Token> tokenizeExpression(@NonNull String expression) {
        String s = expression.replaceAll(" ", "");
        s = s.replaceAll("\\(-(\\d+(\\.\\d+)?)\\)", "#$1");
        StringTokenizer tokenizer = new StringTokenizer(s, "()+-*/", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            boolean matchFound = false;
            for (ParserTokenType parserTokenType : ParserTokenType.values()) {
                Matcher matcher = parserTokenType.getPattern().matcher(token);
                if (matcher.find()) {

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "current token: " + token);
                    }

                    matchFound = true;
                    switch (parserTokenType.getType()) {
                        case BRACKET_OPEN:
                            mOperatorStack.push(Token.create(Token.TokenType.BRACKET_OPEN, token));
                            break;
                        case BRACKET_CLOSE:
                            processClosingBracket();
                            break;
                        case PLUS:
                            processLowerOrderOperation(token, Token.TokenType.PLUS);
                            break;
                        case MINUS:
                            processLowerOrderOperation(token, Token.TokenType.MINUS);
                            break;
                        case MUL:
                            processHigherOrderOperation(token, Token.TokenType.MUL);
                            break;
                        case DIV:
                            processHigherOrderOperation(token, Token.TokenType.DIV);
                            break;
                        case TIME_UNIT:
                            mOutput.add(Token.create(
                                    Token.TokenType.TIME_UNIT, Converter.toMillis(token)
                            ));
                            break;
                        case VALUE:
                            if (token.startsWith(Patterns.PREFIX_SHARP)) {
                                token = token.replaceFirst(Patterns.PREFIX_SHARP, MINUS);
                            }
                            mOutput.add(Token.create(Token.TokenType.VALUE, Double.parseDouble(token)));
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
            if (mOperatorStack.peek().type() != Token.TokenType.BRACKET_OPEN) {
                mOutput.add(mOperatorStack.pop());
            } else {
                openingBracketPresent = true;
                mOperatorStack.pop();
                break;
            }
        }
        if (!openingBracketPresent) throw new NoSuchElementException("(");
    }


    private void processLowerOrderOperation(@NonNull String token, @NonNull Token.TokenType type) {
        if (type != Token.TokenType.PLUS && type != Token.TokenType.MINUS) {
            throw new IllegalStateException();
        }
        while (!mOperatorStack.isEmpty()) {
            if (mOperatorStack.peek().type() != Token.TokenType.BRACKET_OPEN) {
                mOutput.add(mOperatorStack.pop());
            } else {
                break;
            }
        }
        mOperatorStack.push(Token.create(type, token));
    }


    private void processHigherOrderOperation(@NonNull String token, @NonNull Token.TokenType type) {
        if (type != Token.TokenType.MUL && type != Token.TokenType.DIV) {
            throw new IllegalStateException();
        }
        while (!mOperatorStack.isEmpty()) {
            Token.TokenType peekType = mOperatorStack.peek().type();
            if (peekType == Token.TokenType.MUL || peekType == Token.TokenType.DIV) {
                mOutput.add(mOperatorStack.pop());
            } else {
                break;
            }
        }
        mOperatorStack.push(Token.create(type, token));
    }
}
