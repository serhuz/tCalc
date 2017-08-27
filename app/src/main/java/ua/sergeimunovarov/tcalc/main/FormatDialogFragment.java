/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.util.Log;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.R;

public class FormatDialogFragment extends DialogFragment
        implements DialogInterface.OnClickListener {

    @Inject
    ApplicationPreferences preferences;

    public interface FormatSelectionListener {

        /**
         * Notifies implementing classes about calculation output format change event.
         *
         * @param which format position in formats array
         */
        void onOutputFormatChanged(int which);

    }

    private static final String TAG = FormatDialogFragment.class.getSimpleName();

    private FormatSelectionListener mListener;

    public static FormatDialogFragment create() {
        return new FormatDialogFragment();
    }

    @CallSuper
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity(), R.style.DialogAnimation
        );

        builder.setTitle(R.string.title_dialog_format)
                .setSingleChoiceItems(
                        R.array.formats,
                        preferences.loadFormatPreference(),
                        this
                );

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        preferences.storeFormatPreference(which);
        mListener.onOutputFormatChanged(which);
        dialog.dismiss();
    }
}
