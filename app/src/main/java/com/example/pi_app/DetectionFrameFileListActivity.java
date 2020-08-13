package com.example.pi_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.OkHttpClient;

public class DetectionFrameFileListActivity extends AppCompatActivity {
    //Displays a list of recent detections with their detection times and links to associated frames
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private ListView mDetectionsListView;
    private Button mrefreshButton;
    private String ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_frame_file_list);
        mDetectionsListView = findViewById(R.id.detectionListView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);

        setBottomNavigationIntents(bottomNavigationView);



        //Get ip address
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");

        // Execute get request to ask server for list of most recent detections
        new GetFileListAsyncTask(this, mDetectionsListView, ip).execute();

        displayNoFilesToast();

        //When item in list is clicked, file name is stored in intent, and user is taken to the FrameViewer page
        mDetectionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFile = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(DetectionFrameFileListActivity.this, FrameViewerActivity.class);
                intent.putExtra("fileName", selectedFile);
                startActivity(intent);
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
        //Set bottom navigation intents
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home_icon:
                        Intent homeIntent = new Intent(DetectionFrameFileListActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;


                    case R.id.stream_icon:
                        Intent streamIntent = new Intent(DetectionFrameFileListActivity.this, ViewStreamActivity.class);
                        startActivity(streamIntent);
                        break;


                    case R.id.detections_icon:
                        Intent detectionIntent = new Intent(DetectionFrameFileListActivity.this, DetectionFrameFileListActivity.class);
                        startActivity(detectionIntent);
                        break;

                    case R.id.video_icon:
                        Intent videoIntent = new Intent(DetectionFrameFileListActivity.this, RecordingsFileListActivity.class);
                        startActivity(videoIntent);
                        break;
                }

                return false;
            }
        });

    }

    public void setSyncClickAction(MenuItem item) {
        new GetFileListAsyncTask(this, mDetectionsListView, ip).execute();
    }

    public void displayNoFilesToast() {
        // Display toast if no files are found or server request fails
        if(mDetectionsListView.getAdapter() == null) {
            Toast toast = Toast.makeText(DetectionFrameFileListActivity.this, "No files found. Check system connections and try again.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }
}