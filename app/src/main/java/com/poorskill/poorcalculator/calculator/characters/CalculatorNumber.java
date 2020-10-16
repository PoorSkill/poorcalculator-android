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

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.poorskill.poorcalculator.calculator.Calculator.isIntegerValue;
import static com.poorskill.poorcalculator.calculator.utility.CalculatorConverter.removeTrailingZeros;


/**
 * CalculatorNumber inheritance CalculatorCharacter and implements CalculatorValues for getting the Value as BigDecimal, Serializable for saving purpose
 * <p>
 * Represents the numbers used in the calculator
 */
public class CalculatorNumber extends CalculatorCharacter implements CalculatorValues, Serializable {
    private static final long serialVersionUID = 6529386098967257690L;
    private BigDecimal numberValue;

    /**
     * Constructor for setting up number
     *
     * @param value value of number in BigDecimal
     */
    public CalculatorNumber(BigDecimal value) {
        super();
        this.numberValue = value;
    }

    /**
     * Returns the value of the number as String
     * Checks if the number isn't null, checks if integer or not and then uses String.format to format into String
     * usage of removeTrailingZeros() is not beautiful or efficient but to lazy to implement something different :)
     * <p>
     * Ignores Locale since the calculator changes to the locale
     *
     * @return value of number in String without trailingZeros (2.00 - > 2) (bad for user input since it also removes the zeros while typing decimal
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String toStringValue() {
        if (numberValue == null) {
            return "";
        }
        return isIntegerValue(numberValue) ? String.format("%,d", numberValue.longValue()) : removeTrailingZeros(String.format("%,f", numberValue));
    }


    /**
     * Getter of number value in BigDecimal
     *
     * @return value of number in BigDecimal
     */
    @Override
    public BigDecimal getValue() {
        return this.numberValue;
    }
}
