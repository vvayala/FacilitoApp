package com.example.facilitoapp.utils;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void ShowDefaultErrorMessage(Context context) {
        Toast.makeText(context, "Algo salió mal, intente nuevamente.", Toast.LENGTH_SHORT).show();
    }
}
