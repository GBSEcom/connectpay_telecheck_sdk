package com.firstdata.sdk.example.tests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.firstdata.sdk.example.TestUtils.CP_MANUAL_DEPOSIT;
import static com.firstdata.sdk.example.TestUtils.assertAndDismissErrorDialog;
import static com.firstdata.sdk.example.TestUtils.fillCPManualDeposit;
import static com.firstdata.sdk.example.TestUtils.fillCPManualDepositFirstMissing;
import static com.firstdata.sdk.example.TestUtils.fillCPManualDepositSecondMissing;
import static com.firstdata.sdk.example.TestUtils.pickFlow;
import static com.firstdata.sdk.example.TestUtils.waitForSuccessCase;

@RunWith(AndroidJUnit4.class)
public class ManualDepositTests {

    @Rule
    public ActivityTestRule<ConfigListActivity> mActivityTestRule = new ActivityTestRule<>(ConfigListActivity.class);

    @Test
    public void Success() throws InterruptedException {
        pickFlow(CP_MANUAL_DEPOSIT);
        fillCPManualDeposit();
        waitForSuccessCase();
    }

    @Test
    public void FirstMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_DEPOSIT);
        fillCPManualDepositFirstMissing();
        assertAndDismissErrorDialog();
    }

    @Test
    public void SecondMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_DEPOSIT);
        fillCPManualDepositSecondMissing();
        assertAndDismissErrorDialog();
    }
}
