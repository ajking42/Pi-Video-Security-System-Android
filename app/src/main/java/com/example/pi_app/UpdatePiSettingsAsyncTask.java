package com.example.pi_app;

import android.os.AsyncTask;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatePiSettingsAsyncTask extends AsyncTask<JSONObject, Void, Void> {
    private JSONObject preferences;
    private WeakReference<String> ipRef;

    public UpdatePiSettingsAsyncTask(JSONObject jsonObject, String ip) {
        preferences = jsonObject;
        ipRef = new WeakReference<>(ip);
    }

    @Override
    protected Void doInBackground(JSONObject... jsonObjects) {

        OkHttpClient client = new OkHttpClient.Builder().build();

        MediaType JSON = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(JSON, preferences.toString());

        Request request = new Request.Builder()
                .url(ipRef.get() + "update_yaml")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
