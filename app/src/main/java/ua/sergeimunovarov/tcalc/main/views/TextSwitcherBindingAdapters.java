/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;


@SuppressWarnings("WeakerAccess")
public final class TextSwitcherBindingAdapters {

    private TextSwitcherBindingAdapters() {
        throw new AssertionError();
    }


    @BindingAdapter("inAnimation")
    public static void setInAnimation(@NonNull TextSwitcher view,
                                      @AnimRes int animationId) {
        view.setInAnimation(view.getContext(), animationId);
    }


    @BindingAdapter("outAnimation")
    public static void setOutAnimation(@NonNull TextSwitcher view,
                                       @AnimRes int animationId) {
        view.setOutAnimation(view.getContext(), animationId);
    }


    @BindingAdapter("textViewFactory")
    public static void setTextViewFactory(@NonNull TextSwitcher view,
                                          @NonNull ViewSwitcher.ViewFactory factory) {
        view.setFactory(factory);
    }
}
