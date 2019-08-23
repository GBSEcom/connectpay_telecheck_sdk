package com.firstdata.sdk.example.tests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.firstdata.sdk.example.TestUtils.CP_MANUAL_ONLY;
import static com.firstdata.sdk.example.TestUtils.accountConfirmMissing;
import static com.firstdata.sdk.example.TestUtils.accountConfirmWrong;
import static com.firstdata.sdk.example.TestUtils.allFieldsSuccess;
import static com.firstdata.sdk.example.TestUtils.diversStateMissing;
import static com.firstdata.sdk.example.TestUtils.emailWrong;
import static com.firstdata.sdk.example.TestUtils.oneAtATimeSuccess;
import static com.firstdata.sdk.example.TestUtils.phoneWrong;
import static com.firstdata.sdk.example.TestUtils.pickFlow;
import static com.firstdata.sdk.example.TestUtils.pickManual;
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
public class ManualOnlyTests {

    @Rule
    public ActivityTestRule<ConfigListActivity> mActivityTestRule = new ActivityTestRule<>(ConfigListActivity.class);

    @Test
    public void TermsOneAtATimeSuccess() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        termsOneAtATimeSuccess();
    }

    @Test
    public void TermsFieldsWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        termsFieldsWrong();
    }

    @Test
    public void TermsConfirmPinWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        termsConfirmPinWrong();
    }

    @Test
    public void TermsConfirmPinMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        termsConfirmPinMissing();
    }

    @Test
    public void AllFieldsSuccess() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        allFieldsSuccess();
    }

    @Test
    public void OneAtATimeSuccess() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        oneAtATimeSuccess();
    }

    @Test
    public void RoutingWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        routingWrong();
    }

    @Test
    public void AccountConfirmMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        accountConfirmMissing();
    }

    @Test
    public void RoutingConfirmMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        routingConfirmMissing();
    }

    @Test
    public void AccountConfirmWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        accountConfirmWrong();
    }

    @Test
    public void RoutingConfirmWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        routingConfirmWrong();
    }

    @Test
    public void EmailWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        emailWrong();
    }

    @Test
    public void PhoneWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        phoneWrong();
    }

    @Test
    public void PostalCodeWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        postalCodeWrong();
    }

    @Test
    public void DriversStateMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        diversStateMissing();
    }

    @Test
    public void SSNWrong() throws InterruptedException {
        pickFlow(CP_MANUAL_ONLY);
        pickManual();
        ssnWrong();
    }
}
