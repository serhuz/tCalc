/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.input;

import ua.sergeimunovarov.tcalc.R;


public class CalcInputFragment extends BaseInputFragment {

    public static CalcInputFragment create() {
        return new CalcInputFragment();
    }


    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_input_calc;
    }
}
