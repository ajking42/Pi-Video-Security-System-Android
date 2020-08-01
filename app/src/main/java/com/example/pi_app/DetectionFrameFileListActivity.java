package com.example.pi_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
        mrefreshButton = findViewById(R.id.refreshButton); // Manual sync button for list

        //Get ip address
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");

        // Execute get request to ask server for list of most recent detections
        new GetFileListAsyncTask(this, mDetectionsListView, ip).execute();

        // Set get request for syncing list manually using refresh button
        mrefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetFileListAsyncTask(DetectionFrameFileListActivity.this, mDetectionsListView, ip).execute();
            }
        });


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
}