package com.firstdata.sdk.example;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.assertion.ViewAssertions;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

public class GenericTestUtils {

    public static void makePickerSelection(String id,String value) {
        onView(withTagValue(is(id))).perform(scrollTo());
        onView(withTagValue(is(id))).perform(click());
        onView(withText(value)).perform(click());
        onView(withText("OK")).perform(click());
    }

    public static void scrollToAndFillField(String id,String value) {
        onView(withTagValue(is(id))).perform(scrollTo());
        onView(withTagValue(is(id))).perform(typeText(value));
    }

    public static void clickNextButton() {
        onView(withTagValue(is("nextButton"))).perform(scrollTo());
        onView(withTagValue(is("nextButton"))).perform(click());
    }

    public static void makeDateSelection()
    {
        onView(withTagValue(is("dob"))).perform(scrollTo());
        onView(withTagValue(is("dob"))).perform(click());
        onView(withResourceName("month")).perform(swipeUp());
        onView(withResourceName("day")).perform(swipeUp());
        onView(withResourceName("year")).perform(swipeDown());
        onView(withText("OK")).perform(click());
    }

    public static void makeCheckboxSelection(String text)
    {
        onView(withText(text)).perform(scrollTo());
        onView(withText(text)).perform(click());
    }

    public static void waitFor(final Matcher<View> viewMatcher, final long timeout) throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + timeout;
        do {
            try {
                onView(viewMatcher).check(ViewAssertions.matches(isDisplayed()));
                return;
            }
            catch (Exception e){
                Thread.sleep(50);
                if(System.currentTimeMillis() > endTime)
                    throw e;
            }
        }
        while (System.currentTimeMillis() < endTime);
        StringDescription stringDescription = new StringDescription();
        viewMatcher.describeTo(stringDescription);
        throw new PerformException.Builder()
                .withActionDescription("Failed waiting "+timeout+"ms for")
                .withViewDescription(stringDescription.toString())
                .withCause(new TimeoutException())
                .build();
    }

    public static Matcher<View> childAtPosition(
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
