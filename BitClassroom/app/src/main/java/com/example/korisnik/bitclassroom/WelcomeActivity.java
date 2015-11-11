package com.example.korisnik.bitclassroom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.korisnik.bitclassroom.helpers.ConnectHelper;
import com.example.korisnik.bitclassroom.helpers.IpAddress;
import com.example.korisnik.bitclassroom.helpers.ServiceRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Starting activity in application. By clicking on button "Enter classroom",
 * first is checked is user still signed into application. If user is signed in it will
 * redirect depending of the roll of student on next activity. If user isn't signed into
 * application it will redirect to LogInActivity.
 */
public class WelcomeActivity extends AppCompatActivity {

    //Default properties
    private ImageView imageView;
    private Button mEnterButton;
    public static SharedPreferences prefs;
    public static String url = IpAddress.getIpAddress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        imageView = (ImageView) findViewById(R.id.image_welcome);
        imageView.setImageResource(R.drawable.bc_logo_med);
        mEnterButton = (Button) findViewById(R.id.enter_button);

        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfSignedIn();
            }
        });
    }

    /**
     * Method that checks is there already user in session.
     */
    public void checkIfSignedIn(){
        prefs = getSharedPreferences("ba.classroom", Context.MODE_PRIVATE);
        String email = prefs.getString("email",null);
        String password = prefs.getString("password",null);

        if(email != "" && email != null && password != null && password != ""){

        }else{
            Intent i = LogInActivity.newIntent(WelcomeActivity.this);
            startActivityForResult(i, 0);
        }
    }

    /**
     * Method that sends request to web server. This request is for signing into web application.
     * @param email - User email.
     * @param password - User password.
     */
    public void sendRequestToServer(String email, String password){
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password",password);
        }catch(JSONException e){
            e.printStackTrace();
        }

        ConnectHelper.clearAll();

        ProgressDialog pDialog = new ProgressDialog(WelcomeActivity.this);
        pDialog.setMessage("Entering classroom ....");
        pDialog.show();

        ServiceRequest.post(url + "api/login", json.toString(), ConnectHelper.loginCheck(WelcomeActivity.this,url));

        pDialog.dismiss();
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Do you want to exit?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                WelcomeActivity.this.finish();
            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        alert.show();
    }


}
