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
package com.poorskill.poorcalculator.calculator;

import com.poorskill.poorcalculator.calculator.characters.CalculatorCharacter;
import com.poorskill.poorcalculator.calculator.characters.CalculatorNumber;
import com.poorskill.poorcalculator.calculator.characters.CalculatorOperation;
import com.poorskill.poorcalculator.calculator.characters.CalculatorSeparator;
import com.poorskill.poorcalculator.calculator.characters.CalculatorValues;
import com.poorskill.poorcalculator.calculator.characters.supported.CalculatorSeparatorEnum;
import com.poorskill.poorcalculator.calculator.characters.supported.SupportedOperations;
import com.poorskill.poorcalculator.calculator.exceptions.BadExpressionException;
import com.poorskill.poorcalculator.calculator.exceptions.CalculatorException;
import com.poorskill.poorcalculator.calculator.exceptions.DividedByZeroException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * UtilityClass Calculator which is the heart of the application as tool
 */
public final class Calculator {
    //BigDecimal Division Scale set to fixed value since this is only a project for learning and not an scientific program
    private static final int DIVIDE_SCALE = 100;

    /**
     * Calculates single operation of the given params and returns result as BigDecimal
     *
     * @param operation supported operations (not faculty)
     * @param x         first value in BigDecimal
     * @param y         second value in BigDecimal
     * @return result of x operation y as BigDecimal
     * @throws DividedByZeroException if second value is 0 for multiplying
     */
    private static BigDecimal calculateSingleOperation(SupportedOperations operation, BigDecimal x, BigDecimal y) throws DividedByZeroException, BadExpressionException {
        if (x == null || y == null) {
            throw new BadExpressionException();
        }
        switch (operation) {
            case Add:
                return x.add(y);
            case Sub:
                return x.subtract(y);
            case Multiply:
                return x.multiply(y);
            case Divide:
                if (y.equals(BigDecimal.ZERO)) {
                    throw new DividedByZeroException();
                }
                return x.divide(y, DIVIDE_SCALE, RoundingMode.HALF_UP);
        }
        return x;
    }

    /**
     * Calculates recursive the given List of calculator characters and returns the result as BigDecimal
     *
     * @param ccs Formula as List of CalculatorCharacters
     * @return evaluation of ccs' formula
     * @throws CalculatorException called functions can call CalculatorExceptions -> passed on to function call to process exceptions
     */
    public static BigDecimal calculateCCS(List<CalculatorCharacter> ccs) throws CalculatorException {
        SupportedOperations lastOp = null;
        BigDecimal xBD = null, yBD;
        //Checks if CalculatorCharacterList contains any faculty and priorities calculates it into an number value
        if (ccs.stream().filter(cc -> cc instanceof CalculatorOperation).anyMatch(cc -> ((CalculatorOperation) cc).getOperation() == SupportedOperations.Faculty)) {
            calculateFaculty(ccs);
        }
        //Checks if CalculatorCharacterList contains any instance of CalculatorSeparator
        // if true splits down to separator
        if (ccs.stream().anyMatch(cc -> cc instanceof CalculatorSeparator)) {
            filterOutSeparatorOperations(ccs);
        }
        //Checks if CalculatorCharacterList contains any instance of prioritised operations (multiply and divide)
        if (ccs.stream().filter(cc -> cc instanceof CalculatorOperation).anyMatch(cc -> ((CalculatorOperation) cc).getOperation() == SupportedOperations.Multiply || ((CalculatorOperation) cc).getOperation() == SupportedOperations.Divide)) {
            filterOutPrioritisedOperations(ccs);
        }
        return calculateSubValue(ccs);
    }


    /**
     * Calculates faculty of input BigDecimal with while loop
     *
     * @param input faculty of as BigDecimal
     * @return the faculty value
     */
    private static BigDecimal getFaculty(BigDecimal input) {
        BigDecimal result = input, restValue = input;
        if (input.equals(BigDecimal.ZERO)) {
            return BigDecimal.ONE;
        }
        while (restValue.longValue() > 1) {
            result = result.multiply(restValue = restValue.subtract(BigDecimal.ONE));
        }
        return result;
    }

    /**
     * Strips down the giving calculatorCharacterList to fill in the getFaculty() function and returns the list with the faculty exchanged for the actual number value in BigDecimal
     *
     * @param ccs calculatorCharacterList which contains operation faculty
     * @return calculatorCharacterList with the operation faculty replaced with the number value in BigDecimal
     */
    private static List<CalculatorCharacter> calculateFaculty(List<CalculatorCharacter> ccs) {
        for (int i = 1; i < ccs.size(); ++i) {
            if (ccs.get(i) instanceof CalculatorOperation) {
                if (((CalculatorOperation) ccs.get(i)).getOperation() == SupportedOperations.Faculty) {
                    if (ccs.get(i - 1) instanceof CalculatorNumber) {
                        ccs.remove(i);
                        ccs.add(i, new CalculatorNumber(getFaculty(((CalculatorNumber) ccs.get(i - 1)).getValue())));
                        ccs.remove(i - 1);
                        break;
                    }
                }
            }
        }
        return ccs;
    }


