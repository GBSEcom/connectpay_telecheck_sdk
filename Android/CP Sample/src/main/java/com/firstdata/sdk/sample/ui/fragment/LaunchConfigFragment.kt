package com.firstdata.sdk.sample.ui.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firstdata.sdk.sample.R
import com.firstdata.sdk.sample.ui.activities.BaseFragment
import com.firstdata.sdk.sample.utility.*
import kotlinx.android.synthetic.main.fragment_launch_config.*
import kotlinx.android.synthetic.main.fragment_session_config.*
import kotlinx.android.synthetic.main.widget_acc_info.*
import kotlinx.android.synthetic.main.widget_title.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LaunchConfigFragment : BaseFragment() {

    private lateinit var inflater: LayoutInflater
    private var mUseCaseInfo: UseCaseConfigFragment.UIHpLink? = null
    private var mSelectedUseCase: UseCase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        return inflater.inflate(R.layout.fragment_launch_config, container, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mSelectedUseCase = null
        mUseCaseInfo = null
    }

    override fun onResume() {
        super.onResume()
        btnLaunchSDK.setOnClickListener(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(aUIEvent: UIEvent) {

        val lEventID = aUIEvent.eventID
        val lEventObject = aUIEvent.eventObject
        val lEventSubObject = aUIEvent.subEventObject

        if (lEventID == MessageID.MSG_LOAD_LAUNCH_CONFIG_SCREEN) {


            val lSelectedUseCase = lEventSubObject as UseCase

            if (mSelectedUseCase == lSelectedUseCase) {
                return
            }
            mSelectedUseCase = lSelectedUseCase
            mUseCaseInfo = lEventObject as UseCaseConfigFragment.UIHpLink

            configureUIForUseCase(mSelectedUseCase!!)
            EventBus.getDefault().removeStickyEvent(aUIEvent)
        }
    }

    private fun configureUIForUseCase(useCase: UseCase) {

        when (useCase) {
            UseCase.ENROLL_ACCOUNT_DETAILS -> {
                configureUIForEnrollAccountDetails()
            }
            else -> {
                Toast.makeText(context, "Unknown", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureUIForEnrollAccountDetails() {

        var lView = inflater.inflate(R.layout.widget_title, null)
        lView.titleTxtView.setText(R.string.str_title_user_acc_info)
        rootContainer.addView(lView)
        inflater.inflate(R.layout.widget_acc_info, rootContainer)


        lView = inflater.inflate(R.layout.widget_title, null)
        rootContainer.addView(lView)
        lView.titleTxtView.setText(R.string.str_title_user_address_info)
        inflater.inflate(R.layout.widget_address, rootContainer)
    }

    private fun prepareExtraForUseCase(): Boolean {
        when (mSelectedUseCase) {
            UseCase.ENROLL_ACCOUNT_DETAILS -> {


                if (accNumber.text?.length!! == 0) {
                    accNumber.requestFocus()
                    return false
                }
                if (routingNo.text?.length!! == 0) {
                    routingNo.requestFocus()
                    return false
                }
                if (transactionID.text?.length!! == 0) {
                    transactionID.requestFocus()
                    return false
                }

                // TODO : Other fields should be added
                val map = mutableMapOf<String, Any>(
                        "accountNumber" to accNumber.text.toString(),
                        "routingNumber" to routingNo.text.toString(),
                        "onlineBankTransactionId" to transactionID.text.toString(),
                        "postUrl" to postUriEndPointView.text.toString()
                )
                mUseCaseInfo?.extraParams = map


            }
            else -> {
            }
        }
        return true
    }


    override fun onClick(v: View?) {
        when (mSelectedUseCase) {
            UseCase.ENROLL_ACCOUNT_DETAILS -> {

                if (prepareExtraForUseCase()) {

                    launchSDK(activity as AppCompatActivity, SDK_TEST_PROFILE, mUseCaseInfo)
                }


            }
            else -> {
            }
        }

    }
}