/*
 *  Copyright 2022 Alexey Andreev.
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
package org.teavm.idea.debug;

import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;
import org.jetbrains.annotations.Nullable;
import org.teavm.debugging.Debugger;

class TeaVMSuspendContext extends XSuspendContext {
    private XExecutionStack executionStack;

    TeaVMSuspendContext(Debugger innerDebugger, Project project) {
        executionStack = new TeaVMExecutionStack(innerDebugger, project);
    }

    @Nullable
    @Override
    public XExecutionStack getActiveExecutionStack() {
        return executionStack;
    }
}
