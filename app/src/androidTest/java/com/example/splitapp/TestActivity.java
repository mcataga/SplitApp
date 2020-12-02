package com.example.splitapp;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.allOf;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestActivity {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void loginTest() {

        onView(withId(R.id.btnRegisterActivity)).check(matches(isDisplayed()));
        onView(withId(R.id.btnRegisterActivity)).perform(click());
        onView(withId(R.id.btnLoginActivity)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLoginActivity)).perform(click());
        onView(withId(R.id.loginEmailAddress)).check(matches(isDisplayed()));
        onView(withId(R.id.loginEmailAddress)).perform(click());
        onView(withId(R.id.loginEmailAddress)).perform(replaceText("manveergg@gmail.com"));
        onView(withId(R.id.loginPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPassword)).perform(click());
        onView(withId(R.id.loginPassword)).perform(replaceText("testtest"));
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createActivityTest() {
        loginTest();
        onView(withId(R.id.addActivityItem)).check(matches(isDisplayed()));
        onView(withId(R.id.addActivityItem)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.activityName)).check(matches(isDisplayed()));
        onView(withId(R.id.activityName)).perform(click());
        onView(withId(R.id.activityName)).perform(replaceText("Test Activity"));
        onView(withId(R.id.btnAddActivity)).check(matches(isDisplayed()));
        onView(withId(R.id.btnAddActivity)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerActivityItem)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerActivityItem)).perform(click());
    }
    @Test
    public void goIntoActivityTest() {
        loginTest();
        onView(withId(R.id.recyclerActivityItem)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerActivityItem)).perform(click());
        onView(withId(R.id.recyclerActivityItem))
                .perform(actionOnItemAtPosition(0, click()));
    }
    @Test
    public void createBill() {
        goIntoActivityTest();
        onView(withId(R.id.activityDetailBtnBill)).check(matches(isDisplayed()));
        onView(withId(R.id.activityDetailBtnBill)).perform(click());
        onView(withId(R.id.editBillName)).check(matches(isDisplayed()));
        onView(withId(R.id.editBillName)).perform(click());
        onView(withId(R.id.editBillName)).perform(replaceText("Test Bill"));
        Espresso.pressBack();
        onView(withId(R.id.btnSaveNewBill)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSaveNewBill)).perform(click());
        onView(withId(R.id.recyclerItems))
                .perform(actionOnItemAtPosition(0, click()));
    }
}