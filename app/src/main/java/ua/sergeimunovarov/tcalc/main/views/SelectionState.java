/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import com.google.auto.value.AutoValue;

import androidx.annotation.IntRange;


@AutoValue
public abstract class SelectionState {

    public static SelectionState create(@IntRange(from = 0) int selectionStart,
                                        @IntRange(from = 0) int selectionEnd) {
        return new AutoValue_SelectionState(selectionStart, selectionEnd);
    }


    public static SelectionState create(int position) {
        return new AutoValue_SelectionState(position, position);
    }


    public abstract int selectionStart();


    public abstract int selectionEnd();
}
