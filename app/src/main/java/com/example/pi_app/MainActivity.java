package com.example.pi_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home_icon:
                        break;


                    case R.id.stream_icon:
                        Intent streamIntent = new Intent(MainActivity.this, ViewStreamActivity.class);
                        startActivity(streamIntent);
                        break;


                    case R.id.detections_icon:
                        Intent detectionIntent = new Intent(MainActivity.this, DetectionFrameFileListActivity.class);
                        startActivity(detectionIntent);
                        break;

                    case R.id.video_icon:
                        Intent videoIntent = new Intent(MainActivity.this, RecordingsFileListActivity.class);
                        startActivity(videoIntent);
                        break;
                }

                return false;
            }
        });


        //Initialise shared preferences to store base server url
        mPreferences = getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("ip", "http://192.168.0.34:5000/");
        preferencesEditor.apply();




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    public void setSyncClickAction(MenuItem item) {

    }
}