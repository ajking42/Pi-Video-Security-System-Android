package com.example.pi_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.Preferences;

public class SettingsActivity extends PreferenceActivity {
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
        Preference preference = findPreference("model_preference");
        System.out.println(preference);
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ip = sp.getString("ip", "");
    }

    public void updatePiSettings(View view) throws JSONException {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        String settingsString = settings.getAll().toString().toLowerCase();

        String settingsJsonFormat = settingsString.replace('=', ':');

        JSONObject settingsJson = new JSONObject(settingsJsonFormat);

        new UpdatePiSettingsAsyncTask(settingsJson, ip).execute();

    }

}