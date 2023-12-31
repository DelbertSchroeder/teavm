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
import org.teavm.model.FieldReference;
import org.teavm.model.MethodReference;
import org.teavm.runtime.RuntimeClass;

public class PlatformClassMetadataIntrinsic implements Intrinsic {
    private static final String PLATFORM_CLASS_METADATA = "org.teavm.platform.PlatformClassMetadata";
    private static final FieldReference ITEM_TYPE_FIELD = new FieldReference(
            RuntimeClass.class.getName(), "itemType");
    private static final FieldReference SUPERCLASS_FIELD = new FieldReference(
            RuntimeClass.class.getName(), "parent");
    private static final FieldReference NAME_FIELD = new FieldReference(
            RuntimeClass.class.getName(), "name");
    private static final FieldReference SIMPLE_NAME_FIELD = new FieldReference(
            RuntimeClass.class.getName(), "simpleName");
    private static final FieldReference DECLARING_CLASS_FIELD = new FieldReference(
            RuntimeClass.class.getName(), "declaringClass");
    private static final FieldReference ENCLOSING_CLASS_FIELD = new FieldReference(
            RuntimeClass.class.getName(), "enclosingClass");
    private static final FieldReference FLAGS_FIELD = new FieldReference(
            RuntimeClass.class.getName(), "flags");

    @Override
    public boolean canHandle(MethodReference method) {
        if (!method.getClassName().equals(PLATFORM_CLASS_METADATA)) {
            return false;
        }
        switch (method.getName()) {
            case "getArrayItem":
            case "getSuperclass":
            case "getName":
            case "getSimpleName":
            case "getDeclaringClass":
            case "getEnclosingClass":
            case "getFlags":
                return true;
        }
        return false;
    }

    @Override
    public void apply(IntrinsicContext context, InvocationExpr invocation) {
        switch (invocation.getMethod().getName()) {
            case "getArrayItem":
                printFieldAccess(context, invocation, ITEM_TYPE_FIELD);
                break;
            case "getSuperclass":
                printFieldAccess(context, invocation, SUPERCLASS_FIELD);
                break;
            case "getName":
                context.writer().print("(");
                printFieldAccess(context, invocation, NAME_FIELD);
                context.writer().print(" ? *");
                printFieldAccess(context, invocation, NAME_FIELD);
                context.writer().print(" : NULL)");
                break;
            case "getSimpleName":
                context.writer().print("(");
                printFieldAccess(context, invocation, SIMPLE_NAME_FIELD);
                context.writer().print(" ? *");
                printFieldAccess(context, invocation, SIMPLE_NAME_FIELD);
                context.writer().print(" : NULL)");
                break;
            case "getDeclaringClass":
                printFieldAccess(context, invocation, DECLARING_CLASS_FIELD);
                break;
            case "getEnclosingClass":
                printFieldAccess(context, invocation, ENCLOSING_CLASS_FIELD);
                break;
            case "getFlags":
                context.writer().print("(");
                printFieldAccess(context, invocation, FLAGS_FIELD);
                context.writer().print(" >> " + RuntimeClass.FLAGS_SHIFT + ")");
                break;
        }
    }

    private void printFieldAccess(IntrinsicContext context, InvocationExpr invocation, FieldReference field) {
        context.writer().print("TEAVM_FIELD(");
        context.emit(invocation.getArguments().get(0));
        context.writer().print(",");
        context.writer().print(context.names().forClass(field.getClassName())).print(", ");
        context.writer().print(context.names().forMemberField(field));
        context.writer().print(")");
    }
}
