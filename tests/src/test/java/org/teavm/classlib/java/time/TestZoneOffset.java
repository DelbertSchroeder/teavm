/*
 *  Copyright 2020 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
*
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.teavm.classlib.java.time;

import static java.time.temporal.ChronoField.OFFSET_SECONDS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.JulianFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.runner.RunWith;
import org.teavm.junit.TeaVMTestRunner;
import org.testng.annotations.Test;

/**
 * Test ZoneOffset.
 */
@Test
@RunWith(TeaVMTestRunner.class)
public class TestZoneOffset extends AbstractDateTimeTest {

    //-----------------------------------------------------------------------
    @Override
    protected List<TemporalAccessor> samples() {
        TemporalAccessor[] array = { ZoneOffset.ofHours(1), ZoneOffset.ofHoursMinutesSeconds(-5, -6, -30) };
        return Arrays.asList(array);
    }

    @Override
    protected List<TemporalField> validFields() {
        TemporalField[] array = {
            OFFSET_SECONDS,
        };
        return Arrays.asList(array);
    }

    @Override
    protected List<TemporalField> invalidFields() {
        List<TemporalField> list = new ArrayList<>(Arrays.asList(ChronoField.values()));
        list.removeAll(validFields());
        list.add(JulianFields.JULIAN_DAY);
        list.add(JulianFields.MODIFIED_JULIAN_DAY);
        list.add(JulianFields.RATA_DIE);
        return list;
    }

    //-----------------------------------------------------------------------
    // constants
    //-----------------------------------------------------------------------
    @Test
    public void test_constant_UTC() {
        ZoneOffset test = ZoneOffset.UTC;
        doTestOffset(test, 0, 0, 0);
    }

    @Test
    public void test_constant_MIN() {
        ZoneOffset test = ZoneOffset.MIN;
        doTestOffset(test, -18, 0, 0);
    }

    @Test
    public void test_constant_MAX() {
        ZoneOffset test = ZoneOffset.MAX;
        doTestOffset(test, 18, 0, 0);
    }

    //-----------------------------------------------------------------------
    // of(String)
    //-----------------------------------------------------------------------
    @Test
    public void test_factory_string_UTC() {
        String[] values = new String[] {
            "Z", "+0",
            "+00", "+0000", "+00:00", "+000000", "+00:00:00",
            "-00", "-0000", "-00:00", "-000000", "-00:00:00",
        };
        for (int i = 0; i < values.length; i++) {
            ZoneOffset test = ZoneOffset.of(values[i]);
            assertSame(test, ZoneOffset.UTC);
        }
    }

