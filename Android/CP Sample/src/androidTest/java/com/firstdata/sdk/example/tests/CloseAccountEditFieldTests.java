package com.firstdata.sdk.example.tests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static com.firstdata.sdk.example.GenericTestUtils.clickNextButton;
import static com.firstdata.sdk.example.GenericTestUtils.scrollToAndFillField;
import static com.firstdata.sdk.example.GenericTestUtils.waitFor;
import static com.firstdata.sdk.example.TestUtils.CP_CLOSE_ACCOUNT_EDIT_FIELD;
import static com.firstdata.sdk.example.TestUtils.TIMEOUT;
import static com.firstdata.sdk.example.TestUtils.assertAndDismissErrorDialog;
import static com.firstdata.sdk.example.TestUtils.pickFlow;
import static com.firstdata.sdk.example.TestUtils.waitForSuccessCase;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class CloseAccountEditFieldTests {

    @Rule
    public ActivityTestRule<ConfigListActivity> mActivityTestRule = new ActivityTestRule<>(ConfigListActivity.class);

    @Test
    public void Success() throws InterruptedException {
        pickFlow(CP_CLOSE_ACCOUNT_EDIT_FIELD);
        waitFor(withTagValue(is("reason")),TIMEOUT);
        scrollToAndFillField("accountNumber","1234");
        scrollToAndFillField("reason","hate");
        clickNextButton();
        waitForSuccessCase();
    }

    @Test
    public void NoReason() throws InterruptedException {
        pickFlow(CP_CLOSE_ACCOUNT_EDIT_FIELD);
        waitFor(withTagValue(is("reason")),TIMEOUT);
        scrollToAndFillField("accountNumber","1234");
        clickNextButton();
        waitForSuccessCase();
    }

    @Test
    public void NoAccountNumber() throws InterruptedException {
        pickFlow(CP_CLOSE_ACCOUNT_EDIT_FIELD);
        waitFor(withTagValue(is("reason")),TIMEOUT);
        scrollToAndFillField("reason","hate");
        clickNextButton();
        assertAndDismissErrorDialog();
    }
}
