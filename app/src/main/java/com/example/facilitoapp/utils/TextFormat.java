package com.example.facilitoapp.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.List;

public class TextFormat implements TextWatcher {
    private final EditText editText;
    private final List<Integer> cutPositions;
    private final String separator;
    private boolean isFormatting;

    public TextFormat(EditText editText, List<Integer> cutPositions, String separator) {
        this.editText = editText;
        this.cutPositions = cutPositions;
        this.separator = separator;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isFormatting) return;

        isFormatting = true;
        String digits = s.toString().replace(separator, "");
        StringBuilder formatted = new StringBuilder();

        int index = 0;
        for (int i = 0; i < digits.length(); i++) {
            formatted.append(digits.charAt(i));
            index++;
            if (cutPositions.contains(index) && i < digits.length() - 1) {
                formatted.append(separator);
            }
        }

        editText.setText(formatted.toString());
        editText.setSelection(formatted.length());
        isFormatting = false;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public static String getRawValue(EditText editText, String separator) {
        return editText.getText().toString().replace(separator, "");
    }
}
