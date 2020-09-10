package com.lgs.lgsclient;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAsynTask extends AsyncTask<String, String, Boolean> {
    private Session session;
    private String url;
    private JSONObject payload;
    public LoginAsynTask(Session session,String url,JSONObject payload) {
        this.session = session;
        this.url = url;
        this.payload = payload;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");

            //Send request
            conn.getOutputStream().write(payload.toString().getBytes());

            String auth = conn.getHeaderField("Authorization");
            String uId = conn.getHeaderField("UserID");
            if(auth != null && uId != null){
                session.setAuthKey(auth);
                session.setUserId(uId);
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
