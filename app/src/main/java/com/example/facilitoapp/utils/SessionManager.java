package com.example.facilitoapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String KEY_USER_ID    = "user_id";
    private static final String KEY_TOKEN      = "access_token";
    private static final String KEY_REFRESH    = "refresh_token";
    private static final String KEY_USER_ROLE_ID = "user_role_id";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("facilito_prefs", Context.MODE_PRIVATE);
    }

    public void saveUserId(String userId) {
        prefs.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public void saveTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString(KEY_TOKEN, accessToken)
                .putString(KEY_REFRESH, refreshToken)
                .apply();
    }

    public String getAccessToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUserRoleId(String roleId) {
        prefs.edit().putString(KEY_USER_ROLE_ID, roleId).apply();
    }

    public String getUserRoleId() {
        return prefs.getString(KEY_USER_ROLE_ID, null);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
