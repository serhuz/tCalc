/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Provides global access to application preferences.
 */
public class ApplicationPreferences {

    private final SharedPreferences mPreferences;


    public ApplicationPreferences(@NonNull SharedPreferences preferences) {
        mPreferences = preferences;
    }


    /**
     * Stores selected calculation format
     *
     * @param formatId format int defined in {@link FormatConstants}
     * @see FormatConstants
     */
    public void storeFormatPreference(@FormatConstants.FormatId int formatId) {
        getPreferences()
                .edit()
                .putInt(PreferenceKeys.KEY_FORMAT, formatId)
                .apply();
    }


    public SharedPreferences getPreferences() {
        return mPreferences;
    }


    /**
     * Loads calculation format
     *
     * @return persisted format or {@link Defaults#DEFAULT_FORMAT} if none is set
     * @see FormatConstants
     */
    public int loadFormatPreference() {
        return getPreferences().getInt(PreferenceKeys.KEY_FORMAT, Defaults.DEFAULT_FORMAT);
    }


    /**
     * Loads vibration preference
     *
     * @return persisted value or false if none is set
     */
    public boolean loadVibrationPreference() {
        return getPreferences().getBoolean(PreferenceKeys.KEY_VIBRO_ENABLED, false);
    }


    /**
     * Loads vibration duration preference
     *
     * @return vibration duration or {@link Defaults#DEFAULT_DURATION} if none is set
     */
    public int loadVibrationDurationPreference() {
        return Integer.parseInt(
                getPreferences().getString(
                        PreferenceKeys.KEY_VIBRO_DURATION,
                        String.valueOf(Defaults.DEFAULT_DURATION)
                )
        );
    }


    /**
     * Loads input layout preference
     *
     * @return layout preference or int representation of {@link Defaults#DEFAULT_LAYOUT}
     * if none is set
     */
    public int loadLayoutPreference() {
        return Integer.parseInt(
                getPreferences().getString(PreferenceKeys.KEY_LAYOUT, Defaults.DEFAULT_LAYOUT)
        );
    }


    public int loadPrecisionPreference() {
        return getPreferences().getInt(PreferenceKeys.KEY_PRECISION, Defaults.DEFAULT_PRECISION);
    }

    /**
     * Preference identifiers for calculator layouts
     */
    public static final class LayoutConstants {

        /**
         * Old calculator layout
         *
         * @see ua.sergeimunovarov.tcalc.main.input.PhoneInputFragment
         */
        public static final int LAYOUT_OLD = 0;

        /**
         * New calculator layout
         *
         * @see ua.sergeimunovarov.tcalc.main.input.CalcInputFragment
         */
        public static final int LAYOUT_NEW = 1;
    }


    /**
     * Contains default preference values
     */
    public static final class Defaults {

        /**
         * Default output format used to format calculation output.
         */
        public static final int DEFAULT_FORMAT = FormatConstants.HMS;

        /**
         * Default vibration duration.
         */
        public static final int DEFAULT_DURATION = 50;

        /**
         * Default input layout.
         */
        public static final String DEFAULT_LAYOUT = String.valueOf(LayoutConstants.LAYOUT_OLD);

        /**
         * Default calculation precision
         */
        public static final int DEFAULT_PRECISION = 5;
    }


    /**
     * Contains preference key identifiers
     */
    public static final class PreferenceKeys {

        /**
         * Input layout preference key
         */
        public static final String KEY_LAYOUT = "layout";

        /**
         * Vibration preference key
         */
        public static final String KEY_VIBRO_ENABLED = "vibro_enabled";

        /**
         * Vibration duration preference key
         */
        public static final String KEY_VIBRO_DURATION = "vibro_duration";

        /**
         * Calculation output format key.
         */
        public final static String KEY_FORMAT = "format";

        /**
         * Calculation precision key
         */
        public static final String KEY_PRECISION = "precision";
    }


    /**
     * Contains available calculation format types
     */
    public static final class FormatConstants {

        /**
         * DDd. HH:MM:SS format
         */
        public static final int DHMS = 0;

        /**
         * HH:MM:SS format
         */
        public static final int HMS = 1;

        /**
         * MM:SS format
         */
        public static final int MS = 2;

        /**
         * (HH:MM:SS)%24H format
         */
        public static final int HMS_MOD24 = 3;


        @IntDef({DHMS, HMS, MS, HMS_MOD24})
        @Retention(RetentionPolicy.SOURCE)
        public @interface FormatId {

        }
    }
}
