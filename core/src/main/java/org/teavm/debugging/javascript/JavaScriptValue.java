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
package org.teavm.debugging.javascript;

import java.util.Map;
import org.teavm.common.Promise;

public interface JavaScriptValue {
    String getSimpleRepresentation();

    Promise<String> getRepresentation();

    Promise<String> getClassName();

    Promise<Map<String, ? extends JavaScriptVariable>> getProperties();

    boolean hasInnerStructure();

    String getInstanceId();
}
