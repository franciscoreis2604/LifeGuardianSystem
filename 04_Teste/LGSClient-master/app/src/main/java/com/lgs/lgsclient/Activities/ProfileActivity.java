package com.lgs.lgsclient.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lgs.lgsclient.JSONAsyncTask;
import com.lgs.lgsclient.R;
import com.lgs.lgsclient.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {
    private Session session;
    private TextView userIdTv;
    private TextView userNameTv;
    private TextView phoneNumberTv;
    private TextView birthDateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");
        session = new Session(this);
        userIdTv = findViewById(R.id.userIdValue);
        userNameTv = findViewById(R.id.userNameValue);
        phoneNumberTv = findViewById(R.id.phoneNumberValue);
        birthDateTv = findViewById(R.id.birthdateValue);

        JSONAsyncTask loginAsyncTask = new JSONAsyncTask("http://192.168.1.95:8080/lgs/v1/user/" + session.getUserID(),"GET",session);
        try {
            JSONObject response = loginAsyncTask.execute().get();
            userIdTv.setText ("ID: " + (String)response.get("id"));
            userNameTv.setText ("Name: " + (String)response.get("name"));
            phoneNumberTv.setText ("Phone number: " + (String)response.get("phoneNumber"));
            birthDateTv.setText ("Birthday: " + (String)response.get("birthDate"));
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
    public void onBackPressed() {
        finish();
    }
}
