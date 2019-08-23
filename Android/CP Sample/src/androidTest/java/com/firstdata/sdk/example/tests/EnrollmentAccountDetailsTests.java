package com.firstdata.sdk.example.tests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.firstdata.sdk.example.TestUtils.CP_ENROLLMENT_ACCOUNT_DETAILS;
import static com.firstdata.sdk.example.TestUtils.accountConfirmMissing;
import static com.firstdata.sdk.example.TestUtils.accountConfirmWrong;
import static com.firstdata.sdk.example.TestUtils.allFieldsSuccess;
import static com.firstdata.sdk.example.TestUtils.diversStateMissing;
import static com.firstdata.sdk.example.TestUtils.emailWrong;
import static com.firstdata.sdk.example.TestUtils.oneAtATimeSuccess;
import static com.firstdata.sdk.example.TestUtils.phoneWrong;
import static com.firstdata.sdk.example.TestUtils.pickFlow;
import static com.firstdata.sdk.example.TestUtils.postalCodeWrong;
import static com.firstdata.sdk.example.TestUtils.routingConfirmMissing;
import static com.firstdata.sdk.example.TestUtils.routingConfirmWrong;
import static com.firstdata.sdk.example.TestUtils.routingWrong;
import static com.firstdata.sdk.example.TestUtils.ssnWrong;
import static com.firstdata.sdk.example.TestUtils.termsConfirmPinMissing;
import static com.firstdata.sdk.example.TestUtils.termsConfirmPinWrong;
import static com.firstdata.sdk.example.TestUtils.termsFieldsWrong;
import static com.firstdata.sdk.example.TestUtils.termsOneAtATimeSuccess;

@RunWith(AndroidJUnit4.class)
public class EnrollmentAccountDetailsTests {

    @Rule
    public ActivityTestRule<ConfigListActivity> mActivityTestRule = new ActivityTestRule<>(ConfigListActivity.class);

    @Test
    public void TermsOneAtATimeSuccess() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        termsOneAtATimeSuccess();
    }

    @Test
    public void TermsFieldsWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        termsFieldsWrong();
    }

    @Test
    public void TermsConfirmPinWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        termsConfirmPinWrong();
    }

    @Test
    public void TermsConfirmPinMissing() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        termsConfirmPinMissing();
    }

    @Test
    public void AllFieldsSuccess() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        allFieldsSuccess();
    }

    @Test
    public void OneAtATimeSuccess() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        oneAtATimeSuccess();
    }

    @Test
    public void RoutingWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        routingWrong();
    }

    @Test
    public void AccountConfirmMissing() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        accountConfirmMissing();
    }

    @Test
    public void RoutingConfirmMissing() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        routingConfirmMissing();
    }

    @Test
    public void AccountConfirmWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        accountConfirmWrong();
    }

    @Test
    public void RoutingConfirmWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        routingConfirmWrong();
    }

    @Test
    public void EmailWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        emailWrong();
    }

    @Test
    public void PhoneWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        phoneWrong();
    }

    @Test
    public void PostalCodeWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        postalCodeWrong();
    }

    @Test
    public void DriversStateMissing() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        diversStateMissing();
    }

    @Test
    public void SSNWrong() throws InterruptedException {
        pickFlow(CP_ENROLLMENT_ACCOUNT_DETAILS);
        ssnWrong();
    }
}
