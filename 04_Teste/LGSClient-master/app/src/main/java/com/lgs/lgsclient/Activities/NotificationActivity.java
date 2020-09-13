package com.lgs.lgsclient.Activities;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lgs.lgsclient.JSONAsyncTask;
import com.lgs.lgsclient.R;
import com.lgs.lgsclient.Session;

import java.util.concurrent.ExecutionException;

public class NotificationActivity extends AppCompatActivity {
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Notification");
        session = new Session(this);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        sendNotifications();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }

                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
        builder.setMessage("Reach out to SOS Contacts?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void sendNotifications(){
        JSONAsyncTask asyncTask = new JSONAsyncTask("http://192.168.1.95:8080/lgs/v1/user/contacts/notify/" + session.getUserID(),"GET", session);
        try {
            asyncTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
