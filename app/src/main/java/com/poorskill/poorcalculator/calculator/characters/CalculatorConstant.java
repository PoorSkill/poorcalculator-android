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

import com.poorskill.poorcalculator.calculator.characters.supported.SupportedConstant;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CalculatorConstant inheritance CalculatorCharacter and implements CalculatorValues for getting the Value as BigDecimal, Serializable for saving purpose
 * <p>
 * Represents the supported constants used in the calculator
 */
public class CalculatorConstant extends CalculatorCharacter implements CalculatorValues, Serializable {
    private SupportedConstant constant;

    /**
     * Constructor for setting Constant up
     *
     * @param constant supported constant
     */
    public CalculatorConstant(SupportedConstant constant) {
        super();
        this.constant = constant;
    }

    /**
     * Getter of Constant
     *
     * @return supported constant of the CalculatorConstant object
     */
    public SupportedConstant getConstant() {
        return this.constant;
    }

    /**
     * Returns the value of the constant as String for display purpose
     * (Hardcoded unicode symbols for easiness)
     *
     * @return constant as String
     */
    @Override
    public String toStringValue() {
        switch (constant) {
            case Pi:
                return "π";
            case E:
                return "e";
        }
        return constant.toString();
    }

    /**
     * Returns the constant value as BigDecimal Value
     * Since the more exact values than the Math.Constant values are unnecessary for this kind of learning project and non value-perfection calculators ...
     *
     * @return constant values in BigDecimal
     */
    @Override
    public BigDecimal getValue() {
        switch (constant) {
            case Pi:
                return BigDecimal.valueOf(Math.PI);
            case E:
                return BigDecimal.valueOf(Math.E);
        }
        return null;
    }
}
