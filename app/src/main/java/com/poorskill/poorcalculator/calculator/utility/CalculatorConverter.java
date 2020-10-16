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
package com.poorskill.poorcalculator.calculator.utility;

import android.annotation.SuppressLint;

import com.poorskill.poorcalculator.calculator.characters.CalculatorCharacter;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Utility Class for converting CalculatorCharacters into String for display purpose
 */
public final class CalculatorConverter {
    /**
     * Converts BigDecimal into an easy to read String (eg. removes trailing zeros)
     *
     * @param d bigDecimal value
     * @return value formatted with thousand separators and removed trailing zeros
     */
    @SuppressLint("DefaultLocale")
    public static String formatResultToString(BigDecimal d) {
        if (d == null) {
            return "";
        }
        return removeTrailingZeros(String.format("%,f", d));
    }

    /**
     * Oh yikes
     * Really bad, inefficient and totally random function to remove trailing Zeros from String
     * DecimalFormat would work better :)
     *
     * @param s number as String
     * @return number param without trailing zeros
     */
    public static String removeTrailingZeros(String s) {
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        String stringDecimalSeparator = String.valueOf(decimalSymbols.getDecimalSeparator());
        StringBuilder sb = new StringBuilder(s);
        if (s.contains(stringDecimalSeparator)) {
            int decimalSeparatorIndex = s.indexOf(stringDecimalSeparator);
            for (int i = s.length() - 1; i >= decimalSeparatorIndex; --i) {
                if (s.charAt(i) != '0') {
                    sb = new StringBuilder(s.substring(0, i));
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Takes each character of the  param CalculatorCharacter list, appends the StringValue and returns the String
     *
     * @param ccs calculatorCharacters
     * @return ccs's value as String
     */
    public static String convertCalculatorCharsToString(List<CalculatorCharacter> ccs) {
        StringBuilder sb = new StringBuilder();
        ccs.forEach(c -> sb.append(c.toStringValue()));
        return sb.toString();
    }
}
