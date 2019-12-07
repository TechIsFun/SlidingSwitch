package com.github.techisfun.slidingswitch.app;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.github.techisfun.slidingswitch.SlidingSwitch;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
public class SlidingswitchActivityTest {

  @Rule
  public ActivityTestRule<SlidingswitchActivity> mActivityTestRule = new ActivityTestRule<>(
      SlidingswitchActivity.class);

  @Test
  public void testDefaultState() throws InterruptedException {
    SlidingswitchActivity activity = mActivityTestRule.getActivity();
    SlidingSwitch slidingSwitch = activity.findViewById(R.id.sliding_switch);

    assertFalse(slidingSwitch.getState());

    onView(withId(R.id.sliding_switch)).perform(swipeRight());
    Thread.sleep(1000);

    assertTrue(slidingSwitch.getState());

    onView(withId(R.id.sliding_switch)).perform(swipeLeft());
    Thread.sleep(1000);

    assertFalse(slidingSwitch.getState());

  }

  @Test
  public void testInvertedState() throws InterruptedException {
    SlidingswitchActivity activity = mActivityTestRule.getActivity();
    SlidingSwitch slidingSwitch = activity.findViewById(R.id.sliding_switch);

    slidingSwitch.setState(true);

    assertTrue(slidingSwitch.getState());

    onView(withId(R.id.sliding_switch)).perform(swipeLeft());
    Thread.sleep(1000);

    assertFalse(slidingSwitch.getState());

    onView(withId(R.id.sliding_switch)).perform(swipeRight());
    Thread.sleep(1000);

    assertTrue(slidingSwitch.getState());

  }


}
