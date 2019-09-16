package com.firstdata.sdk.sample.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.firstdata.cpsdk.CPSDKErrorCode
import com.firstdata.cpsdk.external.*
import com.firstdata.sdk.BundleKey
import com.firstdata.sdk.sample.R
import com.firstdata.sdk.sample.WebConfiguration
import com.firstdata.sdk.sample.ui.fragment.UseCaseConfigFragment
import com.firstdata.sdk.sample.utility.MessageID.MSG_LOAD_LAUNCH_CONFIG_SCREEN
import com.firstdata.util.utils.showDialog
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.use_case_dashboard_item.view.*


val cloneSDKTestProfileConfig = fun(mDefaultProfile: SDKTestProfileConfig): SDKTestProfileConfig {
    return SDKTestProfileConfig(mDefaultProfile!!.fdAccountID,
            mDefaultProfile!!.qaEndPoint,
            mDefaultProfile!!.apiKey,
            mDefaultProfile!!.secretKey,
            mDefaultProfile!!.fdCustomerID,
            mDefaultProfile!!.catEndPoint,
            mDefaultProfile.environment,
            mDefaultProfile.publicKey)
}

fun configureDashItem(
        aListener: View.OnClickListener,
        lView: View,
        titleTxt1: Int,
        titleImg1: Int,
        titleTxt2: Int,
        titleImg2: Int,
        titleTxt3: Int,
        titleImg3: Int) {

    if (-1 == titleTxt1 && -1 == titleImg1 && -1 == titleTxt2 && -1 == titleImg2
            && -1 == titleTxt3 && -1 == titleImg3) {
        lView.visibility = View.GONE
        return
    }

    val lContainer1 = lView.col_1_container
    lContainer1.visibility = View.VISIBLE

    if (-1 != titleTxt1 && -1 != titleImg1) {
        lContainer1.tag = titleTxt1
        lContainer1.setOnClickListener(aListener)
        val lBtn1 = lView.btn_1
        lBtn1.setText(titleTxt1)
        val lImg1 = lView.img_view_1
        lImg1.setImageResource(titleImg1)
        lImg1.bringToFront()
    } else {
        lContainer1.visibility = View.INVISIBLE
    }

    val lContainer2 = lView.col_2_container
    lContainer2.visibility = View.VISIBLE
    if (-1 != titleTxt2 && -1 != titleImg2) {
        lContainer2.tag = titleTxt2
        lContainer2.setOnClickListener(aListener)
        val lBtn2 = lView.btn_2
        lBtn2.setText(titleTxt2)
        val lImg2 = lView.img_view_2
        lImg2.setImageResource(titleImg2)
        lImg2.bringToFront()
    } else {
        lContainer2.visibility = View.INVISIBLE
    }

    val lContainer3 = lView.col_3_container
    lContainer3.visibility = View.VISIBLE
    if (-1 != titleTxt3 && -1 != titleImg3) {
        lContainer3.tag = titleTxt3
        lContainer3.setOnClickListener(aListener)
        val lBtn3 = lView.btn_3
        lBtn3.setText(titleTxt3)
        val lImg3 = lView.img_view_3
        lImg3.setImageResource(titleImg3)
        lImg3.bringToFront().let { }
    } else {
        lContainer3.visibility = View.INVISIBLE
    }
}

fun launchSDK(aContext: AppCompatActivity, aConfig: SDKTestProfileConfig,
              aUIConfig: UseCaseConfigFragment.UIHpLink?) {

    val lConfig = WebConfiguration(aConfig.environment, aConfig.apiKey, aConfig.fdCustomerID,
            SESSION_TOKEN, aConfig.publicKey, aUIConfig?.pageID!!,
            aUIConfig?.useCaseName.getValue(), aUIConfig.extraParams, aUIConfig.postURL.toString())

    handleWebConfiguration(aContext, lConfig)

}

