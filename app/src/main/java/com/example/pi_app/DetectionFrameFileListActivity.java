package com.example.pi_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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



        //When item in list is clicked, file name is stored in intent, and user is taken to the FrameViewer page
        mDetectionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(DetectionFrameFileListActivity.this)
                        .setItems(R.array.listOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                switch (j) {
                                    case 0:
                                        String selectedFile = (String) adapterView.getItemAtPosition(i);
                                        Intent intent = new Intent(DetectionFrameFileListActivity.this, FrameViewerActivity.class);
                                        intent.putExtra("fileName", selectedFile);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        downloadFile(adapterView, i);
                                        break;
                                    case 2:
                                        Toast toast = Toast.makeText(DetectionFrameFileListActivity.this, "Function not yet implemented", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                        toast.show();
                                }


                            }
                        })
                        .create().show();


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
                Intent settingsIntent = new Intent(DetectionFrameFileListActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return false;
            }
        });
        return true;
    }

    private void setBottomNavigationIntents(BottomNavigationView bottomNavigationView) {
        //Set bottom navigation intents
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

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

    private void downloadFile(AdapterView adapterView, int i) {
        // Request storage permission
        int STORAGE_PERMISSION_CODE = 1;
        final String selectedFile = (String) adapterView.getItemAtPosition(i);


        if(ContextCompat.checkSelfPermission(DetectionFrameFileListActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            new DownloadFileAsyncTask(ip, selectedFile, "detection_storage", DetectionFrameFileListActivity.this).execute();
            System.out.println("fdjfjfjf");

        } else if(ActivityCompat.shouldShowRequestPermissionRationale(DetectionFrameFileListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(DetectionFrameFileListActivity.this)
                    .setTitle("Storage Permission Needed")
                    .setMessage("This permission is needed in order to download the file")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new DownloadFileAsyncTask(ip, selectedFile, "detection_storage", DetectionFrameFileListActivity.this).execute();

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(DetectionFrameFileListActivity.this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }


    }


}