package com.lgs.lgsclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.lgs.lgsclient.LoginAsynTask;
import com.lgs.lgsclient.R;
import com.lgs.lgsclient.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginBtn = findViewById(R.id.loginButton);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("username", emailField.getText().toString());
                    payload.put("password", passwordField.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LoginAsynTask loginAsynTask = new LoginAsynTask(session,"http://192.168.1.95:8080/login",payload);
                try {
                    Boolean logged = loginAsynTask.execute().get();
                    if(logged) startActivity(new Intent(MainActivity.this, UserActivity.class));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
