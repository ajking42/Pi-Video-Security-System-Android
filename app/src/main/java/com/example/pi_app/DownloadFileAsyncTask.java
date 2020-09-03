package com.example.pi_app;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadFileAsyncTask extends AsyncTask <String, Void, File> {
    // TODO: Need to sort out android permissions in order to download files
    private WeakReference<String> ipRef;
    private WeakReference<String> mFileName;
    private WeakReference<String> fileDirRef;


    public DownloadFileAsyncTask(String ip, String fileName, String fileDir) {
        ipRef = new WeakReference<String>(ip);
        mFileName = new WeakReference<String>(fileName);
        fileDirRef = new WeakReference<String>(fileDir);
    }

    @Override
    protected File doInBackground(String... strings) {

        String url = ipRef.get() + fileDirRef.get() + "/" + mFileName.get();

        OkHttpClient client = new OkHttpClient.Builder().build();
        System.out.println(url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();


            InputStream in = response.body().byteStream();

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mFileName.get());
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];
            int count;
            long total = 0;

            while ((count = in.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
                System.out.println(count);
            }
            System.out.println(count);
            output.flush();
            output.close();
            in.close();
            response.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }
}
