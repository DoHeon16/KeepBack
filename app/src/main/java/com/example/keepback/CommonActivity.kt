package com.example.keepback

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.keepback.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_common.*

class CommonActivity : AppCompatActivity() {

    val textArray=arrayListOf<String>("이용가능 시간","이용 가격","크기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        init()
    }
    private fun init() {
        viewpager.adapter=MyInfoListAdapter(this)
        TabLayoutMediator(tablayout,viewpager){
                tab, position ->
            tab.text=textArray[position]
        }.attach()
    }
}
