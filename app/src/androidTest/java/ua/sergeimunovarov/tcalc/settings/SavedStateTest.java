/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.settings;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.view.AbsSavedState;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(AndroidJUnit4.class)
public class SavedStateTest {

    @Test
    public void parcel() {
        SeekBarPreference.SavedState expected = new SeekBarPreference.SavedState(AbsSavedState.EMPTY_STATE);
        expected.mValue = 1;

        Parcel parcel = Parcel.obtain();
        expected.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        SeekBarPreference.SavedState actual = SeekBarPreference.SavedState.CREATOR.createFromParcel(parcel);

        assertThat(actual.mValue).isEqualTo(expected.mValue);
    }
}
