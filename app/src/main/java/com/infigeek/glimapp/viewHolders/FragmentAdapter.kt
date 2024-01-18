package com.infigeek.glimapp.viewHolders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infigeek.glimapp.viewFragments.Business
import com.infigeek.glimapp.viewFragments.Entertainment
import com.infigeek.glimapp.viewFragments.General
import com.infigeek.glimapp.viewFragments.Health
import com.infigeek.glimapp.viewFragments.Science
import com.infigeek.glimapp.viewFragments.Sports
import com.infigeek.glimapp.viewFragments.Tech
import com.infigeek.glimapp.utility.appConstants.TOTAL_NEWS_TAB

class FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle){
    override fun getItemCount(): Int = TOTAL_NEWS_TAB
    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                return General()
            }
            1 -> {
                return Tech()
            }
            2 -> {
                return Health()
            }
            3 -> {
                return Science()
            }
            4 -> {
                return Sports()
            }
            5 -> {
                return Entertainment()
            }
            6 -> {
                return Business()
            }
            else -> return Business()

        }
    }
}