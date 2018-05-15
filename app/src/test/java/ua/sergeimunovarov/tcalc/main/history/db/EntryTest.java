/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import org.junit.Test;

import ua.sergeimunovarov.tcalc.main.ops.Result;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;


public class EntryTest {

    @Test
    public void equals() {
        Entry first = new Entry("1+1", Result.ResultType.RESULT_OK_VALUE, "2", 0, 0);
        Entry second = new Entry("1+1", Result.ResultType.RESULT_OK_VALUE, "2", 0, 0);

        assertThat(first.equals(second)).isTrue();
    }


    @Test
    public void notEquals() {
        Entry first = new Entry("1+1", Result.ResultType.RESULT_OK_VALUE, "2", 0, 0);
        Entry second = new Entry("1*1", Result.ResultType.RESULT_OK_VALUE, "1", 0, 0);

        assertThat(first.equals(second)).isFalse();
    }


    @Test
    public void sameContents1() {
        Entry first = new Entry("1+2", Result.ResultType.RESULT_OK_VALUE, "3", 0L, 0);
        Entry second = new Entry("1+2", Result.ResultType.RESULT_OK_VALUE, "3", 100L, 0);

        assertThat(first.sameContents(second)).isTrue();
    }


    @Test
    public void sameContents2() {
        Entry first = new Entry("1+2", Result.ResultType.RESULT_OK_VALUE, "3", 0L, 0);
        Entry second = new Entry("1+2", Result.ResultType.RESULT_OK_VALUE, "3", 100L, 1);

        assertThat(first.sameContents(second)).isTrue();
    }


    @Test
    public void differentContents1() {
        Entry first = new Entry("1:00+2:00", Result.ResultType.RESULT_OK, "3:00:00", 0L, 1);
        Entry second = new Entry("1:00+2:00", Result.ResultType.RESULT_OK, "180:00", 100L, 2);

        assertThat(first.sameContents(second)).isFalse();
    }


    @Test
    public void differentContents2() {
        Entry first = new Entry("1:00+2:00", Result.ResultType.RESULT_OK, "3:00:00", 0L, 1);
        Entry second = new Entry("2:00+1:00", Result.ResultType.RESULT_OK, "3:00:00", 100L, 1);

        assertThat(first.sameContents(second)).isFalse();
    }


    @Test
    public void throwIllegalArgumentExceptionForResultType() {
        Entry first = new Entry("12345/0", Result.ResultType.RESULT_ERR, "Err", 1000L, 1);

        assertThatThrownBy(() -> first.sameContents(first)).isInstanceOf(IllegalArgumentException.class);
    }
}
