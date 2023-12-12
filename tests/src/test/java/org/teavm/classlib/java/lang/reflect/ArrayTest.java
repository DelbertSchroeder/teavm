/*
 *  Copyright 2014 Alexey Andreev.
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
package org.teavm.classlib.java.lang.reflect;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Array;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.EachTestCompiledSeparately;
import org.teavm.junit.SkipPlatform;
import org.teavm.junit.TeaVMTestRunner;
import org.teavm.junit.TestPlatform;

@RunWith(TeaVMTestRunner.class)
@EachTestCompiledSeparately
public class ArrayTest {
    @Test
    public void createsNewInstance() {
        Object instance = Array.newInstance(Object.class, 10);
        assertEquals(Object[].class, instance.getClass());
        assertEquals(10, Array.getLength(instance));
    }

    @Test
    public void createsNewPrimitiveInstance() {
        Object instance = Array.newInstance(int.class, 15);
        assertEquals(int[].class, instance.getClass());
        assertEquals(15, Array.getLength(instance));
    }

    @Test
    @SkipPlatform({TestPlatform.C, TestPlatform.WEBASSEMBLY, TestPlatform.WASI})
    public void setWorks() {
        Object array = Array.newInstance(String.class, 2);
        Array.set(array, 0, "foo");
        Array.set(array, 1, "bar");
        assertArrayEquals(new String[] { "foo", "bar" }, (String[]) array);
    }

    @Test
    @SkipPlatform({TestPlatform.C, TestPlatform.WEBASSEMBLY, TestPlatform.WASI})
    public void setPrimitiveWorks() {
        Object array = Array.newInstance(int.class, 2);
        Array.set(array, 0, 23);
        Array.set(array, 1, 42);
        assertArrayEquals(new int[] { 23, 42 }, (int[]) array);
    }
}
