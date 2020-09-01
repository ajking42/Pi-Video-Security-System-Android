package com.example.pi_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetFrameBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
    // Post request to get frame of selected file
    private WeakReference<ImageView> mImageView;
    private WeakReference<String> ipRef;
    private WeakReference<String> mFileName;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private boolean success;

    public GetFrameBitmapAsyncTask(ImageView imageView, String ip, String fileName) {
        mImageView = new WeakReference<ImageView>(imageView);
        ipRef = new WeakReference<String>(ip);
        mFileName = new WeakReference<String>(fileName);
    }


    @Override
    protected Bitmap doInBackground(String... strings) {
        JSONObject json = new JSONObject();
        MediaType JSON = MediaType.parse("application/json");
        try {
            json.put("filename", mFileName.get());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url(ipRef.get() + "/selectedimage")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store received image as bitmap
        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        response.body().close();

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        // Display bitmap image
        mImageView.get().setImageBitmap(bitmap);
    }
}
