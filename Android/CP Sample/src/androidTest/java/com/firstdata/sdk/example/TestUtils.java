package com.firstdata.sdk.example;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.assertion.ViewAssertions;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.firstdata.ucom.hostedpages.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.firstdata.sdk.example.GenericTestUtils.childAtPosition;
import static com.firstdata.sdk.example.GenericTestUtils.clickNextButton;
import static com.firstdata.sdk.example.GenericTestUtils.makeCheckboxSelection;
import static com.firstdata.sdk.example.GenericTestUtils.makeDateSelection;
import static com.firstdata.sdk.example.GenericTestUtils.makePickerSelection;
import static com.firstdata.sdk.example.GenericTestUtils.scrollToAndFillField;
import static com.firstdata.sdk.example.GenericTestUtils.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class TestUtils {

    public static final int CP_ACCOUNT_VALIDATION = 0;
    public static final int CP_BANK_ONLY = 1;
    public static final int CP_CLOSE_ACCOUNT = 2;
    public static final int CP_CLOSE_ACCOUNT_EDIT_FIELD = 3;
    public static final int CP_ENROLLMENT_ACCOUNT_DETAILS = 4;
    public static final int CP_ENROLLMENT_BOTH_OPTIONS = 5;
    public static final int CP_MANUAL_DEPOSIT = 6;
    public static final int CP_MANUAL_DEPOSIT_EDIT_FIELD = 7;
    public static final int CP_MANUAL_ONLY = 8;
    public static final int CP_UPDATE_ENROLLMENT = 9;

    public static final int TIMEOUT = 10000;

    public static Map<String,String> getCorrectEnrollmentAccountValues(){
        Map<String,String> map = new HashMap<>();
        map.put("routingNumber","123456789");
        map.put("accountNumber","123456789");
        map.put("confirmRoutingNumber","123456789");
        map.put("confirmAccountNumber","123456789");
        map.put("connectpayPaymentNumber","123456789");
        map.put("firstName","matthew");
        map.put("lastName","slade");
        map.put("email","matthew@gyft.com");
        map.put("phone[0].number","6502158810");
        map.put("phone[0].displayMode","MOBILE");
        map.put("phone[1].number","6502158810");
        map.put("phone[1].displayMode","MOBILE");
        map.put("street","153 mercy st");
        map.put("city","mountain view");
        map.put("state","CA");
        map.put("postalCode","94041");
        map.put("country","USA");
        map.put("dob","19870404");
        map.put("memberDate","");
        map.put("securityQuestion1","Question 1");
        map.put("securityQuestion2","Question 1");
        map.put("securityAnswer1","1234");
        map.put("securityAnswer2","1234");
        map.put("newPin","1234");
        map.put("confirmPin","1234");
        map.put("oldPin","1234");
        map.put("gender","M");
        return map;
    }

    public static Map<String,String> addOptionalFields(Map<String,String> map){
        map.put("street2","apartment b");
        map.put("driverLicenseNumber","123456789");
        map.put("driverLicenseState","CA");
        map.put("ssn","123456789");
        return map;
    }

    public static void pickFlow(int pos){
        onView(allOf(
                withId(R.id.recyclerView),
                childAtPosition(
                        withClassName(is("android.widget.LinearLayout")),
                        0)
        )).perform(actionOnItemAtPosition(pos, click()));

        onView(allOf(
                withId(R.id.action_native),
                withText("Launch SDK"),
                isDisplayed())
        ).perform(click());
    }

    public static void pickUpdateFlow(int pos,String tag) throws InterruptedException{
        pickFlow(pos);
        waitFor(withTagValue(is(tag)),TIMEOUT);
        onView(withTagValue(is(tag))).perform(click());
    }

    public static void fillUpdatePersonalInformation(Map<String,String> map) throws InterruptedException {

        waitFor(withTagValue(is("firstName")),TIMEOUT);

        fillValueForKey(map,"firstName");
        fillValueForKey(map,"lastName");
        fillValueForKey(map,"email");
        fillValueForKey(map,"phone[0].number");
        fillValueForKey(map,"phone[1].number");

        clickNextButton();
    }

    public static void fillUpdateBankDetails(Map<String,String> map) throws InterruptedException {

        waitFor(withTagValue(is("routingNumber")),TIMEOUT);

        fillValueForKey(map,"routingNumber");
        fillValueForKey(map,"confirmRoutingNumber");
        fillValueForKey(map,"accountNumber");
        fillValueForKey(map,"confirmAccountNumber");

        clickNextButton();
    }

    public static void fillUpdatePinDetails(Map<String,String> map) throws InterruptedException {

        waitFor(withTagValue(is("oldPin")),TIMEOUT);

        fillValueForKey(map,"oldPin");
        fillValueForKey(map,"newPin");
        fillValueForKey(map,"confirmPin");

        clickNextButton();
    }

    public static void fillUpdateSecurityQuestion(Map<String,String> map) throws InterruptedException {

        waitFor(withTagValue(is("securityQuestion1")),TIMEOUT);

        if(map.containsKey("securityQuestion1"))
            makePickerSelection("securityQuestion1","Question 1");
        fillValueForKey(map,"securityAnswer1");

        if(map.containsKey("securityQuestion2"))
            makePickerSelection("securityQuestion2","Question 1");
        fillValueForKey(map,"securityAnswer2");

        clickNextButton();
    }

    public static void fillUpdateOtherDetails(Map<String,String> map) throws InterruptedException {

        waitFor(withTagValue(is("driverLicenseNumber")),TIMEOUT);

        fillValueForKey(map,"driverLicenseNumber");

        if(map.containsKey("driverLicenseState"))
            makePickerSelection("driverLicenseState","California");

        if(map.containsKey("gender"))
            makePickerSelection("gender","Male");

        clickNextButton();
    }

    public static void pickManual() throws InterruptedException {
        waitFor(withText("DO MANUAL ENTRY INSTEAD"),TIMEOUT);
        onView(withText("DO MANUAL ENTRY INSTEAD")).perform(click());
    }

    public static void fillCPManualDepositEditField() throws InterruptedException {
        waitFor(withTagValue(is("firstDepositedAmount")),TIMEOUT);
        scrollToAndFillField("accountNumber","1234");
        scrollToAndFillField("firstDepositedAmount","123");
        scrollToAndFillField("secondDepositedAmount","123");

        clickNextButton();
    }

    public static void fillCPManualDepositEditFieldAccountNumberMissing() throws InterruptedException {
        waitFor(withTagValue(is("firstDepositedAmount")),TIMEOUT);
        scrollToAndFillField("firstDepositedAmount","123");
        scrollToAndFillField("secondDepositedAmount","123");

        clickNextButton();
    }

    public static void fillCPManualDepositEditFieldFirstMissing() throws InterruptedException {
        waitFor(withTagValue(is("firstDepositedAmount")),TIMEOUT);
        scrollToAndFillField("accountNumber","1234");
        scrollToAndFillField("secondDepositedAmount","123");

        clickNextButton();
    }

    public static void fillCPManualDepositEditFieldSecondMissing() throws InterruptedException {
        waitFor(withTagValue(is("firstDepositedAmount")),TIMEOUT);
        scrollToAndFillField("accountNumber","1234");
        scrollToAndFillField("firstDepositedAmount","123");

        clickNextButton();
    }

    public static void fillCPManualDeposit() throws InterruptedException {
        waitFor(withTagValue(is("firstDepositedAmount")),TIMEOUT);
        scrollToAndFillField("firstDepositedAmount","123");
        scrollToAndFillField("secondDepositedAmount","123");

        clickNextButton();
    }

    public static void fillCPManualDepositFirstMissing() throws InterruptedException {
        waitFor(withTagValue(is("firstDepositedAmount")),TIMEOUT);
        scrollToAndFillField("secondDepositedAmount","123");

        clickNextButton();
    }

    public static void fillCPManualDepositSecondMissing() throws InterruptedException {
        waitFor(withTagValue(is("firstDepositedAmount")),TIMEOUT);
        scrollToAndFillField("firstDepositedAmount","123");

        clickNextButton();
    }

    public static void fillCPEnrollmentAccountDetails(Map<String,String> map) throws InterruptedException {
        waitFor(withTagValue(is("routingNumber")),TIMEOUT);
        fillValueForKey(map,"routingNumber");
        fillValueForKey(map,"confirmRoutingNumber");
        fillValueForKey(map,"accountNumber");
        fillValueForKey(map,"confirmAccountNumber");
        fillValueForKey(map,"connectpayPaymentNumber");
        fillValueForKey(map,"firstName");
        fillValueForKey(map,"lastName");
        fillValueForKey(map,"email");
        fillValueForKey(map,"phone[0].number");
        fillValueForKey(map,"street");
        fillValueForKey(map,"street2");
        fillValueForKey(map,"city");
        fillValueForKey(map,"postalCode");
        fillValueForKey(map, "ssn");
        fillValueForKey(map,"driverLicenseNumber");
        fillValueForKey(map,"securityAnswer1");

        if(map.containsKey("state"))
            makePickerSelection("state","California");

        if(map.containsKey("gender"))
            makePickerSelection("gender","Male");

        if(map.containsKey("dob"))
            makeDateSelection();

        if(map.containsKey("driverLicenseState"))
            makePickerSelection("driverLicenseState","California");

        if(map.containsKey("securityQuestion1"))
            makePickerSelection("securityQuestion1","Question 1");

        clickNextButton();
    }

    public static void fillCPEnrollmentAccountDetailsOneAtATime(Map<String,String> map) throws InterruptedException {
        waitFor(withTagValue(is("routingNumber")),TIMEOUT);
        fillValueForKey(map,"routingNumber");
        clickNextButton();
        assertAndDismissErrorDialog();
        fillValueForKey(map,"confirmRoutingNumber");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"accountNumber");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"confirmAccountNumber");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"connectpayPaymentNumber");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"firstName");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"lastName");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"email");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"phone[0].number");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"street");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"city");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"postalCode");
        clickNextButton();
        assertAndDismissErrorDialog();


        if(map.containsKey("state"))
            makePickerSelection("state","California");
        clickNextButton();
        assertAndDismissErrorDialog();

        if(map.containsKey("gender"))
            makePickerSelection("gender","Male");
        clickNextButton();
        assertAndDismissErrorDialog();

        if(map.containsKey("dob"))
            makeDateSelection();
        clickNextButton();
        assertAndDismissErrorDialog();

        if(map.containsKey("securityQuestion1"))
            makePickerSelection("securityQuestion1","Question 1");
        clickNextButton();
        assertAndDismissErrorDialog();

        fillValueForKey(map,"securityAnswer1");
        clickNextButton();
    }

    public static void assertAndDismissErrorDialog(){
        onView(withText("Please correct the errors in the form and try again")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }

    public static void fillValueForKey(Map<String,String> map, String key){
        if(map.containsKey(key))
            scrollToAndFillField(key,map.get(key));
    }

    public static void waitForSuccessCase() throws InterruptedException {
        waitFor(withText("Select Environment:"),TIMEOUT);
    }

    public static void fillCPEnrollmentTerms() throws InterruptedException {
        waitFor(withText("I accept terms and conditions"),TIMEOUT);
        makeCheckboxSelection("I accept terms and conditions");
        scrollToAndFillField("pin","1234");
        scrollToAndFillField("confirmPin","1234");
        clickNextButton();
    }

    public static void termsOneAtATimeSuccess() throws InterruptedException {
        fillCPEnrollmentAccountDetails(getCorrectEnrollmentAccountValues());
        waitFor(withText("I accept terms and conditions"),TIMEOUT);
        clickNextButton();
        onView(withText("OK")).perform(click());
        onView(withText("OK")).perform(click());
        makeCheckboxSelection("I accept terms and conditions");
        clickNextButton();
        assertAndDismissErrorDialog();
        scrollToAndFillField("pin","1234");clickNextButton();
        assertAndDismissErrorDialog();
        scrollToAndFillField("confirmPin","1234");
        clickNextButton();
        waitForSuccessCase();
    }

    public static void termsFieldsWrong() throws InterruptedException {
        fillCPEnrollmentAccountDetails(getCorrectEnrollmentAccountValues());
        waitFor(withText("I accept terms and conditions"),TIMEOUT);
        makeCheckboxSelection("I accept terms and conditions");
        scrollToAndFillField("pin","123");
        scrollToAndFillField("confirmPin","123");
        clickNextButton();
        assertAndDismissErrorDialog();
    }

    public static void termsConfirmPinWrong() throws InterruptedException {
        fillCPEnrollmentAccountDetails(getCorrectEnrollmentAccountValues());
        waitFor(withText("I accept terms and conditions"),TIMEOUT);
        makeCheckboxSelection("I accept terms and conditions");
        scrollToAndFillField("pin","1234");
        scrollToAndFillField("confirmPin","123");
        clickNextButton();
        assertAndDismissErrorDialog();
    }

    public static void termsConfirmPinMissing() throws InterruptedException {
        fillCPEnrollmentAccountDetails(getCorrectEnrollmentAccountValues());
        waitFor(withText("I accept terms and conditions"),TIMEOUT);
        makeCheckboxSelection("I accept terms and conditions");
        scrollToAndFillField("pin","1234");
        clickNextButton();
        assertAndDismissErrorDialog();
    }

    public static void allFieldsSuccess() throws InterruptedException {
        fillCPEnrollmentAccountDetails(addOptionalFields(getCorrectEnrollmentAccountValues()));
        fillCPEnrollmentTerms();
        waitForSuccessCase();
    }

    public static void oneAtATimeSuccess() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        fillCPEnrollmentAccountDetailsOneAtATime(map);
        fillCPEnrollmentTerms();
        waitForSuccessCase();
    }

    public static void routingWrong() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.put("routingNumber","1");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void accountConfirmMissing() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.remove("confirmAccountNumber");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void routingConfirmMissing() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.remove("confirmRoutingNumber");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void accountConfirmWrong() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.put("confirmAccountNumber","111111111");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }


    public static void routingConfirmWrong() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.put("confirmRoutingNumber","111111111");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void emailWrong() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.put("email","notanemail@");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void phoneWrong() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.put("phone[0].number","123456789");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void postalCodeWrong() throws InterruptedException {
        Map<String,String> map = getCorrectEnrollmentAccountValues();
        map.put("postalCode","1");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void diversStateMissing() throws InterruptedException {
        Map<String,String> map = addOptionalFields(getCorrectEnrollmentAccountValues());
        map.remove("driverLicenseState");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }

    public static void ssnWrong() throws InterruptedException {
        Map<String,String> map = addOptionalFields(getCorrectEnrollmentAccountValues());
        map.put("ssn","1234");
        fillCPEnrollmentAccountDetails(map);
        assertAndDismissErrorDialog();
    }
}
