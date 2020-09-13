package com.lgs.lgsclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setAuthKey(String authKey) {
        prefs.edit().putString("Authorization", authKey).commit();
    }

    public void setUserId(String userId){
        prefs.edit().putString("UserID", userId).commit();
    }

    public String getAuthKey() {
        return prefs.getString("Authorization","");
    }

    public String getUserID(){
        return prefs.getString("UserID","");
    }

    public void clearSession(){
        prefs.edit().clear().commit();
    }
}
