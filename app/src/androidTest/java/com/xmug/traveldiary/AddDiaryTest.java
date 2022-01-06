package com.xmug.traveldiary;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddDiaryTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addDiaryTest() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btn_add_diary)).check(matches(isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.img_gallery),
                childAtPosition(childAtPosition(withId(R.id.recycler_gallery), 0), 0), isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.edt_title),
                childAtPosition(childAtPosition(withId(R.id.recycler_edit), 0), 0), isDisplayed())).perform(replaceText("Milano"), closeSoftKeyboard());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.edt_date),
                childAtPosition(childAtPosition(withId(R.id.recycler_edit), 0), 1), isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(android.R.id.button1), withText("確定"),
                childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3))).perform(scrollTo(), click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.imgBtn_weather),
                childAtPosition(childAtPosition(withId(R.id.recycler_edit), 0), 2), isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.img_windy),
                childAtPosition(allOf(withId(R.id.layout_weather), childAtPosition(withId(R.id.weather_doalog), 0)), 4), isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.tv_place), withText("Where am I ?"),
                childAtPosition(childAtPosition(withId(R.id.recycler_edit), 0), 7), isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.places_autocomplete_search_input),
                childAtPosition(allOf(withId(R.id.autocomplete_fragment), childAtPosition(withId(R.id.card_place), 0)), 1), isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.places_autocomplete_edit_text),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")), 0), 1),
                isDisplayed())).perform(replaceText("milano"), closeSoftKeyboard());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.places_autocomplete_list),
                childAtPosition(withClassName(is("android.widget.LinearLayout")), 2))).perform(actionOnItemAtPosition(0, click()));


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.edt_tags),
                childAtPosition(childAtPosition(withId(R.id.recycler_edit), 0), 10), isDisplayed())).perform(replaceText("italy "), closeSoftKeyboard());;


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.edt_content),
                childAtPosition(childAtPosition(withId(R.id.recycler_edit), 0), 8), isDisplayed())).perform(replaceText("I like it"), closeSoftKeyboard());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.btn_done), withText("Done"), childAtPosition(allOf(withId(R.id.toolbar),
                        childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)), 1), isDisplayed())).perform(click());


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(R.id.imgBtn_back), childAtPosition(allOf(withId(R.id.toolbar),
                        childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)), 4), isDisplayed())).perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
