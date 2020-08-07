package com.example.pi_app;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadFileAsyncTask extends AsyncTask <String, Void, File> {
    // TODO: Need to sort out android permissions in order to download files

    @Override
    protected File doInBackground(String... strings) {
        String ip = "http://192.168.0.23:5000/video_storage/07-31-2020,%2013:56:35.mp4";
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(ip)
                .build();
        File downloadFile = new File(Environment.getDownloadCacheDirectory().toString(), "output.mp4");

        try {
            Response response = client.newCall(request).execute();

            InputStream in = response.body().byteStream();

            byte[] bytes = new byte[in.available()];
            in.read(bytes);


            FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);

            fileOutputStream.write(bytes);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return downloadFile;
    }

}
