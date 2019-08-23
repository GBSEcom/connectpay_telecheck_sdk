package com.firstdata.sdk.sample.utility

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.TextUtils
import com.firstdata.util.utils.FDLogger
import org.greenrobot.eventbus.EventBus
import java.io.Serializable

data class SDKTestProfileConfig(
        var fdAccountID: String,
        var qaEndPoint: String,
        var postUrlEndPoint: String,
        var apiKey: String,
        var secretKey: String,
        var fdCustomerID: String,
        var catEndPoint: String,
        var environment: String,
        var publicKey: String,
        var cardNumber: String?
) : BaseObservable() {

    @Bindable
    fun getfdAccountID(): String {
        return fdAccountID
    }

    fun setfdAccountID(newValue: String) {
        if (newValue.equals(fdAccountID)) {
            return
        }
        fdAccountID = newValue
        //notifyPropertyChanged(binding)
    }


    @Bindable
    fun getfdCustomerID(): String {
        return fdCustomerID
    }

    @Bindable
    fun getapiKey(): String {
        return apiKey
    }

    @Bindable
    fun getsecretKey(): String {
        return secretKey
    }


    var isMandatoryKeysAvailable = fun(): Boolean {
        var lRes = !TextUtils.isEmpty(apiKey) && !TextUtils.isEmpty(secretKey)
        System.out.println("Key valid ? $lRes  ")
        return lRes
    }
    var isMandatoryProfileInfoAvailable = fun(): Boolean {
        var lRes = !TextUtils.isEmpty(fdAccountID) && !TextUtils.isEmpty(fdCustomerID)
        System.out.println("Profile valid ? $lRes ")
        return lRes
    }
}

enum class UseCase(value: String) {

    // ROW : 1
    MANUAL_ONLY("CP.ManualOnly"),
    MANUAL_DEPOSIT("CP.ManualDeposit"),
    MANUAL_DEPOSIT_WITH_ACC_FIELD("CP.ManualDepositWithAccountField"),

    // ROW : 2
    BANK_ONLY("CP.BankOnly"),
    ACCOUNT_VALIDATION("CP.AccountValidations"),
    UPDATE_ENROLLMENT("CP.UpdateEnrollment"),


    // ROW : 3
    ENROLL_BOTH_OPTION_MOBILE("CP.EnrollmentBothOption.Mobile"),
    ENROLL_BOTH_OPTION_WEB("CP.EnrollmentBothOption.Web"),
    ENROLL_ACCOUNT_DETAILS("CP.EnrollmentAccountDetails"),

    // ROW : 4
    CLOSE_ACCOUNT("CP.CloseAccount"),
    ACCOUNT_UPDATE("account.update");

    private val mValue: String = value

    fun getValue(): String {
        return mValue
    }
}

abstract class IMessage protected constructor(val eventName: String) {

    fun post() {
        FDLogger.d("Event Bus", "-------> [ $eventName ]")
        EventBus.getDefault().post(this)
    }

    fun postSticky() {
        FDLogger.d("Event Bus", "----(S)---> [ $eventName ]")
        EventBus.getDefault().postSticky(this)
    }

    companion object {

        val TAG = IMessage::class.java.simpleName
    }

}

class UIEvent(aBuilder: Builder) : IMessage(aBuilder.eventName), Serializable {

    val eventID: Int

    val subEventID: Int

    val eventObject: Any?

    var subEventObject: Any? = null

    init {
        eventID = aBuilder.eventID
        eventObject = aBuilder.obj
        subEventObject = aBuilder.subObject
        subEventID = aBuilder.subEventID
    }

    class Builder {

        var subEventID = -1

        var eventID = -1

        var eventName = ""

        var obj: Any? = null

        var subObject: Any? = null

        fun setEventName(eventName: String): Builder {
            this.eventName = eventName
            return this
        }

        fun setEventID(eventID: Int): Builder {
            this.eventID = eventID
            return this
        }

        fun setSubEventID(subEvent: Int): Builder {
            this.subEventID = subEvent
            return this
        }

        fun setEventObject(obj: Any): Builder {
            this.obj = obj
            return this
        }

        fun setSubEventObject(obj: Any): Builder {
            this.subObject = obj
            return this
        }

        fun build(): IMessage {
            return UIEvent(this)
        }

        fun buildAndPost() {
            build().post()
        }

        fun buildAndPostSticky() {
            build().postSticky()
        }
    }
}


