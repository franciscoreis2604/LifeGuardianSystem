package com.lgs.lgsclient.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lgs.lgsclient.JSONAsyncTask;
import com.lgs.lgsclient.R;
import com.lgs.lgsclient.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class HistoryActivity extends AppCompatActivity {
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("History");
        session = new Session(this);

        JSONAsyncTask loginAsyncTask = new JSONAsyncTask("http://192.168.1.95:8080/lgs/v1/sensorData/" + session.getUserID(),"GET",session);
        try {
            JSONObject response = loginAsyncTask.execute().get();
            JSONArray sensorPayload = (JSONArray) response.get("sensorData");
            System.out.println(sensorPayload.toString());
            for (int i = 0; i < sensorPayload.length(); i++) {
                String timeStamp = (String) sensorPayload.getJSONObject(i).get("timeStamp");
                String timeStampToPrint = timeStamp.split("\\.")[0];
                String sensorReading = sensorPayload.getJSONObject(i).get("name") + ": " + sensorPayload.getJSONObject(i).get("value") + ", Timestamp: " + timeStampToPrint;
                logEvent(sensorReading);
            }
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearScroll();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    protected void logEvent(String eventText){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setText(eventText);

        LinearLayout scrollLayout = findViewById(R.id.scrollLayout);
        scrollLayout.addView(tv);

    }

    private void clearScroll(){
        LinearLayout scrollLayout = findViewById(R.id.scrollLayout);
        scrollLayout.removeAllViews();
    }
}
