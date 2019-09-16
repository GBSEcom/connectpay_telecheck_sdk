package com.firstdata.sdk.sample.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.firstdata.sdk.sample.R
import com.firstdata.sdk.sample.ui.fragment.LaunchConfigFragment
import com.firstdata.sdk.sample.ui.fragment.SessionConfigFragment
import com.firstdata.sdk.sample.ui.fragment.UseCaseConfigFragment

class HomePageAdapter(context: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private val mContext: Context = context

    override fun getItem(position: Int): Fragment {

        if (0 == position) {
            return SessionConfigFragment()
        } else if (1 == position) {
            return UseCaseConfigFragment()
        } else { // (2 == position) {
            return LaunchConfigFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        if (0 == position) {
            return mContext.getString(R.string.str_frag_title_session_config)
        } else if (1 == position) {
            return mContext.getString(R.string.str_frag_title_use_case)
        } else { // (2 == position) {
            return mContext.getString(R.string.str_frag_title_launch_sdk)
        }

    }

    override fun getCount(): Int {
        return 3
    }

}