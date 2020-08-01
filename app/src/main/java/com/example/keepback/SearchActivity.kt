package com.example.keepback

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    val sectionFragment=SectionFragment()
    val stationFragment=StationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    fun init(){
        val fragment=supportFragmentManager.beginTransaction()
        fragment.addToBackStack(null)//back btn 누르면 이전 frag로 이동 가능
        fragment.replace(R.id.frameLayout,stationFragment) //기본은 역 검색
        fragment.commit()
        station.setOnClickListener {
            //역 검색
            if(!stationFragment.isVisible){
                val fragment=supportFragmentManager.beginTransaction()
                fragment.addToBackStack(null)//back btn 누르면 이전 frag로 이동 가능
                fragment.replace(R.id.frameLayout,stationFragment)
                fragment.commit()
            }
        }
        section.setOnClickListener {
            //구간 검색
            if(!sectionFragment.isVisible){
                val fragment=supportFragmentManager.beginTransaction()
                fragment.addToBackStack(null)//back btn 누르면 이전 frag로 이동 가능
                fragment.replace(R.id.frameLayout,sectionFragment)
                fragment.commit()
            }
        }

        back.setOnClickListener {
            finish()
        }
    }

//    override fun onListFragmentInteraction(item: String?) {
//       // TODO("Not yet implemented")
//    }
}
