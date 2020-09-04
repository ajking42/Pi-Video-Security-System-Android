package com.example.pi_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadFileAsyncTask extends AsyncTask <String, Integer, File> {
    private WeakReference<String> ipRef;
    private WeakReference<String> mFileName;
    private WeakReference<String> fileDirRef;
    private WeakReference<Context> contextRef;
    private ProgressDialog mprogressBar;


    public DownloadFileAsyncTask(String ip, String fileName, String fileDir, Context context) {
        ipRef = new WeakReference<String>(ip);
        mFileName = new WeakReference<String>(fileName);
        fileDirRef = new WeakReference<String>(fileDir);
        contextRef = new WeakReference<Context>(context);
        mprogressBar = new ProgressDialog(context);
        mprogressBar.setCancelable(true);
        mprogressBar.setMessage("File Downloading ...");
        mprogressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mprogressBar.setProgress(0);
        mprogressBar.setMax(100);
        mprogressBar.show();
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
            long fileLength  = response.body().contentLength();


            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mFileName.get());
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];
            int count;
            long total = 0;

            while ((count = in.read(data)) != -1) {
                total += count;
                publishProgress((int)(total*100/fileLength));
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
    protected void onProgressUpdate(Integer... values) {
        mprogressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(File file) {
        mprogressBar.dismiss();
        Toast toast = Toast.makeText(contextRef.get(), "File downloaded!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
