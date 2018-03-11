/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.input;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.BR;
import ua.sergeimunovarov.tcalc.di.annotation.MainActivityFactory;
import ua.sergeimunovarov.tcalc.main.MainActivityViewModel;


/**
 * A base class for fragments which contain input layouts.
 */
public abstract class BaseInputFragment extends Fragment {

    @Inject
    @MainActivityFactory
    ViewModelProvider.Factory mFactory;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.getAppComponent().inject(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                inflater, getFragmentLayoutId(), container, false
        );

        MainActivityViewModel viewModel =
                ViewModelProviders.of(getActivity(), mFactory).get(MainActivityViewModel.class);

        binding.setVariable(BR.model, viewModel);
        return binding.getRoot();
    }


    protected abstract int getFragmentLayoutId();
}
