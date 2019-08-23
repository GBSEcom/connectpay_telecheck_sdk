package com.firstdata.sdk.sample.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.WindowManager
import com.firstdata.cpsdk.external.parseOnActivityResult
import com.firstdata.cpsdk.utils.DialogUtils
import com.firstdata.sdk.BundleKey
import com.firstdata.sdk.sample.R
import com.firstdata.sdk.sample.adapter.HomePageAdapter
import com.firstdata.sdk.sample.ui.view.FDProgressView
import com.firstdata.sdk.sample.utility.MessageID
import com.firstdata.sdk.sample.utility.UIEvent
import com.firstdata.sdk.sample.utility.getErrorMsgFromCode
import com.firstdata.sdk.sample.utility.getProfileConfigurationFromAsset
import com.firstdata.util.utils.FDLogger
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_default_tool_bar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class HomeActivity : BaseClientActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_home)

        setSupportActionBar(appToolBar)
        title = getString(R.string.app_name)

        getProfileConfigurationFromAsset(this)
        fragmentPager.adapter = HomePageAdapter(this, supportFragmentManager)
        fragmentPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(pageIdx: Int) {
                FDLogger.d(FDLogger.TEMP_DEBUG_TAG, "Page $pageIdx Selected")
                if (!gateState && pageIdx == 2) {
                    fragmentPager.currentItem = 1
                } else {
                    gateState = false
                }
            }

        })
    }

    var gateState: Boolean = false
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(aUIEvent: UIEvent) {

        val lEventID = aUIEvent.eventID

        if (lEventID == MessageID.MSG_GENERATE_TOKEN) {
            FDProgressView.hideProgressBar()
            fragmentPager.currentItem = 1
        } else if (lEventID == MessageID.MSG_LOAD_LAUNCH_CONFIG_SCREEN) {
            gateState = true
            fragmentPager.currentItem = 2
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        parseStatusResponse(requestCode, resultCode, this, data)
        fragmentPager.currentItem = 1
    }

    private fun parseStatusResponse(requestCode: Int, resultCode: Int, aContext: Context, data: Intent?) {

        var lSDKResponseJson = ""
        var lStatus = DialogUtils.Status.ERROR
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
                        lStatus = DialogUtils.Status.SUCCESS
                    } else if (aResult?.result?.transactionStatus?.toLowerCase().equals("error")) {
                        lStatus = DialogUtils.Status.ERROR
                    } else {
                        lStatus = DialogUtils.Status.WARNING
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
                    lStatus = DialogUtils.Status.ERROR
                    lSDKResponseJson = getErrorMsgFromCode(aContext, aResult.error?.code)
                }
            }
        }
        DialogUtils.showDialog(this, lSDKResponseJson, lStatus)
    }

}