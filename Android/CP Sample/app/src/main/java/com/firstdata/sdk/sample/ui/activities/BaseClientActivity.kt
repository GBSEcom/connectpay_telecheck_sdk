package com.firstdata.sdk.sample.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firstdata.sdk.sample.utility.APPLICATION
import org.greenrobot.eventbus.EventBus

open class BaseClientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APPLICATION?.appComponent?.inject(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}