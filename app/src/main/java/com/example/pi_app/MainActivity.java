package com.example.pi_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private Button viewDetectionButton;
    private Button viewStreamButton;
    private Button viewRecordings;

    public final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewDetectionButton = findViewById(R.id.viewDetectionsButton);
        viewStreamButton = findViewById(R.id.viewStreamButton);
        viewRecordings = findViewById(R.id.viewRecordingsButton);

        //Initialise shared preferences to store base server url
        mPreferences = getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("ip", "http://192.168.0.23:5000/");
        preferencesEditor.apply();

        //TODO: add video request button


    }

    public void seeDetections(View view) {
        Intent intent = new Intent(MainActivity.this, DetectionFrameFileListActivity.class);
        startActivity(intent);
    }


    public void viewStream(View view) {
        Intent intent = new Intent(MainActivity.this, ViewStreamActivity.class);
        startActivity(intent);

    }

    public void viewRecordings(View view) {
        Intent intent = new Intent(MainActivity.this, RecordingsFileListActivity.class);
        startActivity(intent);
    }
}