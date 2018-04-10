/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.widget.TextView;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class TextViewBindingAdaptersTest {

    @Test
    public void setStringFromArray() {
        TextView textView = mock(TextView.class);

        TextViewBindingAdapters.setStringFromArray(
                textView, new String[]{"HH:MM:SS"}, 0
        );

        verify(textView).setText(eq("HH:MM:SS"));
    }
}
