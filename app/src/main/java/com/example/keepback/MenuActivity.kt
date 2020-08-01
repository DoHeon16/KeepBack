package com.example.keepback

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    lateinit var myDBHelper: ReadCSV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        //데이터베이스 생성-->전달 말고 다른 곳에서 생성한 데이터베이스를 쓰는 것으로?
        myDBHelper=ReadCSV(resources.openRawResource(R.raw.place),resources.openRawResource(R.raw.quantity),this)
        myDBHelper.readFiles()
        init()
    }

    override fun onResume() {
        super.onResume()
        menu1.setTextColor(Color.GRAY)
        menu2.setTextColor(Color.GRAY)
        menu3.setTextColor(Color.GRAY)
        menu4.setTextColor(Color.GRAY)
    }

    private fun init(){

        menu1.setOnClickListener {
            //검색
            menu1.setTextColor(Color.BLACK)
            val i=Intent(this,SearchActivity::class.java)
            startActivity(i)
        }
        menu2.setOnClickListener {
            //현재 내 위치
            menu2.setTextColor(Color.BLACK)
            val i=Intent(this,MapActivity::class.java)
            startActivity(i)
        }
        menu3.setOnClickListener {
            //사용 정보
            menu3.setTextColor(Color.BLACK)
            val i=Intent(this,CommonActivity::class.java)
            startActivity(i)
        }
        menu4.setOnClickListener {
            //즐겨찾기
            menu4.setTextColor(Color.BLACK)
            val i=Intent(this,BookmarkActivity::class.java)
            startActivity(i)
        }
    }
}
