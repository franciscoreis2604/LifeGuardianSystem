package com.lgs.lgsclient;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class JSONAsyncTask extends AsyncTask<String, String, JSONObject> {
    private String url;
    private String method;
    private Session session;

    private JSONObject payload;

    public JSONAsyncTask(String url,String method, Session session) {
        this.url = url;
        this.method = method;
        this.session = session;
        this.payload = null;

    }

    public JSONAsyncTask(String url,String method, Session session, JSONObject payload) {
        this.url = url;
        this.method = method;
        this.session = session;
        this.payload = payload;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected JSONObject doInBackground(String... urls) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod(method);
            if (method.equals("POST")){
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
            }

            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Authorization", session.getAuthKey());

            //Send request
            if(payload != null)
                conn.getOutputStream().write(payload.toString().getBytes());

            //Get response
            BufferedReader br = null;
            String response = "";
            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 399)
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            else br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

            response = br.lines().collect(Collectors.joining());
            return new JSONObject(response);

        } catch (IOException | JSONException e ) {
            e.printStackTrace();
        }
        return null;
    }
}



