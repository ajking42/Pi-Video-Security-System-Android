package com.example.pi_app;

import android.Manifest;

import androidx.test.espresso.intent.VerificationMode;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DetectionFrameFileListActivityTest {

    @Rule
    public IntentsTestRule<DetectionFrameFileListActivity> intentsTestRule =
            new IntentsTestRule<>(DetectionFrameFileListActivity.class);

    @Rule
    public GrantPermissionRule storage = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);


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

    @Test
    public void testListItemDownload() {
        onData(anything()).inAdapterView(withId(R.id.detectionListView)).atPosition(0).perform(click());
        clickMenu("Download File");
    }

    @Test
    public void testListItemView() {
        onData(anything()).inAdapterView(withId(R.id.detectionListView)).atPosition(0).perform(click());
        clickMenu("View File");
        intended(allOf(
                hasExtraWithKey("fileName"),
                hasComponent(FrameViewerActivity.class.getName())));
        onView(withId(R.id.downloadButton)).perform(click());
    }

    @Test
    public void testListItemDelete() {
        onData(anything()).inAdapterView(withId(R.id.detectionListView)).atPosition(0).perform(click());
        clickMenu("Delete File");
    }
}