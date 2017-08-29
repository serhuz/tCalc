/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.settings;

import android.os.Bundle;

import ua.sergeimunovarov.tcalc.AbstractTransitionActivity;
import ua.sergeimunovarov.tcalc.R;


/**
 * Holds {@link SettingsFragment}.
 */
public class SettingsActivity extends AbstractTransitionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SettingsFragment.create())
                .commit();
    }
}
