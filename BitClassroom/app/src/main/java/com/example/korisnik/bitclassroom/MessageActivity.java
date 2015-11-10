package com.example.korisnik.bitclassroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.korisnik.bitclassroom.helpers.ConnectHelper;
import com.example.korisnik.bitclassroom.helpers.ServiceRequest;
import com.example.korisnik.bitclassroom.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Korisnik on 10/30/2015.
 */
public class MessageActivity extends AppCompatActivity {

    public static User user;
    private EditText subject;
    private EditText message;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);

        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.message);
        sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageSubject = subject.getText().toString();
                String messageContent = message.getText().toString();

                ProgressDialog pDialog = new ProgressDialog(MessageActivity.this);
                pDialog.setMessage("Sending message ....");
                pDialog.show();

                ConnectHelper.sendMessage(messageSubject, messageContent, MessageActivity.this);
                Toast.makeText(getApplicationContext(), "You've send message succesfully!", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                onBackPressed();
            }
        });
    }

    public static Intent newIntent(Context packageContext, User receiver) {
        Intent i = new Intent(packageContext, MessageActivity.class);
        user = receiver;
        return i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_logout:
                ConnectHelper.coursePictures.clear();
                ConnectHelper.courseWebIds.clear();
                ConnectHelper.courseNames.clear();
                ConnectHelper.courseDescriptions.clear();
                ConnectHelper.courseTeachers.clear();
                ConnectHelper.courses.clear();

                ConnectHelper.posts.clear();
                ConnectHelper.postContents.clear();
                ConnectHelper.postDueDates.clear();
                ConnectHelper.postNames.clear();
                ConnectHelper.postTimestamps.clear();
                ConnectHelper.postUsers.clear();

                ConnectHelper.students.clear();
                ConnectHelper.studentPics.clear();
                ConnectHelper.studentTokens.clear();
                ConnectHelper.studentRoles.clear();
                ConnectHelper.studentNames.clear();
                ConnectHelper.studentLastnames.clear();
                ConnectHelper.studentIds.clear();

                SharedPreferences prefs = getSharedPreferences("ba.classroom",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("user_id");
                editor.commit();

                Intent i = LogInActivity.newIntent(MessageActivity.this);
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
