package com.example.facilitoapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String APP_PREFS = "facilito_user";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUserId(String userId) {
        editor.putString("user_id", userId);
        editor.apply();
    }

    public String getUserId() {
        return prefs.getString("user_id", null);
    }

    public void clearSession() {
        editor.clear().apply();
    }
}
