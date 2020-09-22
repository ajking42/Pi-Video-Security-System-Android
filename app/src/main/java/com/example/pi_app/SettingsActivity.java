package com.example.pi_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.Preferences;

public class SettingsActivity extends PreferenceActivity {
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.main_preferences, false);
        addPreferencesFromResource(R.xml.main_preferences);
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");
    }

    public void updatePiSettings(View view) throws JSONException {
        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle("Warning")
                .setMessage("In order to update Pi settings, the Pi system will be restarted.")
                .setPositiveButton("Restart Pi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

                        String settingsString = settings.getAll().toString().toLowerCase();

                        String settingsJsonFormat = settingsString.replace('=', ':');
                        settingsJsonFormat = settingsJsonFormat.replace('/',    '-');


                        JSONObject settingsJson = null;
                        try {
                            settingsJson = new JSONObject(settingsJsonFormat);
                        new UpdatePiSettingsAsyncTask(SettingsActivity.this, settingsJson, ip).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle("Warning")
                .setMessage("Changes to Pi settings will not be applied until you press 'Update Pi'")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Stay on page", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
}