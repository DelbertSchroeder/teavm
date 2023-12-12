/*
 *  Copyright 2016 Alexey Andreev.
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
package org.teavm.backend.wasm.model.expression;

import java.util.Objects;

public class WasmIntUnary extends WasmExpression {
    private WasmIntType type;
    private WasmIntUnaryOperation operation;
    private WasmExpression operand;

    public WasmIntUnary(WasmIntType type, WasmIntUnaryOperation operation, WasmExpression operand) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(operation);
        Objects.requireNonNull(operand);
        this.type = type;
        this.operation = operation;
        this.operand = operand;
    }

    public WasmIntType getType() {
        return type;
    }

    public void setType(WasmIntType type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    public WasmIntUnaryOperation getOperation() {
        return operation;
    }

    public void setOperation(WasmIntUnaryOperation operation) {
        Objects.requireNonNull(operation);
        this.operation = operation;
    }

    public WasmExpression getOperand() {
        return operand;
    }

    public void setOperand(WasmExpression operand) {
        Objects.requireNonNull(operand);
        this.operand = operand;
    }

    @Override
    public void acceptVisitor(WasmExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
