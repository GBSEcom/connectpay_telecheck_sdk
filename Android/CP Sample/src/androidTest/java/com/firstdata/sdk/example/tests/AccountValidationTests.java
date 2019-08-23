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
import static com.firstdata.sdk.example.TestUtils.CP_ACCOUNT_VALIDATION;
import static com.firstdata.sdk.example.TestUtils.TIMEOUT;
import static com.firstdata.sdk.example.TestUtils.assertAndDismissErrorDialog;
import static com.firstdata.sdk.example.TestUtils.pickFlow;
import static com.firstdata.sdk.example.TestUtils.waitForSuccessCase;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class AccountValidationTests {

    @Rule
    public ActivityTestRule<ConfigListActivity> mActivityTestRule = new ActivityTestRule<>(ConfigListActivity.class);

    @Test
    public void Success() throws InterruptedException {
        pickFlow(CP_ACCOUNT_VALIDATION);
        waitFor(withTagValue(is("connectpayPaymentNumber")),TIMEOUT);
        scrollToAndFillField("connectpayPaymentNumber","4111111111111111");
        scrollToAndFillField("pin","1234");
        clickNextButton();
        waitForSuccessCase();
    }

    @Test
    public void NumberMissing() throws InterruptedException {
        pickFlow(CP_ACCOUNT_VALIDATION);
        waitFor(withTagValue(is("connectpayPaymentNumber")),TIMEOUT);
        scrollToAndFillField("pin","1234");
        clickNextButton();
        assertAndDismissErrorDialog();
    }

    @Test
    public void PinMissing() throws InterruptedException {
        pickFlow(CP_ACCOUNT_VALIDATION);
        waitFor(withTagValue(is("connectpayPaymentNumber")),TIMEOUT);
        scrollToAndFillField("connectpayPaymentNumber","4111111111111111");
        clickNextButton();
        assertAndDismissErrorDialog();
    }

    @Test
    public void PinWrong() throws InterruptedException {
        pickFlow(CP_ACCOUNT_VALIDATION);
        waitFor(withTagValue(is("connectpayPaymentNumber")),TIMEOUT);
        scrollToAndFillField("connectpayPaymentNumber","4111111111111111");
        scrollToAndFillField("pin","1");
        clickNextButton();
        assertAndDismissErrorDialog();
    }
}
