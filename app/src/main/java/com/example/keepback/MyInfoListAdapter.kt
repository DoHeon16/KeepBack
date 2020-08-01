package com.example.keepback

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyInfoListAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return when(position){
            0 -> ItemFragment.newItemFragment(1)//time
            1 -> ItemFragment.newItemFragment(2)//price
            2 -> ItemFragment.newItemFragment(3)//size
            else -> ItemFragment()
        }
    }
}