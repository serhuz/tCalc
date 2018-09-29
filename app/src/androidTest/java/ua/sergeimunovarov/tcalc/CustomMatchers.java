/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.annotation.NonNull;
import androidx.test.espresso.Root;
import androidx.test.espresso.matcher.BoundedMatcher;


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


    public static Matcher<View> hasCurrentText(@NonNull String text) {
        return new BoundedMatcher<View, TextSwitcher>(TextSwitcher.class) {
            @Override
            protected boolean matchesSafely(TextSwitcher item) {
                String currentText = ((TextView) item.getCurrentView()).getText().toString();
                return text.contentEquals(currentText);
            }


            @Override
            public void describeTo(Description description) {
                description.appendText("has current text: " + text);
            }
        };
    }


    public static Matcher<Root> isToast() {
        return new TypeSafeMatcher<Root>() {

            @Override
            public boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    return windowToken == appToken;
                }
                return false;
            }


            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }
        };
    }
}
