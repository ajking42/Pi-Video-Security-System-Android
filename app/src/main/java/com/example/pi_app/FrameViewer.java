package com.example.pi_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FrameViewer extends AppCompatActivity {


    private String ip;
    private ImageView mFrameView;
    private Bitmap frame;
    private String frameString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_viewer);




        String selectedFile = getIntent().getStringExtra("fileName");

        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip","");
        System.out.println(ip);


        mFrameView = findViewById(R.id.frameView);


        new getFrameAsync(this, mFrameView, ip, selectedFile).execute();

    }




    public static class getFrameAsync extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<ImageView> mFrameView;
        private WeakReference<Context> contextRef;
        private WeakReference<String> ipRef;
        private WeakReference<String> mFileName;
        private OkHttpClient client = new OkHttpClient.Builder().build();

        public getFrameAsync(Context context, ImageView imageView, String ip, String fileName) {
            contextRef = new WeakReference<Context>(context);
            mFrameView = new WeakReference<ImageView>(imageView);
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
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mFrameView.get().setImageBitmap(bitmap);
        }
    }
}