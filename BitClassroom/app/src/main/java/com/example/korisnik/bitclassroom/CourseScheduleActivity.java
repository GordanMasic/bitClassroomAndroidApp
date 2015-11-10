package com.example.korisnik.bitclassroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.korisnik.bitclassroom.helpers.IpAddress;

/**
 * Created by Korisnik on 11/2/2015.
 */
public class CourseScheduleActivity extends AppCompatActivity {

    private static String courseId;
    private static String mainUrl = IpAddress.getIpAddress() + "api/calendar/";
    private static WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);
        web = (WebView) findViewById(R.id.course_schedule);
        mainUrl += courseId;
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(mainUrl);
    }

    public static Intent newIntent(Context packageContext, String url) {
        Intent i = new Intent(packageContext, CourseScheduleActivity.class);
        courseId = url;
        return i;
    }
}
