/*
 *  Copyright 2018 Alexey Andreev.
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
package org.teavm.backend.c.intrinsic;

import org.teavm.ast.InvocationExpr;
import org.teavm.model.MethodReference;

public class IntegerIntrinsic implements Intrinsic {
    @Override
    public boolean canHandle(MethodReference method) {
        if (!method.getClassName().equals(Integer.class.getName())) {
            return false;
        }
        switch (method.getName()) {
            case "divideUnsigned":
            case "remainderUnsigned":
            case "compareUnsigned":
                return true;
            default:
                return false;
        }
    }

    @Override
    public void apply(IntrinsicContext context, InvocationExpr invocation) {
        switch (invocation.getMethod().getName()) {
            case "divideUnsigned":
                writeBinary(context, invocation, "/");
                break;
            case "remainderUnsigned":
                writeBinary(context, invocation, "%");
                break;
            case "compareUnsigned":
                context.writer().print("teavm_compare_u32(");
                context.emit(invocation.getArguments().get(0));
                context.writer().print(", ");
                context.emit(invocation.getArguments().get(1));
                context.writer().print(")");
                break;
        }
    }

    private void writeBinary(IntrinsicContext context, InvocationExpr invocation, String operation) {
        context.writer().print("((int32_t) ((uint32_t) ");
        context.emit(invocation.getArguments().get(0));
        context.writer().print(" " + operation + " (uint32_t) ");
        context.emit(invocation.getArguments().get(1));
        context.writer().print("))");
    }
}
