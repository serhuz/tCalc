/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.R;


public class SettingsFragment extends PreferenceFragment {

    @Inject
    ApplicationPreferences mPreferences;


    public static SettingsFragment create() {
        return new SettingsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.getAppComponent().inject(this);
        addPreferencesFromResource(R.xml.prefs);

        Preference layout = findPreference(ApplicationPreferences.PreferenceKeys.KEY_LAYOUT);
        bindPreferenceSummaryToValue(layout);

        Preference precision = findPreference(ApplicationPreferences.PreferenceKeys.KEY_PRECISION);
        bindPreferenceSummaryToValue(precision);

        Preference vibroEnabled =
                findPreference(ApplicationPreferences.PreferenceKeys.KEY_VIBRO_ENABLED);
        bindPreferenceSummaryToValue(vibroEnabled);

        Preference recalculationEnabled =
                findPreference(ApplicationPreferences.PreferenceKeys.KEY_RECALCULATE);
        bindPreferenceSummaryToValue(recalculationEnabled);

        Preference vibroDuration =
                findPreference(ApplicationPreferences.PreferenceKeys.KEY_VIBRO_DURATION);
        bindPreferenceSummaryToValue(vibroDuration);

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (!vibrator.hasVibrator()) {
            disablePreference(vibroEnabled);
            disablePreference(vibroDuration);
        }
    }


    private void bindPreferenceSummaryToValue(@NonNull Preference pref) {
        pref.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        SharedPreferences prefs = mPreferences.getPreferences();
        if (pref instanceof ListPreference) {
            String prefValue = prefs.getString(pref.getKey(), "");
            sBindPreferenceSummaryToValueListener.onPreferenceChange(pref, prefValue);
        } else if (pref instanceof CheckBoxPreference) {
            boolean isEnabled = prefs.getBoolean(pref.getKey(), false);
            sBindPreferenceSummaryToValueListener.onPreferenceChange(pref, isEnabled);
        } else if (pref instanceof SeekBarPreference) {
            int prefValue = prefs.getInt(pref.getKey(), ApplicationPreferences.Defaults.DEFAULT_PRECISION);
            sBindPreferenceSummaryToValueListener.onPreferenceChange(pref, prefValue);
        }
    }


    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            (preference, value) -> {
                if (preference instanceof CheckBoxPreference) {
                    boolean checked = Boolean.parseBoolean(value.toString());
                    ((CheckBoxPreference) preference).setChecked(checked);
                } else if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(value.toString());
                    preference.setSummary(
                            index >= 0 ? listPreference.getEntries()[index] : null
                    );
                } else {
                    String stringValue = value.toString();
                    preference.setSummary(stringValue);
                }
                return true;
            };


    private static void disablePreference(@NonNull Preference preference) {
        preference.setShouldDisableView(true);
        preference.setEnabled(false);
    }
}
