package com.freshism.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesController {

    private static final String DATA_EMAIL = "";

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setDataLogin(Context context, String email) {
        // menyimpan email user ketika di form login agar user tidak perlu login lagi ketike sudah login
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DATA_EMAIL, email);
        editor.apply();
    }

    public static String getEmail(Context context){
        // mendapatkan email user berdasarkan form login
        return getSharedPreferences(context).getString(DATA_EMAIL, "");
    }

    public static void clearData(Context context){
        // Hapus Akun User dari device, agar ketika buka aplikasi harus login lagi
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(DATA_EMAIL);
        editor.apply();
    }

}
