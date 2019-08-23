package com.firstdata.sdk.sample.ui.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.adapters.TextViewBindingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firstdata.sdk.sample.R
import com.firstdata.sdk.sample.databinding.FragmentSessionConfigBinding
import com.firstdata.sdk.sample.ui.activities.BaseFragment
import com.firstdata.sdk.sample.ui.view.FDProgressView
import com.firstdata.sdk.sample.utility.*
import kotlinx.android.synthetic.main.fragment_session_config.*


class SessionConfigFragment : BaseFragment(), TextViewBindingAdapter.OnTextChanged {

    private lateinit var binding: FragmentSessionConfigBinding

    override fun onStart() {
        super.onStart()
        binding.sdkConfig = SDK_TEST_PROFILE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session_config, container, false)
        binding.lifecycleOwner = activity
        binding.clickListener = this
        binding.textWatcher = this
        return binding.root
    }

    override fun onClick(v: View?) {

        if (btnClearProfile == v) {
            binding.sdkConfig?.fdCustomerID = ""
            binding.sdkConfig?.fdAccountID = ""
        } else if (btnClearKeys == v) {
            binding.sdkConfig?.apiKey = ""
            binding.sdkConfig?.secretKey = ""
        } else if (btnGenToken == v) {
            FDProgressView.showProgressBar(activity as Context, false,
                    "Generating token., Please wait ...")
            SDK_TEST_PROFILE?.environment = binding.apiEndPointView.text.toString()
            SDK_TEST_PROFILE?.let { repo.generateSessionToken(it) }
        }
        binding.invalidateAll()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.btnClearProfile.isEnabled = !binding.sdkConfig?.fdCustomerID?.isEmpty()!!
                || !binding.sdkConfig?.fdAccountID?.isEmpty()!!
        binding.btnClearKeys.isEnabled = !binding.sdkConfig?.apiKey?.isEmpty()!!
                || !binding.sdkConfig?.secretKey?.isEmpty()!!

        binding.btnGenToken.isEnabled = !binding.sdkConfig?.apiKey?.isEmpty()!!
                && !binding.sdkConfig?.secretKey?.isEmpty()!!
                && !binding.sdkConfig?.fdCustomerID?.isEmpty()!!
    }


}