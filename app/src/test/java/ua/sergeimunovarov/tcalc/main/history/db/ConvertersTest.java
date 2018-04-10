/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import org.junit.Before;
import org.junit.Test;

import ua.sergeimunovarov.tcalc.main.ops.Result;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static ua.sergeimunovarov.tcalc.main.ops.Result.ResultType.RESULT_OK;


public class ConvertersTest {


    private Converters mConverters;


    @Before
    public void setUp() {
        mConverters = new Converters();
    }


    @Test
    public void fromResultType() {
        String actual = mConverters.fromResultType(RESULT_OK);

        assertThat(actual).isEqualTo(RESULT_OK.name());
    }


    @Test
    public void toResultType() {
        Result.ResultType actual = mConverters.toResultType("RESULT_OK");

        assertThat(actual).isEqualTo(RESULT_OK);
    }
}
