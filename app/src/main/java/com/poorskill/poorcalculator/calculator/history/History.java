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
package com.poorskill.poorcalculator.calculator.history;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Class for loading and saving calculations
 * <p>
 * Calculations are getting converted into HistoryCalculations and serialized as ArrayList and saved on the Android phone
 */
public class History {
    private final String FILE_NAME = "history.ser";
    private Context context;

    /**
     * Constructor for setting up History-Class
     *
     * @param context context of the application
     */
    public History(Context context) {
        this.context = context;
    }

    /**
     * Tries to read the File and deserialize it as ArrayList and returns it
     *
     * @return result of reading as ArrayList<HistoryCalculation>
     */
    public List<HistoryCalculation> read() {
        ArrayList<HistoryCalculation> result = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            result = new ArrayList<>((ArrayList<HistoryCalculation>) in.readObject());
            in.close();
            fileInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Tries to write/serialize a list of HistoryCalculations into the file
     *
     * @param hcs List of HistoryCalculations
     */
    public void write(List<HistoryCalculation> hcs) {
        if (hcs.isEmpty()) {
            return;
        }
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(hcs);
            out.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads/Deserializes the current HistoryCalculationsList, adds the param to the currently saved history calculation list and saves/serializes the list again
     *
     * @param newHC new HistoryCalculation
     */
    public void addToHistory(HistoryCalculation newHC) {
        ArrayList<HistoryCalculation> currentHCS = new ArrayList<>(read());
        currentHCS.add(newHC);
        write(currentHCS);
    }
}
