package com.example.pi_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetectionAlertActivity extends AppCompatActivity {
    //Displays a list of recent detections with their detection times and links to associated frames
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private ListView mDetectionsListView;
    private Button mrefreshButton;
    private String ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_alert);
        mDetectionsListView = findViewById(R.id.detectionListView);
        mrefreshButton = findViewById(R.id.refreshButton); // Manual sync button for list

        //Get ip address
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");

        // Execute get request to ask server for list of most recent detections
        new getRecentDetectionsAsync(this, mDetectionsListView, ip).execute();

        // Set get request for syncing list manually using refresh button
        mrefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getRecentDetectionsAsync(DetectionAlertActivity.this, mDetectionsListView, ip).execute();
            }
        });


        //When item in list is clicked, file name is stored in intent, and user is taken to the FrameViewer page
        mDetectionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFile = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(DetectionAlertActivity.this, FrameViewer.class);
                intent.putExtra("fileName", selectedFile);
                startActivity(intent);
            }
        });


    }


    public static class getRecentDetectionsAsync extends AsyncTask<String, Void, ArrayList<String>> {
        private WeakReference<ListView> mDetectionList;
        private WeakReference<Context> contextRef;
        private WeakReference<String> ipRef;

        public getRecentDetectionsAsync(Context context, ListView detectionList, String ip) {
            contextRef = new WeakReference<Context>(context);
            mDetectionList = new WeakReference<ListView>(detectionList);
            ipRef = new WeakReference<String>(ip);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            ArrayList<String> recentDetections = new ArrayList<String>();

            Request request = new Request.Builder()
                    .url(ipRef.get())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONArray myResponse = new JSONArray(response.body().string());

                // Convert json array into arraylist
                for (int i = 0; i < myResponse.length(); i++) {
                    recentDetections.add(myResponse.getString(i));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return recentDetections;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            ArrayAdapter adapter = new ArrayAdapter<String>(contextRef.get(), R.layout.support_simple_spinner_dropdown_item, result);
            // Display files in listview using ArrayAdaptor
            mDetectionList.get().setAdapter(adapter);
        }
    }
}