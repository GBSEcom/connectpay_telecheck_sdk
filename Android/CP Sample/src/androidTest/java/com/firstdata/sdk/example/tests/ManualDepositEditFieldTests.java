package com.firstdata.sdk.example.tests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.firstdata.sdk.example.TestUtils.CP_MANUAL_DEPOSIT_EDIT_FIELD;
import static com.firstdata.sdk.example.TestUtils.assertAndDismissErrorDialog;
import static com.firstdata.sdk.example.TestUtils.fillCPManualDepositEditField;
import static com.firstdata.sdk.example.TestUtils.fillCPManualDepositEditFieldAccountNumberMissing;
import static com.firstdata.sdk.example.TestUtils.fillCPManualDepositEditFieldFirstMissing;
import static com.firstdata.sdk.example.TestUtils.fillCPManualDepositEditFieldSecondMissing;
import static com.firstdata.sdk.example.TestUtils.pickFlow;
import static com.firstdata.sdk.example.TestUtils.waitForSuccessCase;

@RunWith(AndroidJUnit4.class)
public class ManualDepositEditFieldTests {

    @Rule
    public ActivityTestRule<ConfigListActivity> mActivityTestRule = new ActivityTestRule<>(ConfigListActivity.class);

    @Test
    public void Success() throws InterruptedException {//
        pickFlow(CP_MANUAL_DEPOSIT_EDIT_FIELD);
        fillCPManualDepositEditField();
        waitForSuccessCase();
    }

    @Test
    public void AccountNumberMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_DEPOSIT_EDIT_FIELD);
        fillCPManualDepositEditFieldAccountNumberMissing();
        assertAndDismissErrorDialog();
    }

    @Test
    public void FirstMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_DEPOSIT_EDIT_FIELD);
        fillCPManualDepositEditFieldFirstMissing();
        assertAndDismissErrorDialog();
    }

    @Test
    public void SecondMissing() throws InterruptedException {
        pickFlow(CP_MANUAL_DEPOSIT_EDIT_FIELD);
        fillCPManualDepositEditFieldSecondMissing();
        assertAndDismissErrorDialog();
    }
}
