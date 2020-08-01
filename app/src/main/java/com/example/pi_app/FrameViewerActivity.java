package com.example.pi_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class FrameViewerActivity extends AppCompatActivity {


    private String ip;
    private ImageView mFrameView;
    private Bitmap frame;
    private String frameString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_viewer);
        mFrameView = findViewById(R.id.frameView);

        // Get file selected in list of detections
        String selectedFile = getIntent().getStringExtra("fileName");

        // Get ip from shared preferences
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip","");
        System.out.println(ip);

        // Send post request to get frame of selected file from server
        new GetFrameBitmapAsyncTask(mFrameView, ip, selectedFile).execute();

        // TODO: add left and right buttons and make frame list cyclable

    }
}