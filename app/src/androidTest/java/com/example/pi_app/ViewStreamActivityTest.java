package com.example.pi_app;

import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.PreferenceMatchers.withTitle;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ViewStreamActivityTest {

    @Rule
    public IntentsTestRule<ViewStreamActivity> intentsTestRule =
            new IntentsTestRule<>(ViewStreamActivity.class);


    @Before
    public void setUp() {
        intentsTestRule.getActivity();
    }

    @Test
    public void testDetectionButton() {
        clickMenu("Detections");
        intended(hasComponent(DetectionFrameFileListActivity.class.getName()));
    }

    @Test
    public void testStreamButton() {
        clickMenu("View Stream");
        intended(hasComponent(ViewStreamActivity.class.getName()));
    }

    @Test
    public void testRecordingsButton() {
        clickMenu("Recordings");
        intended(hasComponent(RecordingsFileListActivity.class.getName()));
    }

    @Test
    public void testSettingsButton() {
        clickMenu(R.id.settings_icon);
        intended(hasComponent(SettingsActivity.class.getName()));
    }

    @Test
    public void testSyncButton() {
        clickMenu(R.id.sync_icon);
    }


}