package com.lgs.lgsclient.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lgs.lgsclient.Consumer;
import com.lgs.lgsclient.JSONAsyncTask;
import com.lgs.lgsclient.R;
import com.lgs.lgsclient.SensorReaderGenerator;
import com.lgs.lgsclient.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserActivity extends AppCompatActivity {
    private Session session;
    private Button monitorBtn;
    private Button cancelMonitorBtn;
    private TextView sensorReadingTv;
    private TextView responseTextView;
    private LinearLayout all_screen;
    boolean toMonitor;

    Thread monitorThread;
    NotificationManagerCompat notificationManager;
    Consumer consumer;
    Thread subscribeThread;
    static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        createNotificationChannel();
        ctx = this;
        notificationManager = NotificationManagerCompat.from(this);
        try {
            consumer = new Consumer();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        session = new Session(this);
        all_screen = findViewById(R.id.all_screen);
        sensorReadingTv = findViewById(R.id.sensorReading);
        responseTextView = findViewById(R.id.responseMonitorText);

        cancelMonitorBtn = findViewById(R.id.cancelMonitorButton);
        cancelMonitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMonitor = false;
                monitorThread = null;
                System.out.println(Thread.activeCount());
                cancelMonitorBtn.setVisibility(View.GONE);
                monitorBtn.setVisibility(View.VISIBLE);
            }
        });

        monitorBtn = findViewById(R.id.monitorButton);
        monitorBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                cancelMonitorBtn.setVisibility(View.VISIBLE);
                monitorBtn.setVisibility(View.GONE);
                toMonitor = true;
                if (monitorThread != null) {
                    monitorThread.start();
                }
                if (monitorThread == null) {
                    monitorThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(toMonitor){
                                try {
                                    sendHTTPRequest();
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    monitorThread.start();
                }
            }
        });
        subscribe();
    }

    @Override
    public void onBackPressed() {
        if (session.getAuthKey() != null)
            Toast.makeText(UserActivity.this, "Please logout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuProfile:
                startActivity(new Intent(UserActivity.this, ProfileActivity.class));
                return true;
            case R.id.menuHistory:
                startActivity(new Intent(UserActivity.this, HistoryActivity.class));
                return true;
            case R.id.menuLogout:
                session.clearSession();
                startActivity(new Intent(UserActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendHTTPRequest(){
            final JSONObject payload = SensorReaderGenerator.GenerateRandomSensorReadingWithMoreThan80PercentHigh(session);
        JSONAsyncTask asyncTask = new JSONAsyncTask("http://192.168.1.95:8080/lgs/v1/sensorData","POST", session, payload);
        try {
            JSONObject response = asyncTask.execute().get();
            final String threatLevel = (String)response.get("Risk Description");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (threatLevel){
                        case "Safe":
                            all_screen.setBackgroundColor(Color.GREEN);
                            break;
                        case "Low risk":
                            all_screen.setBackgroundColor(Color.YELLOW);
                            break;
                        case "Medium risk":
                            all_screen.setBackgroundColor(Color.rgb(255,165,0));
                            break;
                        case "High risk":
                            all_screen.setBackgroundColor(Color.RED);
                            break;
                    }
                    try {
                        JSONArray sensorPayload = (JSONArray) payload.get("sensorData");
                        String sensorReading = "";
                        for (int i = 0; i < sensorPayload.length(); i++) {
                            sensorReading += sensorPayload.getJSONObject(i).get("name") + ": " + sensorPayload.getJSONObject(i).get("value") + " ";
                        }
                        responseTextView.setText(threatLevel);
                        sensorReadingTv.setText(sensorReading);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (InterruptedException | JSONException | ExecutionException e) {
            e.printStackTrace();
        }
    }



    protected void subscribe() {
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = consumer.consume();
                        int requestID = (int) System.currentTimeMillis();
                        Intent notificationIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                        //**edit this line to put requestID as requestCode**
                        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "lgsNotifications")
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("Risk notification")
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(message))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setContentIntent(contentIntent);
                        notificationManager.notify(100,builder.build());


                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
        subscribeThread.start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificationChannel";
            String description = "Channel for emergency notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("lgsNotifications", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public static Context getContext(){
        return ctx;
    }
}
