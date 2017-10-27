/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.R;


public class FormatDialogFragment extends AppCompatDialogFragment
        implements DialogInterface.OnClickListener {

    private static final String TAG = FormatDialogFragment.class.getSimpleName();

    @Inject
    ApplicationPreferences mPreferences;

    private FormatSelectionListener mListener;


    public static FormatDialogFragment create() {
        return new FormatDialogFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.getAppComponent().inject(this);
        try {
            mListener = (FormatSelectionListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "Host activity should implement FormatSelectionListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity(), R.style.DialogAnimation
        );

        builder.setTitle(R.string.title_dialog_format)
                .setSingleChoiceItems(
                        R.array.formats,
                        mPreferences.loadFormatPreference(),
                        this
                );

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        mPreferences.storeFormatPreference(which);
        mListener.onOutputFormatChanged(which);
        dialog.dismiss();
    }


    public interface FormatSelectionListener {

        /**
         * Notifies implementing classes about calculation output format change event.
         *
         * @param which format position in formats array
         */
        void onOutputFormatChanged(int which);
    }
}
