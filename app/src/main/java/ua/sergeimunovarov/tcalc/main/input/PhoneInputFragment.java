/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.input;

import ua.sergeimunovarov.tcalc.R;


public class PhoneInputFragment extends BaseInputFragment {

    public static PhoneInputFragment create() {
        return new PhoneInputFragment();
    }


    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_input_phone;
    }
}
