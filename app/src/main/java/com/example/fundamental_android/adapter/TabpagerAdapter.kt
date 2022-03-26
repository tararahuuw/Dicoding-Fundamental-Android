package com.example.fundamental_android.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fundamental_android.fragment.FollowerFragment
import com.example.fundamental_android.fragment.FollowingFragment

class TabpagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position) {
            0 -> {
                fragment = FollowerFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowerFragment.ARG_NAME, username)
                }
            }
            1 -> {
                fragment = FollowingFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowingFragment.ARG_NAME, username)
                }
            }
        }
        return fragment as Fragment
    }
}
