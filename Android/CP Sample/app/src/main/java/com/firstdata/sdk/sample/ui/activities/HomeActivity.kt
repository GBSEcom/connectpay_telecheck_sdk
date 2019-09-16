package com.firstdata.sdk.sample.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.WindowManager
import com.firstdata.cpsdk.external.CPSDKResult
import com.firstdata.cpsdk.external.parseOnActivityResult
import com.firstdata.sdk.sample.R
import com.firstdata.sdk.sample.adapter.HomePageAdapter
import com.firstdata.sdk.sample.ui.view.FDProgressView
import com.firstdata.sdk.sample.utility.MessageID
import com.firstdata.sdk.sample.utility.UIEvent
import com.firstdata.sdk.sample.utility.getProfileConfigurationFromAsset
import com.firstdata.sdk.sample.utility.showSDKErrorAlert
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_default_tool_bar.*
import org.greenrobot.eventbus.EventBus
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
                System.out.println("---------------> Page $pageIdx Selected<---------------------")
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

        parseOnActivityResult(requestCode, resultCode, data)?.also { cpsdkResult ->
            showStatusAlert(cpsdkResult);
        }
    }

    private fun showStatusAlert(aResult: CPSDKResult) {
        showSDKErrorAlert(this, aResult, null)
        fragmentPager.currentItem = 1
    }
}