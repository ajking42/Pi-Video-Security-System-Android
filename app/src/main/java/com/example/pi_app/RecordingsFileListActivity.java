package com.example.pi_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
        mrefreshButton = findViewById(R.id.refreshButton); // Manual sync button for list

        //Get ip address
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");
        ip = ip + "/recordings_list";

        // Execute get request to ask server for list of most recent detections
        new GetFileListAsyncTask(this, mRecordingsListView, ip).execute();

        // Set get request for syncing list manually using refresh button
        mrefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetFileListAsyncTask(RecordingsFileListActivity.this, mRecordingsListView, ip).execute();
            }
        });


        //When item in list is clicked, file name is stored in intent, and user is taken to the FrameViewer page
        mRecordingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }


}