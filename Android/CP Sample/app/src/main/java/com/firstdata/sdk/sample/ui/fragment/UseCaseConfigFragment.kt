package com.firstdata.sdk.sample.ui.fragment

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firstdata.cpsdk.HPLink
import com.firstdata.sdk.sample.R
import com.firstdata.sdk.sample.databinding.FragmentUseCaseConfigBinding
import com.firstdata.sdk.sample.ui.activities.BaseFragment
import com.firstdata.sdk.sample.utility.*
import com.firstdata.sdk.sample.utility.MessageID.MSG_LOAD_LAUNCH_CONFIG_SCREEN
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.fd_progress_dialog.*
import kotlinx.android.synthetic.main.fd_progress_dialog.view.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.Comparator


class UseCaseConfigFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentUseCaseConfigBinding

    private var useCaseMap: MutableMap<UseCase, UIHpLink> = mutableMapOf()

    data class UIHpLink(var useCaseName: UseCase, var pageID: String,
                        var hpLinkInternal: HPLink, var titleStrID: Int, var imgID: Int,
                        var postURL:String? = null,
                        var extraParams: MutableMap<String, Any>? = null)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_use_case_config, container, false)


        binding.progressContainer.messageTxtView.text = getString(R.string.str_war_use_case)
        binding.progressContainer.visibility = View.VISIBLE

        if (null != CONFIG_PAGES && !CONFIG_PAGES!!.isEmpty()) {
            configureUseCase()
        }
        return binding.root
    }

    private fun buildUIForUseCase() {
        var row: View?
        var lKeys: List<UseCase> = useCaseMap.keys.toList()

        for (position in 0..lKeys.size step 3) {
            if (position <= 2) {
                row = binding.row1
            } else if (position <= 5) {
                row = binding.row2
            } else if (position <= 8) {
                row = binding.row3
            } else {
                row = binding.row4
            }

            var item1: UIHpLink? = null
            var item2: UIHpLink? = null
            var item3: UIHpLink? = null

            if (position < lKeys.size) {
                item1 = useCaseMap[lKeys[position]]
            }
            if (position + 1 < lKeys.size) {
                item2 = useCaseMap[lKeys[position + 1]]
            }
            if (position + 2 < lKeys.size) {
                item3 = useCaseMap[lKeys[position + 2]]
            }

            configureDashItem(this, row!!,
                    item1?.titleStrID ?: -1, item1?.imgID ?: -1,
                    item2?.titleStrID ?: -1, item2?.imgID ?: -1,
                    item3?.titleStrID ?: -1, item3?.imgID ?: -1
            )
        }
        binding.progressContainer.visibility = View.GONE

    }


    private fun configureUseCase() {

        for (pageInfo: HPLink in CONFIG_PAGES!!) {

            var lResult: UIHpLink? = null
            when (pageInfo.rel) {
                UseCase.MANUAL_ONLY.getValue() -> {

                    lResult = UIHpLink(UseCase.MANUAL_ONLY,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_manual_only, R.drawable.ic_manual_only,null)
                }
                UseCase.MANUAL_DEPOSIT.getValue() -> {
                    lResult = UIHpLink(UseCase.MANUAL_DEPOSIT,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_manual_deposit, R.drawable.ic_manual_deposit)
                }
                UseCase.BANK_ONLY.getValue() -> {
                    lResult = UIHpLink(UseCase.BANK_ONLY,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_bank_only, R.drawable.ic_account)
                }
                UseCase.CLOSE_ACCOUNT.getValue() -> {
                    lResult = UIHpLink(UseCase.CLOSE_ACCOUNT,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_close_account, R.drawable.ic_close_account)
                }
                UseCase.UPDATE_ENROLLMENT.getValue() -> {
                    lResult = UIHpLink(UseCase.UPDATE_ENROLLMENT,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_update_enrollment, R.drawable.ic_acc_edit)
                }
                UseCase.ACCOUNT_VALIDATION.getValue() -> {
                    lResult = UIHpLink(UseCase.ACCOUNT_VALIDATION,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_account_validation, R.drawable.ic_acc_validation)
                }
                UseCase.ENROLL_BOTH_OPTION_WEB.getValue() -> {
                    lResult = UIHpLink(UseCase.ENROLL_BOTH_OPTION_WEB,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_enroll_both_web, R.drawable.ic_web_enrollment)
                }
                UseCase.ENROLL_BOTH_OPTION_MOBILE.getValue() -> {
                    lResult = UIHpLink(UseCase.ENROLL_BOTH_OPTION_MOBILE,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_enroll_both_mobile, R.drawable.ic_mobile_enrollment)
                }
                UseCase.ENROLL_ACCOUNT_DETAILS.getValue() -> {
                    lResult = UIHpLink(UseCase.ENROLL_ACCOUNT_DETAILS,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_enroll_acc_detail, R.drawable.ic_enroll_new_account)
                }
                UseCase.MANUAL_DEPOSIT_WITH_ACC_FIELD.getValue() -> {
                    lResult = UIHpLink(UseCase.MANUAL_DEPOSIT_WITH_ACC_FIELD,
                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
                            R.string.str_manual_deposit_with_acc_field_, R.drawable.ic_manual)
                }
//                UseCase.ACCOUNT_UPDATE.name -> {
//                    lResult = UIHpLink(UseCase.ACCOUNT_UPDATE.name,
//                            Uri.parse(pageInfo.href).lastPathSegment, pageInfo,
//                            R.string., R.drawable.)
//                }
            }
            lResult?.let { useCaseMap.put(lResult.useCaseName, it) }
        }
        val lengthComparator = Comparator { str1: UseCase, str2: UseCase -> str1.ordinal - str2.ordinal }
        useCaseMap = useCaseMap.toSortedMap(lengthComparator)

        buildUIForUseCase()
    }

    override fun onClick(aView: View) {
        if (null == aView.tag) {
            return
        }
        val lTargetTagID = Integer.parseInt(aView.tag.toString())

        var lSectedCase: UseCase? = null
        var lNeedMoreInfo = false

        // Row :1
        if (R.string.str_account_validation === lTargetTagID) {
            lSectedCase = UseCase.ACCOUNT_VALIDATION
        } else if (R.string.str_close_account === lTargetTagID) {
            lSectedCase = UseCase.CLOSE_ACCOUNT
        } else if (R.string.str_update_enrollment === lTargetTagID) {
            lSectedCase = UseCase.UPDATE_ENROLLMENT
        }
        // Row :2
        else if (R.string.str_bank_only === lTargetTagID) {
            lSectedCase = UseCase.BANK_ONLY
        } else if (R.string.str_manual_only === lTargetTagID) {
            lSectedCase = UseCase.MANUAL_ONLY
        } else if (R.string.str_manual_deposit === lTargetTagID) {
            lSectedCase = UseCase.MANUAL_DEPOSIT
        }
        // Row :3
        else if (R.string.str_enroll_both_web === lTargetTagID) {
            lSectedCase = UseCase.ENROLL_BOTH_OPTION_WEB
        } else if (R.string.str_enroll_both_mobile === lTargetTagID) {
            lSectedCase = UseCase.ENROLL_BOTH_OPTION_MOBILE
        } else if (R.string.str_enroll_acc_detail === lTargetTagID) {
            lSectedCase = UseCase.ENROLL_ACCOUNT_DETAILS
            lNeedMoreInfo = true
        }
        // Row :4
        else if (R.string.str_manual_deposit_with_acc_field_ === lTargetTagID) {
            lSectedCase = UseCase.MANUAL_DEPOSIT_WITH_ACC_FIELD
        }

        if (lNeedMoreInfo) {
            postLaunchConfigScreenEvent(useCaseMap[lSectedCase]!!, lSectedCase!!)
        } else {
            launchSDK(activity as AppCompatActivity, SDK_TEST_PROFILE, useCaseMap[lSectedCase])
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(aUIEvent: UIEvent) {

        val lEventID = aUIEvent.eventID
        val lEventObject = aUIEvent.eventObject

        if (lEventID == MessageID.MSG_LOAD_PAGES) {
            configureUseCase()
        }
    }

}