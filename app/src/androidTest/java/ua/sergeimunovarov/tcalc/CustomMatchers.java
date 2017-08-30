/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


public final class CustomMatchers {

    private CustomMatchers() {
        throw new AssertionError();
    }


    public static Matcher<View> hasText() {
        return new BoundedMatcher<View, EditText>(EditText.class) {
            @Override
            protected boolean matchesSafely(EditText editText) {
                return !TextUtils.isEmpty(editText.getText().toString());
            }


            @Override
            public void describeTo(Description description) {
                description.appendText("has text");
            }
        };
    }
}
