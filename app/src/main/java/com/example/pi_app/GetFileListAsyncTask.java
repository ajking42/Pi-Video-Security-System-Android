package com.example.pi_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetFileListAsyncTask extends AsyncTask <String, Void, ArrayList<String>> {
    private WeakReference<ListView> mFileList;
    private WeakReference<Context> contextRef;
    private WeakReference<String> ipRef;
    private boolean success;
    private ProgressDialog progressDialog;

    public GetFileListAsyncTask(Context context, ListView listView, String ip) {
        contextRef = new WeakReference<Context>(context);
        mFileList = new WeakReference<ListView>(listView);
        ipRef = new WeakReference<String>(ip);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Fetching File List...");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        ArrayList<String> fileArrayList = new ArrayList<String>();

        Request request = new Request.Builder()
                .url(ipRef.get())
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONArray myResponse = new JSONArray(response.body().string());

            // Convert json array into arraylist
            for (int i = 0; i < myResponse.length(); i++) {
                fileArrayList.add(myResponse.getString(i));
            }
            response.body().close();
            success = true;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            success = false;
        }
        return fileArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList result) {
        Collections.reverse(result);
        ArrayAdapter adapter = new ArrayAdapter<String>(contextRef.get(), R.layout.support_simple_spinner_dropdown_item, result);
        // Display files in listview using ArrayAdaptor
        mFileList.get().setAdapter(adapter);
        progressDialog.dismiss();
        if(result.isEmpty() && success) {
            Toast toast = Toast.makeText(contextRef.get(), "No files found.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else if (result.isEmpty() && !success) {
            Toast toast = Toast.makeText(contextRef.get(), "Connection to server failed.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }
}