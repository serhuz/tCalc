/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.help;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.io.IOException;
import java.io.InputStream;

import ua.sergeimunovarov.tcalc.AbstractTransitionActivity;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.databinding.ActivityHelpBinding;


public class HelpActivity extends AbstractTransitionActivity {

    private static final String TAG = HelpActivity.class.getSimpleName();
    private static final String TAG_RATE = "rate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_help);

        WebView webView = binding.infoWebview;
        webView.setHapticFeedbackEnabled(false);
        webView.setOnClickListener(v -> {
        });
        webView.setLongClickable(false);
        webView.setOnLongClickListener(v -> true);
        webView.loadData(getHelpData(), "text/html", "utf-8");
    }


    private String getHelpData() {
        String data = "";
        InputStream inputStream = getResources().openRawResource(R.raw.help);
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            data = new String(buffer);
            inputStream.close();
        } catch (IOException e) {
            Log.w(TAG, "Cannot read help file", e);
        }
        return data;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_rate:
                showRateDialog();
                return true;
            case R.id.action_licenses:
                showOssLicences();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showOssLicences() {
        Intent intent = new Intent(this, OssLicensesMenuActivity.class);
        intent.putExtra("title", getString(R.string.action_licenses));
        startActivity(intent);
    }


    private void showRateDialog() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_RATE);
        if (fragment != null) {
            transaction.remove(fragment);
        }
        transaction.addToBackStack(null);

        RateAppDialog.create().show(transaction, TAG_RATE);
    }
}
