/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.help;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import ua.sergeimunovarov.tcalc.R;


public class RateAppDialog extends DialogFragment {

    public static RateAppDialog create() {
        return new RateAppDialog();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity(),
                R.style.DialogAnimation
        );

        return builder
                .setTitle(R.string.title_dialog_rate)
                .setCancelable(true)
                .setMessage(R.string.message_rate_dialog)
                .setPositiveButton(
                        R.string.btn_yes,
                        (dialogInterface, which) -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(getString(R.string.link_google_play)));
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                intent.setData(Uri.parse(getString(R.string.link_web)));
                                startActivity(intent);
                            } finally {
                                dismiss();
                            }
                        }
                ).setNegativeButton(
                        R.string.btn_no,
                        (dialogInterface, which) -> dismiss()
                ).create();
    }
}
