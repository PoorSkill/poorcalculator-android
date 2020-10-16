/*
 * The MIT License
 * Copyright © 2020 Anton Kesy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.poorskill.poorcalculator.calculator.characters;

import com.poorskill.poorcalculator.calculator.characters.supported.SupportedOperations;

import java.io.Serializable;

/**
 * CalculatorOperation inheritance CalculatorCharacter and implements Serializable for saving purpose
 * <p>
 * Represents the supported operations used in the calculator
 */
public class CalculatorOperation extends CalculatorCharacter implements Serializable {
    private static final long serialVersionUID = 6529687098967057690L;
    private SupportedOperations operation;

    /**
     * Constructor for setting Operation up
     *
     * @param operation supported operation
     */
    public CalculatorOperation(SupportedOperations operation) {
        super();
        this.operation = operation;
    }

    /**
     * Getter of operation
     *
     * @return supported operation of CalculatorOperation Object
     */
    public SupportedOperations getOperation() {
        return this.operation;
    }

    /**
     * Returns the value of the operations as String for display purpose
     * (Hardcoded unicode symbols for easiness)
     *
     * @return operation as String
     */
    @Override
    public String toStringValue() {
        switch (operation) {
            case Add:
                return "+";
            case Sub:
                return "−";
            case Multiply:
                return "×";
            case Divide:
                return "÷";
            case Faculty:
                return "!";
        }
        return operation.toString();
    }
}
