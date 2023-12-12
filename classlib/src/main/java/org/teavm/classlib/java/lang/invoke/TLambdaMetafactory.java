/*
 *  Copyright 2017 Alexey Andreev.
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
package org.teavm.classlib.java.lang.invoke;

public class TLambdaMetafactory {
    public static final int FLAG_SERIALIZABLE = 1;
    public static final int FLAG_MARKERS = 2;
    public static final int FLAG_BRIDGES = 4;

    private TLambdaMetafactory() {
    }

    public static native TCallSite metafactory(TMethodHandles.Lookup caller, String invokedName,
            TMethodType invokedType, TMethodType samMethodType, TMethodHandle implMethod,
            TMethodType instantiatedMethodType) throws TLambdaConversionException;

    public static native TCallSite altMetafactory(TMethodHandles.Lookup caller, String invokedName,
            TMethodType invokedType, Object... args) throws TLambdaConversionException;
}
