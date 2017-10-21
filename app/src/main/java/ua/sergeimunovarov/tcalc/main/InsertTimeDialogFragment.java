/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.sergeimunovarov.tcalc.R;

import static java.lang.System.currentTimeMillis;


public class InsertTimeDialogFragment extends DialogFragment {

    private static final String TAG = InsertTimeDialogFragment.class.getSimpleName();

    private static final String FORMAT_TSTAMP_HMS = "HH:mm:ss";
    private static final String FORMAT_TSTAMP_HM = "HH:mm";

    private static final int FORMAT_HMS = 0;
    private static final int FORMAT_HM = 1;

    private TimeInsertionListener mListener;


    public static InsertTimeDialogFragment create() {
        return new InsertTimeDialogFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (TimeInsertionListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "Host activity should implement TimeInsertionListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity(), R.style.DialogAnimation
        );

        builder.setTitle(R.string.select_time_format)
                .setItems(
                        R.array.timestamp_formats,
                        (dialogInterface, which) -> {
                            Date current = new Date(currentTimeMillis());
                            String timestamp;
                            switch (which) {
                                case FORMAT_HMS:
                                    timestamp =
                                            new SimpleDateFormat(FORMAT_TSTAMP_HMS, Locale.ENGLISH).format(current);
                                    break;
                                case FORMAT_HM:
                                    timestamp =
                                            new SimpleDateFormat(FORMAT_TSTAMP_HM, Locale.ENGLISH).format(current);
                                    break;
                                default:
                                    throw new IllegalStateException();
                            }
                            mListener.onTimeSelected(timestamp);
                        }
                );

        return builder.create();
    }


    public interface TimeInsertionListener {

        void onTimeSelected(String timestamp);
    }
}
