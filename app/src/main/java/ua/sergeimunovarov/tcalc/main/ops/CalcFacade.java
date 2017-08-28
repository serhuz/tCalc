/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.R;

public class CalcFacade {

    private static final String TAG = CalcFacade.class.getSimpleName();

    /**
     * Output format ID.
     *
     * @see ua.sergeimunovarov.tcalc.ApplicationPreferences.FormatConstants
     */
    private int mFormat;

    public CalcFacade(int format) {
        this.mFormat = format;
    }

    public Result calculateResult(@NonNull String expression) {
        String output;
        Result.ResultType type;

        Parser parser = new Parser();
        try {
            LinkedList<Token> parsedList = parser.tokenizeExpression(expression);

            StackMachine stackMachine = new StackMachine();
            Token resultToken;
            try {
                resultToken = stackMachine.evaluate(parsedList);

                switch (resultToken.type()) {
                    case VALUE:
                        output = Converter.formatValue(resultToken.value());
                        type = Result.ResultType.RESULT_OK_VALUE;
                        break;
                    case TIME_UNIT:
                        output = makeResult(resultToken.value());
                        type = Result.ResultType.RESULT_OK;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                Application.getAppComponent().appContext().getString(R.string.result_error_generic)
                        );
                }
            } catch (IllegalArgumentException | ArithmeticException ex) {
                // illegal argument in stack
                Log.w(TAG, "Illegal argument in stack", ex);
                output = Application.getAppComponent().appContext()
                        .getString(R.string.result_error_specific) + ex.getMessage() + ')';
                type = Result.ResultType.RESULT_ERR;
            } catch (EmptyStackException | IllegalStateException ex) {
                // thrown when operator and operands don't match
                Log.w(TAG, "operator and operands don't match", ex);
                output = Application.getAppComponent().appContext().getString(R.string.result_error_generic);
                type = Result.ResultType.RESULT_ERR;
            }
        } catch (IllegalArgumentException ex) {
            // illegal argument fed to parser
            Log.w(TAG, " illegal argument fed to parser", ex);
            output = Application.getAppComponent().appContext()
                    .getString(R.string.result_error_specific) + ex.getMessage() + ')';
            type = Result.ResultType.RESULT_ERR;
        } catch (NoSuchElementException ex) {
            // brackets mismatch
            Log.w(TAG, "brackets mismatch", ex);
            output = Application.getAppComponent().appContext().getString(R.string.result_error_generic);
            type = Result.ResultType.RESULT_ERR;
        }

        return Result.create(type, output);
    }

    protected String makeResult(@NonNull String timeUnit) {
        long millis;
        try {
            millis = Long.parseLong(timeUnit);
        } catch (NumberFormatException ex) {
            Log.d(TAG, "Result parsing error" + timeUnit, ex);
            // Since time units are checked beforehand in Parser, Converter
            // and StackMachine classes, this block should never be executed.
            // If it is executed, the token probably was tampered with.
            // Or the other checks have failed.
            // We won't notify user about this exception and simply return null.
            return null;
        }

        String result;
        switch (mFormat) {
            case ApplicationPreferences.FormatConstants.DHMS:
                result = Converter.formatDurationDhms(
                        Application.getAppComponent().appContext().getString(R.string.prefix_day),
                        millis
                );
                break;
            case ApplicationPreferences.FormatConstants.HMS:
                result = Converter.formatDurationHms(millis);
                break;
            case ApplicationPreferences.FormatConstants.MS:
                result = Converter.formatDurationMs(millis);
                break;
            case ApplicationPreferences.FormatConstants.HMS_MOD24:
                result = Converter.formatDurationHmsMod(millis);
                break;
            default:
                // If we are here, something went terribly wrong with format setting.
                // Since we don't know how to format the result, we simply set it to null.
                result = null;
        }
        return result;
    }

}
