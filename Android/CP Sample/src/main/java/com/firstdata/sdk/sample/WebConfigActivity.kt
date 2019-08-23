package com.firstdata.sdk.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.firstdata.cpsdk.external.*
import com.firstdata.cpsdk.utils.DialogUtils
import com.firstdata.cpsdk.utils.DialogUtils.Status.*
import com.firstdata.sdk.BundleKey
import com.firstdata.sdk.sample.utility.API_SECRET
import com.firstdata.sdk.sample.utility.APP_WEB_BASE_URI
import com.firstdata.sdk.sample.utility.SUBSCRIBER_ID
import com.firstdata.sdk.sample.utility.getErrorMsgFromCode
import com.firstdata.util.utils.FDLogger
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_web.*


class WebConfigActivity : AppCompatActivity() {

    internal lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gson = Gson()
        setContentView(R.layout.activity_web)
        initWebView()
    }

    private fun initWebView() {
        webView.apply {
            loadUrl(APP_WEB_BASE_URI)
            WebView.setWebContentsDebuggingEnabled(true)
            settings.javaScriptEnabled = true
            settings.blockNetworkImage = false
            settings.loadsImagesAutomatically = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE

            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    this@apply.evaluateJavascript("sdkClient.nativeInit()", null)
                }

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if ("start" == request.url.host) {
                        Log.d("load", request.url.toString())
                        try {
                            gson.fromJson(request.url.getQueryParameter("config"), WebConfiguration::class.java).apply {
                                encryptionKey = encryptionKey.replace(" ", "+")
                                configId = configId.split("/").last()
                            }.also {
                                Log.d("load", it.toString())
                                if (BuildConfig.FLAVOR == "qa") {
                                    this@apply.visibility = View.GONE
                                    progressView.visibility = View.VISIBLE
                                    handleWebConfiguration(it)
                                } else
                                    finish()
                            }
                        } catch (ex: Exception) {
                            Toast.makeText(this@WebConfigActivity, "Invalid Input! ", Toast.LENGTH_LONG).show()
                        }
                    }
                    return true
                }
            }
        }

    }

    fun handleWebConfiguration(webConfiguration: WebConfiguration): Boolean {
        val cpsdk = CPSDK(
                webConfiguration.apiKey,
                when (webConfiguration.env) {
                    "prod" -> Environment.PROD
                    "cat", "int" -> Environment.CAT
                    else -> Environment.SANDBOX
                },
                application,
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
                workflow.start(this@WebConfigActivity)
            }

            override fun onError(sdkError: CPSDKError) {
                Log.d("onError:", "errorCode: " + sdkError.code.errorCode + " -- errorMessage: " + sdkError.code.errorMessage + ";; If any additionalMsg:: " + sdkError.message)
                progressView.visibility = View.GONE
                showStatusAlert(sdkError.code.errorMessage, ERROR)
            }
        }

        when (webConfiguration.rel.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]) {

            "ManualDeposit", "ManualDepositWithAccountField" -> cpsdk.manualDeposit(
                    cpConfiguration,
                    gson.fromJson(extraParams, AccountValidationRequest::class.java),
                    configurationCallback as ConfigurationCallback<ManualDeposit>)

            "ManualOnly" -> cpsdk.manualEnrollment(
                    cpConfiguration,
                    gson.fromJson(extraParams, EnrollmentRequest::class.java),
                    configurationCallback as ConfigurationCallback<ManualEnrollment>)

//            "ManualDeposit" -> cpsdk.manualEnrollment(
//                    cpConfiguration,
//                    gson.fromJson(extraParams, EnrollmentRequest::class.java),
//                    configurationCallback as ConfigurationCallback<ManualEnrollment>)

            //   TODO: Not sure this settings exists in code
            "EnrollmentAccountDetails" -> cpsdk.manualEnrollment(
                    cpConfiguration,
                    gson.fromJson(extraParams, EnrollmentRequest::class.java),
                    configurationCallback as ConfigurationCallback<ManualEnrollment>)

            "EnrollmentBothOption", "EnrollmentOption" -> cpsdk.bothEnrollment(
                    cpConfiguration,
                    gson.fromJson(extraParams, EnrollmentRequest::class.java),
                    configurationCallback as ConfigurationCallback<BothEnrollment>)

            "CloseAccount", "CloseAccountWithAccountField" -> cpsdk.closeAccount(
                    cpConfiguration,
                    gson.fromJson(extraParams, CloseAccountRequest::class.java),
                    configurationCallback as ConfigurationCallback<CloseAccount>)

            "UpdateEnrollment" -> cpsdk.updateEnrollment(
                    cpConfiguration,
                    gson.fromJson(extraParams, EnrollmentRequest::class.java),
                    configurationCallback as ConfigurationCallback<UpdateEnrollment>)

            "AccountValidations" -> cpsdk.accountValidation(
                    cpConfiguration,
                    gson.fromJson(extraParams, AccountValidationRequest::class.java),
                    configurationCallback as ConfigurationCallback<AccountValidation>)

            "BankOnly" -> cpsdk.pwmbEnrollment(
                    cpConfiguration,
                    gson.fromJson(extraParams, EnrollmentRequest::class.java),
                    configurationCallback as ConfigurationCallback<PWMBEnrollment>)

            else -> Toast.makeText(this@WebConfigActivity, "No use case for this REL", Toast.LENGTH_LONG).show()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        parseStatusResponse(requestCode, resultCode, this, data)
    }

    private fun parseStatusResponse(requestCode: Int, resultCode: Int, aContext: Context, data: Intent?) {

        var lSDKResponseJson = ""
        var lStatus = ERROR
        data?.extras?.let {
            if (it.containsKey(BundleKey.ERROR)) {
                lSDKResponseJson = it.getString(BundleKey.ERROR) ?: "No Response"
            }

            if (it.containsKey(BundleKey.RESULT)) {
                lSDKResponseJson = it.getString(BundleKey.RESULT) ?: "No Response"
            }
        }

        FDLogger.d(FDLogger.TEMP_DEBUG_TAG, "Received Response [$lSDKResponseJson]")

        if (TextUtils.isEmpty(lSDKResponseJson)) {
            parseOnActivityResult(requestCode, resultCode, data)?.also { aResult ->
                aResult.result?.also {
                    if (aResult?.result?.transactionStatus?.toLowerCase().equals("success")) {
                        lStatus = SUCCESS
                    } else if (aResult?.result?.transactionStatus?.toLowerCase().equals("error")) {
                        lStatus = ERROR
                    } else {
                        lStatus = WARNING
                    }
                    aResult.result?.responseVerbiage?.let {
                        lSDKResponseJson = aResult.result!!.responseVerbiage.toString()
                    }
                    //message = aResult.result?.transactionStatus.toString()
                    lSDKResponseJson += "\n\n" + aResult.result.toString()
                }?.errorDetails?.isNotEmpty()?.let {
                    lSDKResponseJson = (aResult.result?.errorDetails?.get(0)?.toString()
                            ?: "") + "\n\n" + lSDKResponseJson
                }

                aResult.error?.run {
                    lStatus = ERROR
                    lSDKResponseJson = getErrorMsgFromCode(aContext, aResult.error?.code)
                }
            }
        }
        showStatusAlert(lSDKResponseJson, lStatus)
    }

    fun showStatusAlert(message: String, aStatus: DialogUtils.Status) {
        DialogUtils.showDialog(this, message, aStatus)
        loadWebView()
    }

    fun loadWebView() {
        webView.visibility = View.VISIBLE
        webView.clearCache(true)
        webView.clearHistory()
        webView.loadUrl(APP_WEB_BASE_URI)
    }

    override fun onResume() {
        super.onResume()
        progressView.visibility = View.GONE
    }
}
