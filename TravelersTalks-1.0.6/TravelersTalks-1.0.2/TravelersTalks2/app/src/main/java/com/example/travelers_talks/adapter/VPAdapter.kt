package com.example.travelers_talks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.travelers_talks.fragment_mydrafts
import com.example.travelers_talks.fragment_myfavorites
import com.example.travelers_talks.fragment_myposts

class VPAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
            return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> fragment_myposts()
            1 -> fragment_mydrafts()
            2 -> fragment_myfavorites()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

    }

}