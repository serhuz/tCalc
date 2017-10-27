/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import ua.sergeimunovarov.tcalc.main.ops.CalcFacade;
import ua.sergeimunovarov.tcalc.main.ops.Result;


@SuppressWarnings("WeakerAccess")
public class CalculationLoader extends AsyncTaskLoader<Result> {

    public static final String KEY_FORMAT = "arg.mFormat";
    public static final String KEY_INPUT = "arg.mString";

    private String mString;
    private int mFormat;
    private Result mResult;


    public CalculationLoader(@NonNull Context context,
                             @Nullable Bundle args) {
        super(context);
        if (args != null) {
            mFormat = args.getInt(KEY_FORMAT);
            mString = args.getString(KEY_INPUT);
        }
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mResult != null) {
            deliverResult(mResult);
        } else {
            forceLoad();
        }
    }


    @Override
    public Result loadInBackground() {
        if (mString != null) {
            CalcFacade calcFacade = new CalcFacade(mFormat);
            mResult = calcFacade.calculateResult(mString);
            return mResult;
        } else {
            return null;
        }
    }
}
