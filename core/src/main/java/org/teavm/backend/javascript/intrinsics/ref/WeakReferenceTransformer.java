/*
 *  Copyright 2023 konsoletyper.
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
package org.teavm.backend.javascript.intrinsics.ref;

import java.lang.ref.WeakReference;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.ElementModifier;

public class WeakReferenceTransformer implements ClassHolderTransformer {
    @Override
    public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
        if (!cls.getName().equals(WeakReference.class.getName())) {
            return;
        }
        for (var method : cls.getMethods()) {
            switch (method.getName()) {
                case "<init>":
                    if (method.parameterCount() == 2) {
                        method.setProgram(null);
                        method.getModifiers().add(ElementModifier.NATIVE);
                    }
                    break;
                case "get":
                case "clear":
                    method.setProgram(null);
                    method.getModifiers().add(ElementModifier.NATIVE);
                    break;
            }
        }
    }
}
