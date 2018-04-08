/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.viewmodel;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class StringProviderTest {

    @SuppressWarnings("WeakerAccess")
    @Mock
    Context mContext;

    private StringProvider mStringProvider;


    @Before
    public void setUp() {
        initMocks(this);
        mStringProvider = new StringProvider(mContext);
    }


    @After
    public void tearDown() {
        reset(mContext);
    }


    @Test
    public void provideFormattedString() {
        mStringProvider.provideFormattedString(0, "123");

        verify(mContext).getString(eq(0), eq("123"));
    }
}
