/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.ops;

import com.google.auto.value.AutoValue;

import androidx.annotation.NonNull;


@AutoValue
public abstract class Token<T> {

    public static <T> Token<T> create(@NonNull Type type, @NonNull T value) {
        return new AutoValue_Token<>(type, value);
    }


    @NonNull
    public abstract Type type();

    @NonNull
    public abstract T value();
}
