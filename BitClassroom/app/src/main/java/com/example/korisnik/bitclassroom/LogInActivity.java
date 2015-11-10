package com.example.korisnik.bitclassroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.korisnik.bitclassroom.helpers.ConnectHelper;
import com.example.korisnik.bitclassroom.helpers.IpAddress;
import com.example.korisnik.bitclassroom.helpers.ServiceRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mSignInButton;
    public static String url = IpAddress.getIpAddress();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mEmailInput = (EditText) findViewById(R.id.email);
        mPasswordInput = (EditText) findViewById(R.id.password);
        prefs = getSharedPreferences("ba.classroom", Context.MODE_PRIVATE);
        if(prefs != null){
            mEmailInput.setText(prefs.getString("email",null));
            mPasswordInput.setText(prefs.getString("password",null));
        }
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFilled(mEmailInput, mPasswordInput)) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("email",mEmailInput.getText());
                        json.put("password",mPasswordInput.getText());
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

//                    if(prefs == null) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", mEmailInput.getText().toString());
                        editor.putString("password", mPasswordInput.getText().toString());
                        editor.commit();
//                    }
                    ProgressDialog pDialog = new ProgressDialog(LogInActivity.this);
                    pDialog.setMessage("Signing in ....");
                    pDialog.show();
                    ServiceRequest.post(url + "api/login", json.toString(), ConnectHelper.loginCheck(LogInActivity.this, url));
//                    Toast.makeText(getApplicationContext(), "Couldn't connect to server. Please check your email and password.", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();

                } else {
                    Toast.makeText(LogInActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isFilled(EditText email, EditText password){
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            return true;
        }else{
            return false;
        }
    }
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LogInActivity.class);
        return i;
    }

}