    @Test
    public void test_factory_string_invalid() {
        String[] values = new String[] {
            "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "ZZ",
            "0", "+0:00", "+00:0", "+0:0",
            "+000", "+00000",
            "+0:00:00", "+00:0:00", "+00:00:0", "+0:0:0", "+0:0:00", "+00:0:0", "+0:00:0",
            "1", "+01_00", "+01;00", "+01@00", "+01:AA",
            "+19", "+19:00", "+18:01", "+18:00:01", "+1801", "+180001",
            "-0:00", "-00:0", "-0:0",
            "-000", "-00000",
            "-0:00:00", "-00:0:00", "-00:00:0", "-0:0:0", "-0:0:00", "-00:0:0", "-0:00:0",
            "-19", "-19:00", "-18:01", "-18:00:01", "-1801", "-180001",
            "-01_00", "-01;00", "-01@00", "-01:AA",
            "@01:00",
        };
        for (int i = 0; i < values.length; i++) {
            try {
                ZoneOffset.of(values[i]);
                fail("Should have failed:" + values[i]);
            } catch (DateTimeException ex) {
                // expected
            }
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_string_null() {
        ZoneOffset.of((String) null);
    }

    //-----------------------------------------------------------------------
    @Test
    public void test_factory_string_singleDigitHours() {
        for (int i = -9; i <= 9; i++) {
            String str = (i < 0 ? "-" : "+") + Math.abs(i);
            ZoneOffset test = ZoneOffset.of(str);
            doTestOffset(test, i, 0, 0);
        }
    }

    @Test
    public void test_factory_string_hours() {
        for (int i = -18; i <= 18; i++) {
            String str = (i < 0 ? "-" : "+") + Integer.toString(Math.abs(i) + 100).substring(1);
            ZoneOffset test = ZoneOffset.of(str);
            doTestOffset(test, i, 0, 0);
        }
    }

    @Test
    public void test_factory_string_hours_minutes_noColon() {
        for (int i = -17; i <= 17; i++) {
            for (int j = -59; j <= 59; j++) {
                if ((i < 0 && j <= 0) || (i > 0 && j >= 0) || i == 0) {
                    String str = (i < 0 || j < 0 ? "-" : "+")
                            + Integer.toString(Math.abs(i) + 100).substring(1)
                            + Integer.toString(Math.abs(j) + 100).substring(1);
                    ZoneOffset test = ZoneOffset.of(str);
                    doTestOffset(test, i, j, 0);
                }
            }
        }
        ZoneOffset test1 = ZoneOffset.of("-1800");
        doTestOffset(test1, -18, 0, 0);
        ZoneOffset test2 = ZoneOffset.of("+1800");
        doTestOffset(test2, 18, 0, 0);
    }

    @Test
    public void test_factory_string_hours_minutes_colon() {
        for (int i = -17; i <= 17; i++) {
            for (int j = -59; j <= 59; j++) {
                if ((i < 0 && j <= 0) || (i > 0 && j >= 0) || i == 0) {
                    String str = (i < 0 || j < 0 ? "-" : "+")
                            + Integer.toString(Math.abs(i) + 100).substring(1) + ":"
                            + Integer.toString(Math.abs(j) + 100).substring(1);
                    ZoneOffset test = ZoneOffset.of(str);
                    doTestOffset(test, i, j, 0);
                }
            }
        }
        ZoneOffset test1 = ZoneOffset.of("-18:00");
        doTestOffset(test1, -18, 0, 0);
        ZoneOffset test2 = ZoneOffset.of("+18:00");
        doTestOffset(test2, 18, 0, 0);
    }

    @Test
    public void test_factory_string_hours_minutes_seconds_noColon() {
        for (int i = -17; i <= 17; i++) {
            for (int j = -59; j <= 59; j++) {
                for (int k = -59; k <= 59; k++) {
                    if ((i < 0 && j <= 0 && k <= 0) || (i > 0 && j >= 0 && k >= 0)
                            || (i == 0 && ((j < 0 && k <= 0) || (j > 0 && k >= 0) || j == 0))) {
                        String str = (i < 0 || j < 0 || k < 0 ? "-" : "+")
                                + Integer.toString(Math.abs(i) + 100).substring(1)
                                + Integer.toString(Math.abs(j) + 100).substring(1)
                                + Integer.toString(Math.abs(k) + 100).substring(1);
                        ZoneOffset test = ZoneOffset.of(str);
                        doTestOffset(test, i, j, k);
                    }
                }
            }
        }
        ZoneOffset test1 = ZoneOffset.of("-180000");
        doTestOffset(test1, -18, 0, 0);
        ZoneOffset test2 = ZoneOffset.of("+180000");
        doTestOffset(test2, 18, 0, 0);
    }

    @Test
    public void test_factory_string_hours_minutes_seconds_colon() {
        for (int i = -17; i <= 17; i++) {
            for (int j = -59; j <= 59; j++) {
                for (int k = -59; k <= 59; k++) {
                    if ((i < 0 && j <= 0 && k <= 0) || (i > 0 && j >= 0 && k >= 0)
                            || (i == 0 && ((j < 0 && k <= 0) || (j > 0 && k >= 0) || j == 0))) {
                        String str = (i < 0 || j < 0 || k < 0 ? "-" : "+")
                                + Integer.toString(Math.abs(i) + 100).substring(1) + ":"
                                + Integer.toString(Math.abs(j) + 100).substring(1) + ":"
                                + Integer.toString(Math.abs(k) + 100).substring(1);
                        ZoneOffset test = ZoneOffset.of(str);
                        doTestOffset(test, i, j, k);
                    }
                }
            }
        }
        ZoneOffset test1 = ZoneOffset.of("-18:00:00");
        doTestOffset(test1, -18, 0, 0);
        ZoneOffset test2 = ZoneOffset.of("+18:00:00");
        doTestOffset(test2, 18, 0, 0);
    }

    //-----------------------------------------------------------------------
    @Test
    public void test_factory_int_hours() {
        for (int i = -18; i <= 18; i++) {
            ZoneOffset test = ZoneOffset.ofHours(i);
            doTestOffset(test, i, 0, 0);
        }
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_tooBig() {
        ZoneOffset.ofHours(19);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_tooSmall() {
        ZoneOffset.ofHours(-19);
    }

    //-----------------------------------------------------------------------
    @Test
    public void test_factory_int_hours_minutes() {
        for (int i = -17; i <= 17; i++) {
            for (int j = -59; j <= 59; j++) {
                if ((i < 0 && j <= 0) || (i > 0 && j >= 0) || i == 0) {
                    ZoneOffset test = ZoneOffset.ofHoursMinutes(i, j);
                    doTestOffset(test, i, j, 0);
                }
            }
        }
        ZoneOffset test1 = ZoneOffset.ofHoursMinutes(-18, 0);
        doTestOffset(test1, -18, 0, 0);
        ZoneOffset test2 = ZoneOffset.ofHoursMinutes(18, 0);
        doTestOffset(test2, 18, 0, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_tooBig() {
        ZoneOffset.ofHoursMinutes(19, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_tooSmall() {
        ZoneOffset.ofHoursMinutes(-19, 0);
    }

    //-----------------------------------------------------------------------
    @Test
    public void test_factory_int_hours_minutes_seconds() {
        for (int i = -17; i <= 17; i++) {
            for (int j = -59; j <= 59; j++) {
                for (int k = -59; k <= 59; k++) {
                    if ((i < 0 && j <= 0 && k <= 0) || (i > 0 && j >= 0 && k >= 0)
                            || (i == 0 && ((j < 0 && k <= 0) || (j > 0 && k >= 0) || j == 0))) {
                        ZoneOffset test = ZoneOffset.ofHoursMinutesSeconds(i, j, k);
                        doTestOffset(test, i, j, k);
                    }
                }
            }
        }
        ZoneOffset test1 = ZoneOffset.ofHoursMinutesSeconds(-18, 0, 0);
        doTestOffset(test1, -18, 0, 0);
        ZoneOffset test2 = ZoneOffset.ofHoursMinutesSeconds(18, 0, 0);
        doTestOffset(test2, 18, 0, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_plusHoursMinusMinutes() {
        ZoneOffset.ofHoursMinutesSeconds(1, -1, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_plusHoursMinusSeconds() {
        ZoneOffset.ofHoursMinutesSeconds(1, 0, -1);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_minusHoursPlusMinutes() {
        ZoneOffset.ofHoursMinutesSeconds(-1, 1, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_minusHoursPlusSeconds() {
        ZoneOffset.ofHoursMinutesSeconds(-1, 0, 1);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_zeroHoursMinusMinutesPlusSeconds() {
        ZoneOffset.ofHoursMinutesSeconds(0, -1, 1);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_zeroHoursPlusMinutesMinusSeconds() {
        ZoneOffset.ofHoursMinutesSeconds(0, 1, -1);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_minutesTooLarge() {
        ZoneOffset.ofHoursMinutesSeconds(0, 60, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_minutesTooSmall() {
        ZoneOffset.ofHoursMinutesSeconds(0, -60, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_secondsTooLarge() {
        ZoneOffset.ofHoursMinutesSeconds(0, 0, 60);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_secondsTooSmall() {
        ZoneOffset.ofHoursMinutesSeconds(0, 0, 60);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_hoursTooBig() {
        ZoneOffset.ofHoursMinutesSeconds(19, 0, 0);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_int_hours_minutes_seconds_hoursTooSmall() {
        ZoneOffset.ofHoursMinutesSeconds(-19, 0, 0);
    }

    //-----------------------------------------------------------------------
    @Test
    public void test_factory_ofTotalSeconds() {
        assertEquals(ZoneOffset.ofTotalSeconds(60 * 60 + 1), ZoneOffset.ofHoursMinutesSeconds(1, 0, 1));
        assertEquals(ZoneOffset.ofTotalSeconds(18 * 60 * 60), ZoneOffset.ofHours(18));
        assertEquals(ZoneOffset.ofTotalSeconds(-18 * 60 * 60), ZoneOffset.ofHours(-18));
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_ofTotalSeconds_tooLarge() {
        ZoneOffset.ofTotalSeconds(18 * 60 * 60 + 1);
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_ofTotalSeconds_tooSmall() {
        ZoneOffset.ofTotalSeconds(-18 * 60 * 60 - 1);
    }

    //-----------------------------------------------------------------------
    // from(TemporalAccessor)
    //-----------------------------------------------------------------------
    @Test
    public void test_factory_TemporalAccessor() {
        assertEquals(ZoneOffset.from(OffsetTime.of(LocalTime.of(12, 30), ZoneOffset.ofHours(6))),
                ZoneOffset.ofHours(6));
        assertEquals(ZoneOffset.from(ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2007, 7, 15),
                LocalTime.of(17, 30)), ZoneOffset.ofHours(2))), ZoneOffset.ofHours(2));
    }

    @Test(expectedExceptions = DateTimeException.class)
    public void test_factory_TemporalAccessor_invalid_noDerive() {
        ZoneOffset.from(LocalTime.of(12, 30));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_TemporalAccessor_null() {
        ZoneOffset.from((TemporalAccessor) null);
    }

    //-----------------------------------------------------------------------
    // getTotalSeconds()
    //-----------------------------------------------------------------------
    @Test
    public void test_getTotalSeconds() {
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(60 * 60 + 1);
        assertEquals(offset.getTotalSeconds(), 60 * 60 + 1);
    }

    //-----------------------------------------------------------------------
    // getId()
    //-----------------------------------------------------------------------
    @Test
    public void test_getId() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutesSeconds(1, 0, 0);
        assertEquals(offset.getId(), "+01:00");
        offset = ZoneOffset.ofHoursMinutesSeconds(1, 2, 3);
        assertEquals(offset.getId(), "+01:02:03");
        offset = ZoneOffset.UTC;
        assertEquals(offset.getId(), "Z");
    }

    //-----------------------------------------------------------------------
    // getRules()
    //-----------------------------------------------------------------------
    @Test
    public void test_getRules() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutesSeconds(1, 2, 3);
        assertEquals(offset.getRules().isFixedOffset(), true);
        assertEquals(offset.getRules().getOffset((Instant) null), offset);
        assertEquals(offset.getRules().getDaylightSavings((Instant) null), Duration.ZERO);
        assertEquals(offset.getRules().getStandardOffset((Instant) null), offset);
        assertEquals(offset.getRules().nextTransition((Instant) null), null);
        assertEquals(offset.getRules().previousTransition((Instant) null), null);

        assertEquals(offset.getRules().isValidOffset((LocalDateTime) null, offset), true);
        assertEquals(offset.getRules().isValidOffset((LocalDateTime) null, ZoneOffset.UTC), false);
        assertEquals(offset.getRules().isValidOffset((LocalDateTime) null, null), false);
        assertEquals(offset.getRules().getOffset((LocalDateTime) null), offset);
        assertEquals(offset.getRules().getValidOffsets((LocalDateTime) null), Arrays.asList(offset));
        assertEquals(offset.getRules().getTransition((LocalDateTime) null), null);
        assertEquals(offset.getRules().getTransitions().size(), 0);
        assertEquals(offset.getRules().getTransitionRules().size(), 0);
    }

    //-----------------------------------------------------------------------
    // get(TemporalField)
    //-----------------------------------------------------------------------
    @Test
    public void test_get_TemporalField() {
        assertEquals(ZoneOffset.UTC.get(OFFSET_SECONDS), 0);
        assertEquals(ZoneOffset.ofHours(-2).get(OFFSET_SECONDS), -7200);
        assertEquals(ZoneOffset.ofHoursMinutesSeconds(0, 1, 5).get(OFFSET_SECONDS), 65);
    }

    @Test
    public void test_getLong_TemporalField() {
        assertEquals(ZoneOffset.UTC.getLong(OFFSET_SECONDS), 0);
        assertEquals(ZoneOffset.ofHours(-2).getLong(OFFSET_SECONDS), -7200);
        assertEquals(ZoneOffset.ofHoursMinutesSeconds(0, 1, 5).getLong(OFFSET_SECONDS), 65);
    }

    //-----------------------------------------------------------------------
    // query(TemporalQuery)
    //-----------------------------------------------------------------------
    @Test
    public void test_query() {
        assertEquals(ZoneOffset.UTC.query(TemporalQueries.chronology()), null);
        assertEquals(ZoneOffset.UTC.query(TemporalQueries.localDate()), null);
        assertEquals(ZoneOffset.UTC.query(TemporalQueries.localTime()), null);
        assertEquals(ZoneOffset.UTC.query(TemporalQueries.offset()), ZoneOffset.UTC);
        assertEquals(ZoneOffset.UTC.query(TemporalQueries.precision()), null);
        assertEquals(ZoneOffset.UTC.query(TemporalQueries.zone()), ZoneOffset.UTC);
        assertEquals(ZoneOffset.UTC.query(TemporalQueries.zoneId()), null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_query_null() {
        ZoneOffset.UTC.query(null);
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    @Test
    public void test_compareTo() {
        ZoneOffset offset1 = ZoneOffset.ofHoursMinutesSeconds(1, 2, 3);
        ZoneOffset offset2 = ZoneOffset.ofHoursMinutesSeconds(2, 3, 4);
        assertTrue(offset1.compareTo(offset2) > 0);
        assertTrue(offset2.compareTo(offset1) < 0);
        assertTrue(offset1.compareTo(offset1) == 0);
        assertTrue(offset2.compareTo(offset2) == 0);
    }

    //-----------------------------------------------------------------------
    // equals() / hashCode()
    //-----------------------------------------------------------------------
    @Test
    public void test_equals() {
        ZoneOffset offset1 = ZoneOffset.ofHoursMinutesSeconds(1, 2, 3);
        ZoneOffset offset2 = ZoneOffset.ofHoursMinutesSeconds(2, 3, 4);
        ZoneOffset offset2b = ZoneOffset.ofHoursMinutesSeconds(2, 3, 4);
        assertEquals(offset1.equals(offset2), false);
        assertEquals(offset2.equals(offset1), false);

        assertEquals(offset1.equals(offset1), true);
        assertEquals(offset2.equals(offset2), true);
        assertEquals(offset2.equals(offset2b), true);

        assertEquals(offset1.hashCode() == offset1.hashCode(), true);
        assertEquals(offset2.hashCode() == offset2.hashCode(), true);
        assertEquals(offset2.hashCode() == offset2b.hashCode(), true);
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test
    public void test_toString() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutesSeconds(1, 0, 0);
        assertEquals(offset.toString(), "+01:00");
        offset = ZoneOffset.ofHoursMinutesSeconds(1, 2, 3);
        assertEquals(offset.toString(), "+01:02:03");
        offset = ZoneOffset.UTC;
        assertEquals(offset.toString(), "Z");
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    private void doTestOffset(ZoneOffset offset, int hours, int minutes, int seconds) {
        assertEquals(offset.getTotalSeconds(), hours * 60 * 60 + minutes * 60 + seconds);
        final String id;
        if (hours == 0 && minutes == 0 && seconds == 0) {
            id = "Z";
        } else {
            String str = (hours < 0 || minutes < 0 || seconds < 0) ? "-" : "+";
            str += Integer.toString(Math.abs(hours) + 100).substring(1);
            str += ":";
            str += Integer.toString(Math.abs(minutes) + 100).substring(1);
            if (seconds != 0) {
                str += ":";
                str += Integer.toString(Math.abs(seconds) + 100).substring(1);
            }
            id = str;
        }
        assertEquals(offset.getId(), id);
        assertEquals(offset, ZoneOffset.ofHoursMinutesSeconds(hours, minutes, seconds));
        if (seconds == 0) {
            assertEquals(offset, ZoneOffset.ofHoursMinutes(hours, minutes));
            if (minutes == 0) {
                assertEquals(offset, ZoneOffset.ofHours(hours));
            }
        }
        assertEquals(ZoneOffset.of(id), offset);
        assertEquals(offset.toString(), id);
    }

}
