package com.example.pi_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.OkHttpClient;

public class RecordingsFileListActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private ListView mRecordingsListView;
    private Button mrefreshButton;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings_file_list);
        mRecordingsListView = findViewById(R.id.recordingsListView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        setBottomNavigationIntents(bottomNavigationView);

        //Get ip address
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");
        ip = ip + "/recordings_list";

        // Execute get request to ask server for list of most recent detections
        new GetFileListAsyncTask(this, mRecordingsListView, ip).execute();

        displayNoFilesToast();



        //When item in list is clicked, file name is sent to server for downloading
        mRecordingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFile = (String) adapterView.getItemAtPosition(i);
                new DownloadFileAsyncTask(ip, selectedFile).execute();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    public void setBottomNavigationIntents(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home_icon:
                        Intent homeIntent = new Intent(RecordingsFileListActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;


                    case R.id.stream_icon:
                        Intent streamIntent = new Intent(RecordingsFileListActivity.this, ViewStreamActivity.class);
                        startActivity(streamIntent);
                        break;


                    case R.id.detections_icon:
                        Intent detectionIntent = new Intent(RecordingsFileListActivity.this, DetectionFrameFileListActivity.class);
                        startActivity(detectionIntent);
                        break;

                    case R.id.video_icon:

                        break;
                }

                return false;
            }
        });
    }

    public void setSyncClickAction(MenuItem item) {
        new GetFileListAsyncTask(this, mRecordingsListView, ip).execute();
    }

    public void displayNoFilesToast() {
        if(mRecordingsListView.getAdapter() == null) {
            Toast toast = Toast.makeText(RecordingsFileListActivity.this, "No files found. Check system connections and try again.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }


    public void setSettingsClickAction(MenuItem item) {
        Intent settingsIntent = new Intent(RecordingsFileListActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}