package com.example.pi_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBottomBar();




    }





    private void setBottomBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return false;
            }
        });
        return true;
    }

    public void setSyncClickAction(MenuItem item) {

    }



}