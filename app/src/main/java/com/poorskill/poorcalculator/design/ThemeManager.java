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
package com.poorskill.poorcalculator.design;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.poorskill.poorcalculator.R;

/**
 * Utility class to manage (load, save, set) the current theme of the application
 * Since I had no idea about android design or programming in general ... big oof
 */
public final class ThemeManager {
    private static final int AMOUNT_OF_THEMES = 2;
    private static final String THEME_KEY = "theme";

    /**
     * Gets the in SharedPreferences saved current theme number
     *
     * @param context context of the application
     * @return current theme number
     */
    private static int getThemeNumberFormSharedPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(THEME_KEY, 0);
    }

    /**
     * Add one to the current theme number and if the number is greater than the amount of themes, then the counter starts from zero.
     * Calls the setTheme function
     *
     * @param context context of the application
     * @param view    view of the current activity
     */
    public static void ChangeTheme(Context context, View view) {
        setThemeInSharedPreferences((getThemeNumberFormSharedPreferences(context) + 1) >= AMOUNT_OF_THEMES ? 0 : getThemeNumberFormSharedPreferences(context) + 1, context);
        setTheme(view, context);
    }

    /**
     * Saves the given value of the current theme into the shared preferences
     *
     * @param value   current theme index
     * @param context context of the application
     */
    private static void setThemeInSharedPreferences(int value, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(THEME_KEY, value).apply();
    }

    /**
     * Checks the current theme index and sets the needed color values according to the resources and calls the setColors function
     * Yep, I like spaghetti
     *
     * @param view    view of the current activity
     * @param context context of the application
     */
    public static void setTheme(View view, Context context) {
        //BackgroundColor
        int btnEquals = 0;
        int display = 0;
        int btnFunction = 0;
        int btnOperation = 0;
        int btnNumber = 0;
        //TextColor
        int textColorNormal = 0;
        int textColorSpecial = 0;
        //switch in case of adding more themes in further updates :)
        switch (getThemeNumberFormSharedPreferences(context)) {
            case 0:
                btnEquals = ContextCompat.getColor(context, R.color.btnEqualsBlue);
                display = ContextCompat.getColor(context, R.color.result_displayBlue);
                btnFunction = ContextCompat.getColor(context, R.color.btnColorFunctionBlue);
                btnOperation = ContextCompat.getColor(context, R.color.btnColorOperationBlue);
                btnNumber = ContextCompat.getColor(context, R.color.btnColorNumberBlue);
                textColorNormal = ContextCompat.getColor(context, R.color.normalColorBlue);
                textColorSpecial = ContextCompat.getColor(context, R.color.specialTextColorBlue);
                break;
            case 1:
                btnEquals = ContextCompat.getColor(context, R.color.btnEqualsDark);
                display = ContextCompat.getColor(context, R.color.result_displayDark);
                btnFunction = ContextCompat.getColor(context, R.color.btnColorFunctionDark);
                btnOperation = ContextCompat.getColor(context, R.color.btnColorOperationDark);
                btnNumber = ContextCompat.getColor(context, R.color.btnColorNumberDark);
                textColorNormal = ContextCompat.getColor(context, R.color.normalTextColorBlack);
                textColorSpecial = ContextCompat.getColor(context, R.color.specialTextColorBlack);
                break;
        }
        setColors(view, display, btnFunction, btnOperation, btnNumber, btnEquals, textColorNormal, textColorSpecial);
    }

    /**
     * Oh boy
     * Hot mess of spaghetti code because I have no idea how to use android xml properly - Best way I knew to solve my problem :)
     * Finds every Object by ID and sets new Color
     * Not robust against layout changes
     *
     * @param view                  application view
     * @param displayColor          color value of result and formula display in integer
     * @param btnFunctionColor      color value of function buttons display in integer
     * @param btnOperationColor     color value of operation buttons display in integer
     * @param btnNumberColor        color value of digit buttons in integer
     * @param btnEqualsColor        color value of equal button in integer
     * @param textColorNormalColor  color value of normal text color in integer
     * @param textColorSpecialColor color value of special text color in integer
     */
    private static void setColors(View view, int displayColor, int btnFunctionColor, int btnOperationColor, int btnNumberColor, int btnEqualsColor, int textColorNormalColor, int textColorSpecialColor) {
        //Background
        view.findViewById(R.id.display_layout).setBackgroundColor(displayColor);
        view.findViewById(R.id.functionButtonLayout).setBackgroundColor(btnFunctionColor);
        view.findViewById(R.id.operationButton_0).setBackgroundColor(btnOperationColor);
        view.findViewById(R.id.operationButton_1).setBackgroundColor(btnOperationColor);
        view.findViewById(R.id.operationButton_2).setBackgroundColor(btnOperationColor);
        view.findViewById(R.id.operationButton_3).setBackgroundColor(btnOperationColor);
        view.findViewById(R.id.operationButton_4).setBackgroundColor(btnOperationColor);
        view.findViewById(R.id.operationButton_5).setBackgroundColor(btnOperationColor);
        view.findViewById(R.id.decimalSeparatorButton).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.bracketButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.zeroButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.oneButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.twoButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.threeButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.fourButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.fiveButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.sixButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.sevenButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.eightButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.nineButtonLayout).setBackgroundColor(btnNumberColor);
        view.findViewById(R.id.equalsButtonLayout).setBackgroundColor(btnEqualsColor);
        //Text
        ((TextView) view.findViewById(R.id.smallTextButton0)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.smallTextButton1)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.smallTextButton2)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.bigTextButton0)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.bigTextButton1)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.bigTextButton2)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.bigTextButton3)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.bigTextButton4)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.bigTextButton5)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.bigTextButton6)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_bracket_right)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_bracket_left)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_separator)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_0)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_1)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_2)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_3)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_4)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_5)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_6)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_7)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_8)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.btn_9)).setTextColor(textColorNormalColor);
        ((TextView) view.findViewById(R.id.formula)).setTextColor(textColorSpecialColor);
        ((TextView) view.findViewById(R.id.result)).setTextColor(textColorSpecialColor);
        ((TextView) view.findViewById(R.id.btn_equals_text)).setTextColor(textColorSpecialColor);
    }
}
