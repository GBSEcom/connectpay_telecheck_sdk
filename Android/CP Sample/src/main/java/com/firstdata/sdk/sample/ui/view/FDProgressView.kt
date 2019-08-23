package com.firstdata.sdk.sample.ui.view

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.firstdata.sdk.sample.R
import kotlinx.android.synthetic.main.fd_progress_dialog.*

/**
 *
 */
class FDProgressView private constructor(context: Context) : Dialog(context) {

    var warningMsg: TextView?

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.fd_progress_dialog)
        warningMsg = messageTxtView
        this.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }

    companion object {
        private var sProgressDialog: FDProgressView? = null

        @JvmOverloads
        fun showProgressBar(context: Context, cancelable: Boolean, message: String? = null) {

            try {
                if (sProgressDialog != null && sProgressDialog!!.isShowing) {
                    sProgressDialog!!.cancel()
                }
                sProgressDialog = FDProgressView(context)
                sProgressDialog!!.messageTxtView?.setTextColor(context.resources.getColor(R.color.textColorDefault))
                sProgressDialog!!.messageTxtView?.text = message
                        ?: context.getString(R.string.str_common_warning_wait)

                sProgressDialog!!.setCancelable(cancelable)
                sProgressDialog!!.show()
            } catch (aException: Exception) {
            }

        }

        fun hideProgressBar() {
            try {
                if (sProgressDialog != null && sProgressDialog!!.isShowing) {
                    sProgressDialog!!.dismiss()
                }
            } catch (e: Exception) {
            }

        }
    }
}