fun handleWebConfiguration(context: AppCompatActivity, webConfiguration: WebConfiguration): Boolean {

    var gson = Gson()

    val cpsdk = CPSDK(
            webConfiguration.apiKey,
            when (webConfiguration.env) {
                "https://api.firstdata.com" -> Environment.PROD
                "https://cat.api.firstdata.com" -> Environment.CAT
                else -> Environment.SANDBOX
            },
            context.application,
            true,
            ACHConfiguration(
                    webConfiguration.apiKey,
                    API_SECRET,
                    SUBSCRIBER_ID,
                    webConfiguration.accessToken)
    )

    val cpConfiguration = CPConfiguration(
            webConfiguration.configId,
            webConfiguration.fdCustomerId,
            webConfiguration.accessToken,
            webConfiguration.encryptionKey,
            webConfiguration.apiKey,
            webConfiguration.postUrl)

    val extraParams = gson.toJson(webConfiguration.extraParams)
    val configurationCallback = object : ConfigurationCallback<Workflow> {
        override fun onSuccess(workflow: Workflow) {
            workflow.start(context)
        }

        override fun onError(sdkError: CPSDKError) {
            Toast.makeText(context, "error " + sdkError.toString(), Toast.LENGTH_LONG).show()
        }
    }

    when (webConfiguration.rel) {

        UseCase.MANUAL_DEPOSIT_WITH_ACC_FIELD.getValue() -> cpsdk.manualDeposit(
                cpConfiguration,
                gson.fromJson(extraParams, AccountValidationRequest::class.java),
                configurationCallback as ConfigurationCallback<ManualDeposit>)

        UseCase.MANUAL_ONLY.getValue(),
        UseCase.MANUAL_DEPOSIT.getValue(),
        UseCase.ENROLL_ACCOUNT_DETAILS.getValue() -> cpsdk.manualEnrollment(
                cpConfiguration,
                gson.fromJson(extraParams, EnrollmentRequest::class.java),
                configurationCallback as ConfigurationCallback<ManualEnrollment>)

        UseCase.ENROLL_BOTH_OPTION_MOBILE.getValue(),
        UseCase.ENROLL_BOTH_OPTION_WEB.getValue(),
        "EnrollmentOption" -> cpsdk.bothEnrollment(
                cpConfiguration,
                gson.fromJson(extraParams, EnrollmentRequest::class.java),
                configurationCallback as ConfigurationCallback<BothEnrollment>)

        UseCase.CLOSE_ACCOUNT.getValue(), "CloseAccountWithAccountField" -> cpsdk.closeAccount(
                cpConfiguration,
                gson.fromJson(extraParams, CloseAccountRequest::class.java),
                configurationCallback as ConfigurationCallback<CloseAccount>)

        UseCase.UPDATE_ENROLLMENT.getValue() -> cpsdk.updateEnrollment(
                cpConfiguration,
                gson.fromJson(extraParams, EnrollmentRequest::class.java),
                configurationCallback as ConfigurationCallback<UpdateEnrollment>)

        UseCase.ACCOUNT_VALIDATION.getValue() -> cpsdk.accountValidation(
                cpConfiguration,
                gson.fromJson(extraParams, AccountValidationRequest::class.java),
                configurationCallback as ConfigurationCallback<AccountValidation>)

        UseCase.BANK_ONLY.getValue() -> cpsdk.pwmbEnrollment(
                cpConfiguration,
                gson.fromJson(extraParams, EnrollmentRequest::class.java),
                configurationCallback as ConfigurationCallback<PWMBEnrollment>)

        else -> Toast.makeText(context, "No use case for this REL", Toast.LENGTH_LONG).show()
    }
    return true
}


fun getProfileConfigurationFromAsset(activity: Activity): SDKTestProfileConfig {
    val fileName = "qa_profile.json"
    val lConfigStr = activity?.application?.assets?.open(fileName)?.bufferedReader().use {
        it?.readText()
    }
    SDK_TEST_PROFILE = Gson().fromJson(lConfigStr!!)
    return SDK_TEST_PROFILE
}

fun showSDKErrorAlert(aContext: Context, aResult: CPSDKResult, data: Intent?) {

    var title: String? = ""
    var message: String? = ""

    aResult?.result?.also {
        title = aContext.getString(R.string.str_common_success)
        //message = aResult.result?.transactionStatus.toString()
        message = aResult.result.toString()
        aResult.result?.responseVerbiage?.let {
            //  message = it
            message = aResult.result.toString()
        }

    }?.errorDetails?.let {
        title = aContext.getString(R.string.str_common_failure)

        //  message = "\nCode : " + it[0]?.errorCode + "\nReason : " + it[0]?.errorReason + "\nField : " + it[0]?.errorField
        // message = it[0]?.errorReason.toString()
        message = aResult.result.toString()
    }

    aResult.error?.run {
        title = aContext.getString(R.string.str_common_failure)
        message = getErrorMsgFromCode(aContext, aResult.error?.code) + message
        // message =   aResult.result.toString()
    }

    data?.extras?.also {

        if (it.containsKey(BundleKey.ERROR))
            title = aContext.getString(R.string.str_common_failure)

        if (it.containsKey(BundleKey.RESULT))
            title = aContext.getString(R.string.str_common_success)

    }
    aContext.showDialog(title ?: "Warning", message ?: "Unknown")
}

fun getErrorMsgFromCode(aContext: Context, aErrorCode: CPSDKErrorCode?): String {
    return if (aErrorCode?.errorMessage != null) aErrorCode?.errorMessage else (CPSDKErrorCode.UNKNOWN.errorMessage.toString())
}

object MessageID {
    const val MSG_GENERATE_TOKEN: Int = 100
    const val MSG_LOAD_PAGES = MSG_GENERATE_TOKEN + 1
    const val MSG_LOAD_LAUNCH_CONFIG_SCREEN = MSG_LOAD_PAGES + 1
}


fun postLaunchConfigScreenEvent(aPrimaryObj: UseCaseConfigFragment.UIHpLink, aUseCase: UseCase) {

    UIEvent.Builder().setEventID(MSG_LOAD_LAUNCH_CONFIG_SCREEN)
            .setEventName("Launch Config Screen")
            .setEventObject(aPrimaryObj as Any)
            .setSubEventObject(aUseCase as Any).buildAndPostSticky()
}


fun postNotifyTokenGeneration() {
    UIEvent.Builder().setEventID(MessageID.MSG_GENERATE_TOKEN)
            .setEventName("Notify Token Generation").buildAndPost()
}

fun postNotifyPagesGeneration() {
    UIEvent.Builder().setEventID(MessageID.MSG_LOAD_PAGES)
            .setEventName("Notify Pages Creation").buildAndPost()
}
