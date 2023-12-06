package com.example.capstoneproject.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capstoneproject.ui.history.ongoing.OnGoingFragment
import com.example.capstoneproject.ui.history.successful.SuccessfulFragment
import com.example.capstoneproject.ui.history.unsuccessful.UnsuccessfulFragment

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OnGoingFragment()
            1 -> fragment = SuccessfulFragment()
            2 -> fragment = UnsuccessfulFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}