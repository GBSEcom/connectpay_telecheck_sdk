package com.firstdata.sdk.example.tests;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.firstdata.sdk.example.TestUtils.CP_UPDATE_ENROLLMENT;
import static com.firstdata.sdk.example.TestUtils.addOptionalFields;
import static com.firstdata.sdk.example.TestUtils.fillUpdateBankDetails;
import static com.firstdata.sdk.example.TestUtils.fillUpdateOtherDetails;
import static com.firstdata.sdk.example.TestUtils.fillUpdatePersonalInformation;
import static com.firstdata.sdk.example.TestUtils.fillUpdatePinDetails;
import static com.firstdata.sdk.example.TestUtils.fillUpdateSecurityQuestion;
import static com.firstdata.sdk.example.TestUtils.getCorrectEnrollmentAccountValues;
import static com.firstdata.sdk.example.TestUtils.pickUpdateFlow;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UpdateEnrollmentTests {

    @Rule
    public ActivityTestRule<ConfigListActivity> mActivityTestRule = new ActivityTestRule<>(ConfigListActivity.class);

    @Test
    public void PersonalInformationSuccess() throws InterruptedException {
        pickUpdateFlow(CP_UPDATE_ENROLLMENT,"CPUpdatePersonalInformationWidget");
        fillUpdatePersonalInformation(getCorrectEnrollmentAccountValues());
    }

    @Test
    public void BankDetailsSuccess() throws InterruptedException {
        pickUpdateFlow(CP_UPDATE_ENROLLMENT,"CPUpdateBankDetailsWidget");
        fillUpdateBankDetails(getCorrectEnrollmentAccountValues());
    }

    @Test
    public void PinSuccess() throws InterruptedException {
        pickUpdateFlow(CP_UPDATE_ENROLLMENT,"CPUpdatePinWidget");
        fillUpdatePinDetails(getCorrectEnrollmentAccountValues());
    }

    @Test
    public void SecurityQuestionSuccess() throws InterruptedException {
        pickUpdateFlow(CP_UPDATE_ENROLLMENT,"CPUpdateSecurityQuestionWidget");
        fillUpdateSecurityQuestion(getCorrectEnrollmentAccountValues());
    }

    @Test
    public void OtherDetailsSuccess() throws InterruptedException {
        pickUpdateFlow(CP_UPDATE_ENROLLMENT,"CPUpdateOtherDetailsWidget");
        fillUpdateOtherDetails(addOptionalFields(getCorrectEnrollmentAccountValues()));
    }

}
