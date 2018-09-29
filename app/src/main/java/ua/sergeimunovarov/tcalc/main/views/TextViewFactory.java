/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;


public class TextViewFactory implements ViewSwitcher.ViewFactory {

    private final TextSwitcher mParent;
    private final int mLayoutRes;


    public TextViewFactory(@NonNull TextSwitcher parent, @LayoutRes int layoutRes) {
        mParent = parent;
        mLayoutRes = layoutRes;
    }


    @Override
    public View makeView() {
        return LayoutInflater
                .from(mParent.getContext())
                .inflate(mLayoutRes, mParent, false);
    }
}
