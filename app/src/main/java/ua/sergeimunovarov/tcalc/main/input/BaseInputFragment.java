/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.input;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.sergeimunovarov.tcalc.BR;


/**
 * A base class for fragments which contain input layouts.
 */
public abstract class BaseInputFragment extends Fragment {

    public static final String TAG = BaseInputFragment.class.getSimpleName();

    protected InputListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (InputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity should implement InputListener", e);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, getFragmentLayoutId(), container, false);
        binding.setVariable(BR.model, new InputViewModel(mListener));
        return binding.getRoot();
    }


    protected abstract int getFragmentLayoutId();
}
