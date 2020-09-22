package com.example.pi_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FrameViewerActivity extends AppCompatActivity {


    private String ip;
    private String selectedFile;
    private ImageView mFrameView;
    private Button mDownloadButton;
    private TextView mImageTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_viewer);
        mFrameView = findViewById(R.id.frameView);
        mImageTitle = findViewById(R.id.imageName);
        mDownloadButton = findViewById(R.id.downloadButton);

        // Get file selected in list of detections
        selectedFile = getIntent().getStringExtra("fileName");

        // Get ip from shared preferences
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip","");
        System.out.println(ip);

        // Send post request to get frame of selected file from server
        new GetFrameBitmapAsyncTask(mFrameView, ip, selectedFile).execute();
        mImageTitle.setText(selectedFile);

        // TODO: add left and right buttons and make frame list cyclable

    }

    public void downloadFile(View view) {
        new DownloadFileAsyncTask(ip, selectedFile, "detection_storage", FrameViewerActivity.this).execute();

    }
}