package com.example.pi_app;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendFirebaseDeviceTokenAsyncTask extends AsyncTask<String, Void, String> {
    private WeakReference<String> deviceTokenRef;
    private WeakReference<String> ipRef;

    public SendFirebaseDeviceTokenAsyncTask(String deviceToken, String ip) {
        deviceTokenRef = new WeakReference<String>(deviceToken);
        ipRef = new WeakReference<String>(ip);
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = ipRef.get() + "/setToken";
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = new FormBody.Builder()
                .add("token", deviceTokenRef.get())
                .build();

        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        String responseString = "";
        try {
            Response response = client.newCall(request).execute();
            responseString = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return responseString;
    }
}
