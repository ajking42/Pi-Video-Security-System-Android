package com.example.pi_app;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.PreferenceMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)

public class SettingsActivityTest {

    @Rule
    public IntentsTestRule<SettingsActivity> intentsTestRule =
            new IntentsTestRule<>(SettingsActivity.class);


    @Test
    public void testBackWarningAppearsContinue() {
        pressBack();
        onView(withText("Warning")).check(matches(isDisplayed()));
        onView(withText("Continue")).perform(click());
        assertTrue(intentsTestRule.getActivity().isDestroyed());
    }

    @Test
    public void testBackWarningAppearsCancel() {
        pressBack();
        onView(withText("Warning")).check(matches(isDisplayed()));
        onView(withText("Stay on page")).perform(click());
        isDisplayed().matches(intentsTestRule.getActivity());
    }

    @Test
    public void testUpdatePiRestart() {
        onView(withId(R.id.updateButton)).perform(click());
        onView(withText("Warning")).check(matches(isDisplayed()));
        onView(withText("Restart Pi")).perform(click());
    }

    @Test
    public void testUpdatePiCancel() {
        onView(withId(R.id.updateButton)).perform(click());
        onView(withText("Warning")).check(matches(isDisplayed()));
        onView(withText("Cancel")).perform(click());
    }

    @Test
    public void testDetectionModelPref() {
        onData(PreferenceMatchers.withTitleText("Detection Model")).perform(click());
        clickMenu("Fast");
        onView(withText("Detection Model")).perform(click());
        clickMenu("Accurate");
    }

    @Test
    public void testDetectionCategoriesPref() {
        onData(PreferenceMatchers.withTitleText("Select Detection Categories")).perform(click());
        clickMenu("Person");
        clickMenu("Dog");
        clickMenu("Car");
        onView(withText("Cancel")).perform(click());
        onView(withText("Select Detection Categories")).perform(click());
        clickMenu("Person");
        clickMenu("Dog");
        clickMenu("Car");
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testImageStoragePref() {
        onData(PreferenceMatchers.withTitleText("Max. Image Storage (MB)")).perform(click());
        onView(withText("10000")).perform(replaceText("500"));
        onView(withText("Cancel")).perform(click());
        onView(withText("Max. Image Storage (MB)")).perform(click());
        onView(withText("10000")).perform(replaceText("500"));
        onView(withText("OK")).perform(click());
        onView(withText("Max. Image Storage (MB)")).perform(click());
        isDisplayed().matches(withText("500"));
        onView(withText("500")).perform(replaceText("10000"));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testVideoStoragePref() {
        onData(PreferenceMatchers.withTitleText("Max. Video Storage (MB)")).perform(click());
        onView(withText("10000")).perform(replaceText("500"));
        onView(withText("Cancel")).perform(click());
        onView(withText("Max. Video Storage (MB)")).perform(click());
        onView(withText("10000")).perform(replaceText("500"));
        onView(withText("OK")).perform(click());
        onView(withText("Max. Video Storage (MB)")).perform(click());
        isDisplayed().matches(withText("500"));
        onView(withText("500")).perform(replaceText("10000"));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testNotificationPeriodPref() {
        onData(PreferenceMatchers.withTitleText("Repeat Notification Period (s)")).perform(click());
        onView(withText("5")).perform(replaceText("10"));
        onView(withText("Cancel")).perform(click());
        onView(withText("Repeat Notification Period (s)")).perform(click());
        onView(withText("5")).perform(replaceText("10"));
        onView(withText("OK")).perform(click());
        onView(withText("Repeat Notification Period (s)")).perform(click());
        isDisplayed().matches(withText("10"));
        onView(withText("10")).perform(replaceText("5"));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testFPSEstimatorPref() {
        onData(PreferenceMatchers.withTitleText("Recorded Video FPS Estimator")).perform(click());
        clickMenu("Approximate Seconds");
        onView(withText("Recorded Video FPS Estimator")).perform(scrollTo(), click());
        clickMenu("Absolute Frame Count");
    }

    @Test
    public void testAbsoluteFrameNumberPref() {
        onData(PreferenceMatchers.withTitleText("Absolute Frame Number")).perform(click());
        onView(withText("100")).perform(replaceText("300"));
        onView(withText("Cancel")).perform(click());
        onView(withText("Absolute Frame Number")).perform(click());
        onView(withText("100")).perform(replaceText("300"));
        onView(withText("OK")).perform(click());
        onView(withText("Absolute Frame Number")).perform(click());
        isDisplayed().matches(withText("300"));
        onView(withText("300")).perform(replaceText("100"));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testApproxSecsPref() {
        onData(PreferenceMatchers.withTitleText("Approximate Video Seconds")).perform(click());
        onView(withText("30")).perform(replaceText("120"));
        onView(withText("Cancel")).perform(click());
        onView(withText("Approximate Video Seconds")).perform(click());
        onView(withText("30")).perform(replaceText("120"));
        onView(withText("OK")).perform(click());
        onView(withText("Approximate Video Seconds")).perform(click());
        isDisplayed().matches(withText("120"));
        onView(withText("120")).perform(replaceText("30"));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testConfidenceScorePref() {
        onData(PreferenceMatchers.withTitleText("Minimum Object Confidence Score")).perform(click());
        onView(withText("0.6")).perform(replaceText("0.9"));
        onView(withText("Cancel")).perform(click());
        onView(withText("Minimum Object Confidence Score")).perform(click());
        onView(withText("0.6")).perform(replaceText("0.9"));
        onView(withText("OK")).perform(click());
        onView(withText("Minimum Object Confidence Score")).perform(click());
        isDisplayed().matches(withText("0.9"));
        onView(withText("0.9")).perform(replaceText("0.6"));
        onView(withText("OK")).perform(click());
    }
}