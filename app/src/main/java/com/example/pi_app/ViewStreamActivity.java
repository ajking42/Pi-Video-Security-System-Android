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
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewStreamActivity extends AppCompatActivity {
    private WebView streamView;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
        streamView = findViewById(R.id.streamWebView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        setBottomNavigationIntents(bottomNavigationView);

        //Get ip address
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");

        streamView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast toast = Toast.makeText(ViewStreamActivity.this, "Check connections and press sync", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        });

        streamView.loadUrl(ip + "streaming");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    private void setBottomNavigationIntents(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home_icon:
                        Intent homeIntent = new Intent(ViewStreamActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;


                    case R.id.stream_icon:

                        break;


                    case R.id.detections_icon:
                        Intent detectionIntent = new Intent(ViewStreamActivity.this, DetectionFrameFileListActivity.class);
                        startActivity(detectionIntent);
                        break;

                    case R.id.video_icon:
                        Intent videoIntent = new Intent(ViewStreamActivity.this, RecordingsFileListActivity.class);
                        startActivity(videoIntent);
                        break;
                }

                return false;
            }
        });
    }

    public void setSyncClickAction(MenuItem item) {
        streamView.loadUrl(ip + "streaming");
    }
}