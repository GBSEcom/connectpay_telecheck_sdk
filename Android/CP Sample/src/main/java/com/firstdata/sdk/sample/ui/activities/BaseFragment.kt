package com.firstdata.sdk.sample.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.firstdata.sdk.sample.repo.CPSDKAppRepo
import com.firstdata.sdk.sample.utility.APPLICATION
import com.firstdata.sdk.sample.utility.IMessage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

abstract class BaseFragment : Fragment(), View.OnClickListener {
    @Inject
    lateinit var repo: CPSDKAppRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        APPLICATION?.appComponent?.inject(this)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(aUIEvent: IMessage) {
    }
}
