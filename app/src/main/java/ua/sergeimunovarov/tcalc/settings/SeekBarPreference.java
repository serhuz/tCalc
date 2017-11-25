/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.sergeimunovarov.tcalc.R;


public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private static final int LOCAL_DEFAULT_VALUE = 1;
    private static final int DEFAULT_MIN_VALUE = LOCAL_DEFAULT_VALUE;
    private static final int DEFAULT_MAX_VALUE = 5;

    private SeekBar mPrecisionSeekBar;
    private TextView mSeekBarValue;

    private int mCurrentValue;
    private int mMin;
    private int mMax;


    @SuppressWarnings("unused")
    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ua_sergeimunovarov_tcalc_settings_SeekBarPreference);
        mMin = a.getInt(R.styleable.ua_sergeimunovarov_tcalc_settings_SeekBarPreference_min, DEFAULT_MIN_VALUE);
        mMax = a.getInt(R.styleable.ua_sergeimunovarov_tcalc_settings_SeekBarPreference_max, DEFAULT_MAX_VALUE);
        a.recycle();

        setDialogLayoutResource(R.layout.seekbar_preference_dialog);

        setPositiveButtonText(R.string.btn_ok);
        setNegativeButtonText(R.string.btn_cancel);
        setDialogIcon(null);
    }


    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mPrecisionSeekBar = view.findViewById(R.id.seekbar);
        mSeekBarValue = view.findViewById(R.id.seekbar_value);

        mPrecisionSeekBar.setOnSeekBarChangeListener(this);
        mPrecisionSeekBar.setMax(mMax - mMin);
        mPrecisionSeekBar.setProgress(mCurrentValue - mMin);

        mSeekBarValue.setText(String.valueOf(mPrecisionSeekBar.getProgress() + mMin));
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mCurrentValue);
            OnPreferenceChangeListener changeListener = getOnPreferenceChangeListener();
            if (changeListener != null) {
                changeListener.onPreferenceChange(this, mCurrentValue);
            }
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if (isPersistent()) return superState;

        SavedState savedState = new SavedState(superState);
        savedState.mValue = mCurrentValue;
        return savedState;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentValue = ((SavedState) state).mValue;
        mPrecisionSeekBar.setProgress(mCurrentValue - mMin);
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, LOCAL_DEFAULT_VALUE);
    }


    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mCurrentValue = getPersistedInt(LOCAL_DEFAULT_VALUE);
        } else {
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mCurrentValue = progress + mMin;
            mSeekBarValue.setText(String.valueOf(mCurrentValue));
        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }


                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        int mValue;


        public SavedState(Parcel source) {
            super(source);
            mValue = source.readInt();
        }


        public SavedState(Parcelable superState) {
            super(superState);
        }


        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mValue);
        }
    }
}
