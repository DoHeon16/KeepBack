package com.example.keepback

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_metro.*

class MetroActivity : AppCompatActivity(),LockerFragment.OnListFragmentInteractionListener {

    val textArray=arrayListOf<String>("물품보관함 정보","현재 사용 현황")
    var station:String?=null
    val list= arrayOf("대형","중형","소형")
    lateinit var rdb: DatabaseReference //firebase
    lateinit var myDBHelper:ReadCSV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro)
        val i=intent
        station= i.getStringExtra("station") //검색한 역 정보 전달 받기
        myDBHelper=ReadCSV(resources.openRawResource(R.raw.place),resources.openRawResource(R.raw.quantity),this)
        init()
        btninit()
    }

    private fun init(){
        viewPager.adapter= station?.let { MyFragmentAdapter(this, it) }
        TabLayoutMediator(tab,viewPager){
            tab, position ->
            tab.text=textArray[position]//해당 위치에 tab text 대입
        }.attach()
    }

    fun btninit(){
        
        lent.setOnClickListener {
            rdb=FirebaseDatabase.getInstance().getReference("Usages/locker")
            var size = ""
            val input=EditText(this)
            input.hint="휴대폰 번호를 입력하세요. (숫자 11자리 연속)"
            val builder=AlertDialog.Builder(this)
            builder.setTitle("보관하기").setSingleChoiceItems(list,-1,DialogInterface.OnClickListener { dialog, which ->
                size=list[which]
            }).setView(input)
                .setPositiveButton("확인",object :DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val password=input.text
                        if(password.toString()==""){
                            Toast.makeText(this@MetroActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }else{
                            val array = myDBHelper.findStation(station!!)
                            //firebase add

                            if (array?.get(0).toString().contains(station!!)) {//역명일치시
                                //station child가 이미 존재 시 구분해야되나? ////////////-->구분 필요, 아니면 덮어쓰여져서 쓸모없음
                                val item=SaveInfo(size,password.toString(),false)
                                rdb.child(station!!).child(password.toString()).setValue(item)//database에 추가
                                Toast.makeText(this@MetroActivity, "보관 완료하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }).setNegativeButton("취소",DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this,"보관하기를 취소합니다.", Toast.LENGTH_SHORT).show()
                }).show()
        }
        
        trouble.setOnClickListener {
            var btn=""
            var i=-1
            rdb=FirebaseDatabase.getInstance().getReference("Usages/locker")
            Log.e("rdb",rdb.toString())
           // val query=FirebaseDatabase.getInstance().reference.child("Usages/broken").limitToLast(50)
            //다이얼로그 만들기
            val builder=AlertDialog.Builder(this)
            builder.setTitle("고장 신고 접수").setSingleChoiceItems(list,-1,DialogInterface.OnClickListener { dialog, which ->
                btn=list[which]
            }).setPositiveButton("확인",object :DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val item=SaveInfo(btn,i.toString(),true)
                    rdb.child(station!!).child((i--).toString()).setValue(item)//database에 추가
                    Log.i("station, choice",station+btn)
                    Toast.makeText(this@MetroActivity, "고장 접수 완료하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }).setNegativeButton("취소",DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this,"고장 접수를 취소합니다.", Toast.LENGTH_SHORT).show()
            }).show()
        }

        bookmark.setOnClickListener {
            rdb=FirebaseDatabase.getInstance().getReference("Usages/bookmark")
            val line=myDBHelper.findLine(station!!)
            val count=myDBHelper.findNum(station!!)
            val item=BookMark(line!!,station!!)
            rdb.child(count!!).setValue(item) //즐겨찾기 항목에 추가
            Toast.makeText(this, "즐겨찾기에 추가하였습니다.", Toast.LENGTH_SHORT).show()
        }

        returning.setOnClickListener {
            rdb=FirebaseDatabase.getInstance().getReference("Usages/locker")
            var size = ""
            val input=EditText(this)
            input.hint="휴대폰 번호를 입력하세요. (숫자 11자리 연속)"
            val builder=AlertDialog.Builder(this)
            builder.setTitle("반납하기").setSingleChoiceItems(list,-1,DialogInterface.OnClickListener { dialog, which ->
                    size=list[which]
                }).setView(input)
                .setPositiveButton("확인",object :DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val password=input.text
                        if(password.toString()==""){
                            Toast.makeText(this@MetroActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }else{
                            val array = myDBHelper.findStation(station!!)
                            if (array?.get(0).toString().contains(station!!)) {//역명일치시
                                //station에 저장되어 있는 것들 중 size가 일치하는 것 먼저 찾기
                                //child 값 일치 확인
                                if (rdb.child(station!!).child(size).key == size) {
                                    //비밀번호 확인...???????????????????????????
                                        rdb.child(station!!).child(password.toString()).removeValue()
                                        //Toast.makeText(this@MetroActivity, "반납 완료하였습니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MetroActivity, "사용 중인 보관함이 없습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }).setNegativeButton("취소",DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this,"반납하기를 취소합니다.", Toast.LENGTH_SHORT).show()
                }).show()

        }
    }


    override fun onListFragmentInteraction(item: String?) {
       // TODO("Not yet implemented")
    }


}
