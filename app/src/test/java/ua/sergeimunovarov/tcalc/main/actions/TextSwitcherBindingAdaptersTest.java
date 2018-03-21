/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.actions;

import android.content.Context;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import org.junit.Test;

import ua.sergeimunovarov.tcalc.main.views.TextSwitcherBindingAdapters;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TextSwitcherBindingAdaptersTest {

    @Test
    public void setInAnimation() {
        TextSwitcher textSwitcher = mock(TextSwitcher.class);
        Context context = mock(Context.class);
        when(textSwitcher.getContext()).thenReturn(context);

        TextSwitcherBindingAdapters.setInAnimation(textSwitcher, android.R.anim.slide_in_left);

        verify(textSwitcher).setInAnimation(eq(context), eq(android.R.anim.slide_in_left));
    }


    @Test
    public void setOutAnimation() {
        TextSwitcher textSwitcher = mock(TextSwitcher.class);
        Context context = mock(Context.class);
        when(textSwitcher.getContext()).thenReturn(context);

        TextSwitcherBindingAdapters.setOutAnimation(textSwitcher, android.R.anim.slide_out_right);

        verify(textSwitcher).setOutAnimation(eq(context), eq(android.R.anim.slide_out_right));
    }


    @Test
    public void setTextViewFactory() {
        TextSwitcher textSwitcher = mock(TextSwitcher.class);
        ViewSwitcher.ViewFactory viewFactory = mock(ViewSwitcher.ViewFactory.class);

        TextSwitcherBindingAdapters.setTextViewFactory(textSwitcher, viewFactory);

        verify(textSwitcher).setFactory(eq(viewFactory));
    }
}
