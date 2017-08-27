/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ua.sergeimunovarov.tcalc.main.ops.CalcFacade;
import ua.sergeimunovarov.tcalc.main.ops.Result;


@SuppressWarnings("WeakerAccess")
public class CalculationLoader extends AsyncTaskLoader<Result> {

    public static final String KEY_FORMAT = "arg.format";
    public static final String KEY_INPUT = "arg.input";

    private String input;
    private int format;
    private Result result;

    public CalculationLoader(@NonNull Context context,
                             @Nullable Bundle args) {
        super(context);
        if (args != null) {
            format = args.getInt(KEY_FORMAT);
            input = args.getString(KEY_INPUT);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (result != null) {
            deliverResult(result);
        } else {
            forceLoad();
        }
    }

    @Override
    public Result loadInBackground() {
        if (input != null) {
            CalcFacade calcFacade = new CalcFacade(format);
            result = calcFacade.calculateResult(input);
            return result;
        } else {
            return null;
        }
    }
}
