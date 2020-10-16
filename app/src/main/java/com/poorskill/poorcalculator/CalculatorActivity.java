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
package com.poorskill.poorcalculator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.poorskill.poorcalculator.calculator.characters.CalculatorCharacter;
import com.poorskill.poorcalculator.calculator.characters.CalculatorConstant;
import com.poorskill.poorcalculator.calculator.characters.CalculatorNumber;
import com.poorskill.poorcalculator.calculator.characters.CalculatorOperation;
import com.poorskill.poorcalculator.calculator.characters.CalculatorSeparator;
import com.poorskill.poorcalculator.calculator.characters.supported.CalculatorSeparatorEnum;
import com.poorskill.poorcalculator.calculator.characters.supported.SupportedConstant;
import com.poorskill.poorcalculator.calculator.characters.supported.SupportedOperations;
import com.poorskill.poorcalculator.calculator.exceptions.CalculatorException;
import com.poorskill.poorcalculator.calculator.exceptions.DividedByZeroException;
import com.poorskill.poorcalculator.calculator.exceptions.MissingFormulaException;
import com.poorskill.poorcalculator.calculator.history.History;
import com.poorskill.poorcalculator.calculator.history.HistoryCalculation;
import com.poorskill.poorcalculator.design.ThemeManager;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import static com.poorskill.poorcalculator.calculator.Calculator.calculateCCS;
import static com.poorskill.poorcalculator.calculator.utility.CalculatorConverter.convertCalculatorCharsToString;
import static com.poorskill.poorcalculator.calculator.utility.CalculatorConverter.formatResultToString;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    //History Class Object
    private History history;

    //CalculatorCharacters Lists
    private ArrayList<CalculatorCharacter> ccs = new ArrayList<>();
    private ArrayList<CalculatorCharacter> lastCalculation = new ArrayList<>();
    //Cached Values
    private BigDecimal lastResult;
    private String stringDecimalSeparator;
    //Input Digits
    private StringBuilder digits = new StringBuilder();
    //Calculator Input Flags
    private boolean isCalculated;
    private boolean isDecimal;
    private boolean isBuildingNumber;
    private boolean hasPlaceholder;
    private boolean lastIsBracketClosed;
    private boolean lastIsConstant;
    private boolean lastIsFaculty;
    //Calculator counter for flag
    private int openBracketCount;
    //Cached Layout Objects for continues manipulation
    private TextView resultDisplay;
    private TextView formulaDisplay;


    /**
     * All necessary function calls and caching before using the application
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caclculator_activity);
        //History Object for loading/saving calculations
        this.history = new History(getApplicationContext());
        //Setup OnClickListener
        layoutSetOnClickListener();
        //Theme Manager for loading and changing themes of the application
        ThemeManager.setTheme(findViewById(android.R.id.content).getRootView(), getApplicationContext());
        //Set Locale Decimal on Separator Button and cache value
        setLocaleDecimalSeparator();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * Buttons OnClick calls connected function
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_separator:
                separateNumber();
                break;
            case R.id.btn_bracket_left:
                bracketLeft();
                break;
            case R.id.btn_bracket_right:
                bracketRight();
                break;
            case R.id.btn_9:
                buildNumber(9);
                break;
            case R.id.btn_8:
                buildNumber(8);
                break;
            case R.id.btn_7:
                buildNumber(7);
                break;
            case R.id.btn_6:
                buildNumber(6);
                break;
            case R.id.btn_5:
                buildNumber(5);
                break;
            case R.id.btn_4:
                buildNumber(4);
                break;
            case R.id.btn_3:
                buildNumber(3);
                break;
            case R.id.btn_2:
                buildNumber(2);
                break;
            case R.id.btn_1:
                buildNumber(1);
                break;
            case R.id.btn_0:
                buildNumber(0);
                break;
            case R.id.btn_divide:
                operationButton(SupportedOperations.Divide);
                break;
            case R.id.btn_multiply:
                operationButton(SupportedOperations.Multiply);
                break;
            case R.id.btn_plus:
                operationButton(SupportedOperations.Add);
                break;
            case R.id.btn_subtract:
                operationButton(SupportedOperations.Sub);
                break;
            case R.id.btn_e:
                addConstant(SupportedConstant.E);
                break;
            case R.id.btn_pi:
                addConstant(SupportedConstant.Pi);
                break;
            case R.id.btn_clear:
                removeLastElement();
                break;
            case R.id.btn_equals:
                equalsButton();
                break;
        }
        updateFormula();
    }

    /**
     * Buttons OnLongClick calls connected function
     *
     * @param view
     */
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.result:
                longClickDisplay(resultDisplay);
                break;
            case R.id.formula:
                longClickDisplay(formulaDisplay);
                break;
            case R.id.btn_clear:
                clearAll();
                break;
            case R.id.btn_pi:
                LoadLastCalculation();
                break;
            case R.id.btn_e:
                lastIsConstant = false;
                facultyButton();
                break;
            case R.id.btn_equals:
                showInfo();
        }
        return true;
    }

    /**
     * just a small toast text for showing information about me :)
     */
    private void showInfo() {
        Toast.makeText(this, getResources().getString(R.string.info), Toast.LENGTH_SHORT).show();
    }

    /**
     * Clears both displays, calculatorCharacterList, digitList and calls to resetFlags()
     */
    private void clearAll() {
        resetFlags();
        formulaDisplay.setText("");
        resultDisplay.setText("");
        digits.setLength(0);
        ccs.clear();
    }

    /**
     * Processes onLongClick on display and changes application theme if display which got long clicked is empty else loads clipboard with display String
     *
     * @param resultDisplay TextView which got clicked on
     */
    private void longClickDisplay(TextView resultDisplay) {
        if (resultDisplay.getText().toString().isEmpty()) {
            ThemeManager.ChangeTheme(getApplicationContext(), findViewById(android.R.id.content).getRootView());
        } else {
            copyResultToClipboard(resultDisplay.getText().toString());
        }
    }

    /**
     * Prepares calculatorCharacters for calculation (appends missing RightBrackets (just for looks)), endBuildNumber, sets flag and calls to calculate ccs
     */
    private void equalsButton() {
        //checkOpenBracket
        for (int i = 0; i < openBracketCount; ++i) {
            ccs.add(new CalculatorSeparator(CalculatorSeparatorEnum.RightBracket));
        }
        openBracketCount = 0;
        endBuildNumber();
        isCalculated = true;
        calculateInput();
    }

    /**
     * Operation Button processes operation which got clicked on and calls checkForLastCharIsOperation() for save adding operation to ccs
     *
     * @param operation operation which got clicked on
     */
    private void operationButton(SupportedOperations operation) {
        endBuildNumber();
        lastIsConstant = false;
        checkForLastCharIsOperation(operation);
    }

    /**
     * Adds right bracket and sets all necessary flags and values
     */
    private void bracketRight() {
        endBuildNumber();
        openBracketCount--;
        ccs.add(new CalculatorSeparator(CalculatorSeparatorEnum.RightBracket));
        lastIsBracketClosed = true;
    }

    /**
     * Adds left bracket and sets all necessary flags, values and checks if logic need multiply between bracket and last calculatorCharacter
     */
    private void bracketLeft() {
        endBuildNumber();
        openBracketCount++;
        if (ccs.size() > 0 && !(ccs.get(ccs.size() - 1) instanceof CalculatorOperation) && !(ccs.get(ccs.size() - 1) instanceof CalculatorSeparator)) {
            ccs.add(new CalculatorOperation(SupportedOperations.Multiply));
        }
        ccs.add(new CalculatorSeparator(CalculatorSeparatorEnum.LeftBracket));
    }


    /**
     * Checks if faculty is legal operation and appends it to the ccs
     */
    private void facultyButton() {
        if (!lastIsFaculty && isBuildingNumber) {
            endBuildNumber();
            lastIsFaculty = true;
            ccs.add(new CalculatorOperation(SupportedOperations.Faculty));
            //Is in onLongClick -> not called after click on button
            updateFormula();
        }
    }

    /**
     * Loads last calculations with help of history
     * If last calculation is still on display, load before last calculation
     */
    private void LoadLastCalculation() {
        List<HistoryCalculation> oldCCS = history.read();
        if (oldCCS.size() > 0 && !ccs.isEmpty()) {
            ccs.clear();
            ccs.addAll(history.read().get(history.read().size() - 2).getCalculatorCharacters());
        } else if (oldCCS.size() > 1) {
            ccs.addAll(history.read().get(history.read().size() - 1).getCalculatorCharacters());
        } else {
            return;
        }
        updateFormula();
        calculateInput();
    }

    /**
     * Copies param value into clipboard with toast message
     *
     * @param value string which should be copied
     */
    private void copyResultToClipboard(String value) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", value);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getResources().getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
    }

    /**
     * Appends constant and logically needed operators to calculatorCharacters and sets flag
     *
     * @param constant
     */
    private void addConstant(SupportedConstant constant) {
        endBuildNumber();
        if (lastIsConstant) {
            ccs.add(new CalculatorOperation(SupportedOperations.Multiply));
        } else if (ccs.size() > 0 && !(ccs.get(ccs.size() - 1) instanceof CalculatorOperation)) {
            ccs.add(new CalculatorOperation(SupportedOperations.Multiply));
        }
        lastIsConstant = true;
        ccs.add(new CalculatorConstant(constant));
    }

    /**
     * Checks if the last char in the calculatorCharactersList is a operation and then adds necessary logically operators between
     *
     * @param operation
     */
    private void checkForLastCharIsOperation(SupportedOperations operation) {
        //Checks if last character is CalculatorOperation
        if (ccs.size() > 0 && ccs.get(ccs.size() - 1) instanceof CalculatorOperation) {
            SupportedOperations lastOperation = ((CalculatorOperation) ccs.remove(ccs.size() - 1)).getOperation();
            //switch between last operations to decide if replace last operation, add needed logically operation or just add the new operation
            switch (lastOperation) {
                case Add:
                case Sub:
                    ccs.add(new CalculatorOperation(operation));
                    break;
                case Multiply:
                case Divide:
                    if (operation == SupportedOperations.Sub) {
                        ccs.add(new CalculatorOperation(lastOperation));
                        ccs.add(new CalculatorSeparator(CalculatorSeparatorEnum.LeftBracket));
                        openBracketCount++;
                    }
                    ccs.add(new CalculatorOperation(operation));
                    break;
                case Faculty:
                    ccs.add(new CalculatorOperation(lastOperation));
                    ccs.add(new CalculatorOperation(operation));
                    lastIsFaculty = false;
                    break;
            }
        } else {
            ccs.add(new CalculatorOperation(operation));
        }
    }

    /**
     *
     */
    private void separateNumber() {
        //If is already decimal -> cancel separation
        if (isDecimal | digits.toString().contains(stringDecimalSeparator)) {
            return;
        } else {
            isDecimal = true;
        }
        //remove last calculatorCharacter (number before decimal conversion)
        if (ccs.size() > 0) {
            ccs.remove(ccs.size() - 1);
        }
        //if there is no number currently being build then start with zero for decimal under one
        if (!isBuildingNumber) {
            isBuildingNumber = true;
            digits.append("0");
        }
        digits.append('.');
        hasPlaceholder = true;
        ccs.add(new CalculatorNumber(new BigDecimal(digits.toString()).stripTrailingZeros()));
        ccs.add(new CalculatorSeparator(CalculatorSeparatorEnum.DecimalSeparator));
    }

    /**
     * Removes last element of calculatorCharacterList and sets flags, removes chars accordingly
     */
    private void removeLastElement() {
        resetFlags();
        //Check if ccs is empty
        if (!ccs.isEmpty()) {
            if (hasPlaceholder) {
                hasPlaceholder = false;
                ccs.remove(ccs.size() - 1);
                return;
            }
            //CalculatorNumber - deletes and rebuilds digits
            if (ccs.get(ccs.size() - 1) instanceof CalculatorNumber) {
                digits = new StringBuilder(((CalculatorNumber) ccs.remove(ccs.size() - 1)).getValue().stripTrailingZeros().toString());
                if (digits.length() > 1) {
                    //Check if number was decimal
                    if (digits.deleteCharAt(digits.length() - 1).toString().charAt(digits.length() - 1) == '.') {
                        hasPlaceholder = true;
                    }
                    ccs.add(new CalculatorNumber(new BigDecimal(digits.toString()).stripTrailingZeros()));
                    if (hasPlaceholder) {
                        ccs.add(new CalculatorSeparator(CalculatorSeparatorEnum.DecimalSeparator));
                    }
                } else {
                    digits.setLength(0);
                }
            } else {
                //Brackets - adds or subs of openBracketCounter
                if (ccs.get(ccs.size() - 1) instanceof CalculatorSeparator) {
                    if (((CalculatorSeparator) (ccs.get(ccs.size() - 1))).getSeparator() == CalculatorSeparatorEnum.LeftBracket) {
                        openBracketCount--;
                    } else if (((CalculatorSeparator) (ccs.get(ccs.size() - 1))).getSeparator() == CalculatorSeparatorEnum.RightBracket) {
                        openBracketCount++;
                    }
                }
                ccs.remove(ccs.size() - 1);
            }
        }
    }

    /**
     * Resets all lastCalculatorCharacter flags
     */
    private void resetFlags() {
        lastIsConstant = false;
        lastIsBracketClosed = false;
        lastIsFaculty = false;
        isCalculated = false;
        hasPlaceholder = false;
        isDecimal = false;
        resultDisplay.setText("");
    }

    /**
     * ends the building of the current digit into number, resets the flags and the digits StringBuilder
     */
    private void endBuildNumber() {
        //If ccs is already calculated -> append operation to last ccs and let user reedit ccs
        if (isCalculated) {
            isCalculated = false;
            ccs.clear();
            lastCalculation.clear();
            ccs.add(new CalculatorNumber(lastResult));
        }
        isDecimal = false;
        isBuildingNumber = false;
        digits.setLength(0);
    }

    /**
     * Builds the number out of the digits StringBuilder. Checks flags before and acts accordingly
     *
     * @param number int to append (0-9)
     */
    private void buildNumber(int number) {
        digits.append(number);
        if (lastIsConstant) {
            ccs.add(new CalculatorOperation(SupportedOperations.Multiply));
            lastIsConstant = false;
        }
        if (lastIsFaculty) {
            ccs.add(new CalculatorOperation(SupportedOperations.Multiply));
            lastIsFaculty = false;
        }
        if (lastIsBracketClosed) {
            lastIsBracketClosed = false;
            ccs.add(new CalculatorOperation(SupportedOperations.Multiply));
        }
        if (isCalculated) {
            isCalculated = false;
            ccs.clear();
            resultDisplay.setText("");
        }
        if (hasPlaceholder && ccs.size() > 0) {
            hasPlaceholder = false;
            ccs.remove(ccs.size() - 1);
        }
        if (isBuildingNumber && ccs.size() > 0) {
            ccs.remove(ccs.size() - 1);
        } else {
            isBuildingNumber = true;
        }
        if (digits.length() > 0) {
            ccs.add(new CalculatorNumber(new BigDecimal(digits.toString()).stripTrailingZeros()));
        }
    }


    /**
     * Updates the formula display with the calculatorCharacters in String value format
     */
    private void updateFormula() {
        this.formulaDisplay.setText(convertCalculatorCharsToString(ccs));
    }

    /**
     * tries to calculate the current calculatorCharacterList, handles all exceptions and loads the displays with the result
     */
    private void calculateInput() {
        lastCalculation.clear();
        lastCalculation.addAll(ccs);
        StringBuilder resultString = new StringBuilder();
        try {
            lastResult = calculateCCS(lastCalculation);
            if (lastResult == null) {
                throw new MissingFormulaException("");
            }
            resultString.append(formatResultToString(lastResult));
            history.addToHistory(new HistoryCalculation(ccs, lastResult));
        } catch (DividedByZeroException e) {
            resultString.append(getResources().getString(R.string.exception_divide_by_zero));
        } catch (MissingFormulaException e) {
            resultString.append(getResources().getString(R.string.exception_missing_formula));
        } catch (CalculatorException e) {
            resultString.append(getResources().getString(R.string.exception_bad_expression));
        }
        CheckResultEasterEgg(resultString.toString());
        this.resultDisplay.setText(resultString.toString());
    }

    /**
     * :3
     *
     * @param s
     */
    private void CheckResultEasterEgg(String s) {
        if (s.equals("69")) {
            EasterEggToast(getResources().getString(R.string.nice));
        } else if (s.equals("42")) {
            EasterEggToast(getResources().getString(R.string.ee42));
        }
    }

    /**
     * Even lazy for EasterEggs ...
     *
     * @param s
     */
    private void EasterEggToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets the locale decimal separator into the button loads it into the cache for later use
     */
    private void setLocaleDecimalSeparator() {
        Button decimalSeparatorButton = (Button) findViewById(R.id.btn_separator);
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        stringDecimalSeparator = String.valueOf(decimalSymbols.getDecimalSeparator());
        decimalSeparatorButton.setText(stringDecimalSeparator);
    }

    /**
     * Programmatically adding of clickListeners
     * Yikes
     * too much ...
     */
    private void layoutSetOnClickListener() {
        this.findViewById(R.id.result).setOnClickListener(this);
        this.findViewById(R.id.formula).setOnClickListener(this);
        this.findViewById(R.id.btn_9).setOnClickListener(this);
        this.findViewById(R.id.btn_8).setOnClickListener(this);
        this.findViewById(R.id.btn_7).setOnClickListener(this);
        this.findViewById(R.id.btn_6).setOnClickListener(this);
        this.findViewById(R.id.btn_5).setOnClickListener(this);
        this.findViewById(R.id.btn_4).setOnClickListener(this);
        this.findViewById(R.id.btn_3).setOnClickListener(this);
        this.findViewById(R.id.btn_2).setOnClickListener(this);
        this.findViewById(R.id.btn_1).setOnClickListener(this);
        this.findViewById(R.id.btn_0).setOnClickListener(this);
        this.findViewById(R.id.btn_divide).setOnClickListener(this);
        this.findViewById(R.id.btn_divide).setOnLongClickListener(this);
        this.findViewById(R.id.btn_multiply).setOnClickListener(this);
        this.findViewById(R.id.btn_multiply).setOnLongClickListener(this);
        this.findViewById(R.id.btn_plus).setOnClickListener(this);
        this.findViewById(R.id.btn_plus).setOnLongClickListener(this);
        this.findViewById(R.id.btn_subtract).setOnClickListener(this);
        this.findViewById(R.id.btn_subtract).setOnLongClickListener(this);
        this.findViewById(R.id.btn_clear).setOnClickListener(this);
        this.findViewById(R.id.btn_clear).setOnLongClickListener(this);
        this.findViewById(R.id.btn_equals).setOnClickListener(this);
        this.findViewById(R.id.btn_equals).setOnLongClickListener(this);
        this.findViewById(R.id.btn_pi).setOnClickListener(this);
        this.findViewById(R.id.btn_e).setOnLongClickListener(this);
        this.findViewById(R.id.btn_pi).setOnLongClickListener(this);
        this.findViewById(R.id.btn_e).setOnClickListener(this);
        this.findViewById(R.id.btn_bracket_left).setOnClickListener(this);
        this.findViewById(R.id.btn_bracket_right).setOnClickListener(this);
        this.findViewById(R.id.btn_separator).setOnClickListener(this);
        resultDisplay = findViewById(R.id.result);
        formulaDisplay = findViewById(R.id.formula);
        resultDisplay.setOnLongClickListener(this);
        formulaDisplay.setOnLongClickListener(this);
    }

}