    /**
     * Filters the prioritised operations (multiply, division,(without faculty, since this operations is already handled)) and calls recursive calculation of sublist of calculatorCharacterList
     *
     * @param ccs calculatorCharacterList which contains prioritised operations
     * @return calculatorCharacterList with the prioritised operations replaced with number values
     * @throws BadExpressionException if there are operation characters miss-placed or not logical
     * @throws DividedByZeroException if calculateValue(->calculateSingleOperation()) detects division by zero
     */
    private static List<CalculatorCharacter> filterOutPrioritisedOperations(List<CalculatorCharacter> ccs) throws BadExpressionException, DividedByZeroException {
        int indexLeft = -1, indexRight = -1;
        for (int i = 1; i < ccs.size(); ++i) {
            if (ccs.get(i) instanceof CalculatorOperation) {
                if (((CalculatorOperation) ccs.get(i)).getOperation() == SupportedOperations.Multiply || ((CalculatorOperation) ccs.get(i)).getOperation() == SupportedOperations.Divide) {
                    indexLeft = i - 1;
                    indexRight = i + 2;
                    if (ccs.size() > i + 2 && ccs.get(i + 1) instanceof CalculatorOperation) {
                        if (((CalculatorOperation) ccs.get(i + 1)).getOperation() == SupportedOperations.Sub) {
                            indexRight++;
                        } else if (((CalculatorOperation) ccs.get(i + 1)).getOperation() == SupportedOperations.Divide || ((CalculatorOperation) ccs.get(i + 1)).getOperation() == SupportedOperations.Multiply) {
                            throw new BadExpressionException();
                        }
                    }
                }
            }
        }
        if (indexRight > ccs.size()) {
            throw new BadExpressionException();
        }
        if (indexLeft > -1 && indexRight > -1) {
            ArrayList<CalculatorCharacter> ccsPartList = new ArrayList<>(ccs.subList(indexLeft, indexRight));
            ccs.remove(indexLeft);
            ccs.add(indexLeft, new CalculatorNumber(calculateSubValue(ccsPartList)));
            ccs.subList(indexLeft + 1, indexRight).clear();
            return filterOutPrioritisedOperations(ccs);
        }

        return ccs;
    }

    /**
     * reads out given calculatorCharacterList to fill calculateSingleOperation(), calculates every single operation and number and returns result as BigDecimal
     *
     * @param ccs calculatorCharacterList with operations
     * @return calculatorCharacterList evaluations as BigDecimal
     * @throws DividedByZeroException if calculateSingleOperation()detects division by zero
     * @throws BadExpressionException if there are operation characters miss-placed or not logical
     */
    private static BigDecimal calculateSubValue(List<CalculatorCharacter> ccs) throws DividedByZeroException, BadExpressionException {
        SupportedOperations lastOp = null;
        BigDecimal xBD = null, yBD;
        for (int i = 0; i < ccs.size(); ++i) {
            if (ccs.get(i) instanceof CalculatorValues) {
                if (xBD == null) {
                    xBD = ((CalculatorValues) ccs.get(i)).getValue();
                    if (lastOp == SupportedOperations.Sub) {
                        xBD = xBD.negate();
                        lastOp = null;
                    } else if (lastOp != null) {
                        throw new BadExpressionException();
                    }
                } else {
                    yBD = ((CalculatorValues) ccs.get(i)).getValue();
                    if (lastOp != null) {
                        xBD = calculateSingleOperation(lastOp, xBD, yBD);
                        lastOp = null;
                    }
                }
            } else if (lastOp == null && ccs.get(i) instanceof CalculatorOperation) {
                lastOp = ((CalculatorOperation) ccs.get(i)).getOperation();
            }
        }
        return xBD;
    }

    /**
     * Filters out sub-lists of given calculatorCharacterList and calculates recursive
     * Returns the list if no more separators are found
     *
     * @param ccs
     * @return calculatorCharacterList with separator-sub-lists replaced with BigDecimal number values
     * @throws CalculatorException if calculateCCS() recursive call gives back exception
     */
    private static List<CalculatorCharacter> filterOutSeparatorOperations(List<CalculatorCharacter> ccs) throws CalculatorException {
        int indexLeft = -1, indexRight = -1;
        for (int i = 0; i < ccs.size(); ++i) {
            if (ccs.get(i) instanceof CalculatorSeparator) {
                //Updates with latest left Bracket
                if (((CalculatorSeparator) ccs.get(i)).getSeparator() == CalculatorSeparatorEnum.LeftBracket) {
                    indexLeft = i;
                } else if (((CalculatorSeparator) ccs.get(i)).getSeparator() == CalculatorSeparatorEnum.RightBracket) {
                    indexRight = i;
                    break;
                }
            }
        }
        if (indexLeft > -1 && indexRight > -1) {
            ArrayList<CalculatorCharacter> ccsPartList = new ArrayList<>(ccs.subList(indexLeft + 1, indexRight));
            ccs.remove(indexLeft);
            ccs.add(indexLeft, new CalculatorNumber(calculateCCS(ccsPartList)));
            ccs.subList(indexLeft + 1, indexRight + 1).clear();
            return filterOutSeparatorOperations(ccs);
        }
        return ccs;
    }

    /**
     * Returns true if BigDecimal is integer, false if float
     *
     * @param bd BigDecimal to check
     * @return if BigDecimal is integer true
     */
    public static boolean isIntegerValue(BigDecimal bd) {
        return bd.stripTrailingZeros().scale() <= 0;
    }


}
