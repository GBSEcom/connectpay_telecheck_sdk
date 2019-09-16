package com.firstdata.sdk.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.firstdata.cpsdk.external.*
import com.firstdata.sdk.BundleKey
import com.firstdata.sdk.sample.utility.API_SECRET
import com.firstdata.sdk.sample.utility.APP_WEB_BASE_URI
import com.firstdata.sdk.sample.utility.SUBSCRIBER_ID
import com.firstdata.sdk.sample.utility.showSDKErrorAlert
import com.firstdata.util.utils.showDialog
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
                showStatusAlert("CP-SDK Error", sdkError.code.errorMessage)
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
        var str = ""
        loadWebView()
        data?.extras?.let {
            if (it.containsKey(BundleKey.ERROR))
                str = it.getString(BundleKey.ERROR) ?: "No Response"

            if (it.containsKey(BundleKey.RESULT))
                str = it.getString(BundleKey.RESULT) ?: "No Response"
            showStatusAlert("CP-SDK Response", str)
        }



//        parseOnActivityResult(requestCode, resultCode, data)?.also { cpsdkResult ->
//            showStatusAlert(cpsdkResult, data)
//        }
    }

    fun showStatusAlert(title: String, message: kotlin.String) {
        showDialog(title, message)
    }

    fun loadWebView() {
        webView.visibility = View.VISIBLE
        webView.clearCache(true)
        webView.clearHistory()
        webView.loadUrl(APP_WEB_BASE_URI)
    }


    private fun showStatusAlert(aResult: CPSDKResult, data: Intent?) {
        showSDKErrorAlert(this, aResult, data)
        loadWebView()
    }

    override fun onResume() {
        super.onResume()
        progressView.visibility = View.GONE
    }
}
