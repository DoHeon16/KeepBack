package com.example.keepback

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_section.*

/**
 * A simple [Fragment] subclass.
 */
class SectionFragment : Fragment(){

    lateinit var myDBHelper: ReadCSV
    var metro= mutableListOf<String>()
    lateinit var register: ArrayAdapter<String>
    lateinit var adapter: MySectionAdapter
    var stations=ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_section, container, false)
//        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
//        initSpinner()
//        init(recyclerView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSpinner()
        init(recyclerView)
    }

    fun initSpinner(){
        myDBHelper=ReadCSV(resources.openRawResource(R.raw.place),resources.openRawResource(R.raw.quantity),activity!!)
        spinner.setSelection(1) //1호선이 기본
        spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                metro.clear()
                when(position){
                    0->{
                        Toast.makeText(activity!!,"한 호선 내 구간 검색만 가능합니다.",Toast.LENGTH_SHORT).show()
                    }
                    1->{metro= myDBHelper.makeAdapter("1호선", metro)!!}
                    2->{metro= myDBHelper.makeAdapter("2호선", metro)!!}
                    3->{metro= myDBHelper.makeAdapter("3호선", metro)!!}
                    4->{metro= myDBHelper.makeAdapter("4호선", metro)!!}
                    5->{metro= myDBHelper.makeAdapter("5호선", metro)!!}
                    6->{metro= myDBHelper.makeAdapter("6호선", metro)!!}
                    7->{metro= myDBHelper.makeAdapter("7호선", metro)!!}
                    8->{metro= myDBHelper.makeAdapter("8호선", metro)!!}
                }
            }

        }
    }

    fun init(recyclerView: RecyclerView){
        //역명 힌트 어댑터 등록
        start.setOnClickListener {
            start.setText("")
            start.setTextColor(Color.BLACK)
        }
        end.setOnClickListener {
            end.setText("")
            end.setTextColor(Color.BLACK)
        }
        if(metro!=null){
            register=ArrayAdapter(activity!!,android.R.layout.simple_dropdown_item_1line,metro)//adapter 등록
            start.setAdapter(register)
            start.addTextChangedListener (object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val str=s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            end.setAdapter(register)
            end.addTextChangedListener (object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val str=s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        //검색 버튼 누르면
        button.setOnClickListener {
            if(start.text.toString()==""){//입력이 안되어 있는 경우
                Toast.makeText(activity!!,"출발역을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            if(end.text.toString()==""){//입력이 안되어 있는 경우
                Toast.makeText(activity!!,"도착역을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            //검색 클릭 시, 역 이름에 일치하는 내용이면
            if(metro.contains(start.text.toString())&&metro.contains(end.text.toString())){
                //recylerview 생성
                val arrival=myDBHelper.findNum(start.text.toString())?.toInt()
                val destination=myDBHelper.findNum(end.text.toString())?.toInt()

                //해당 고유번호 사이의 역 이름 받기
                if (arrival != null && destination!= null) {
                    //if(stations!=null) stations.clear()
                    if(arrival<destination){
                        for(i in arrival .. destination){
                            val station=myDBHelper.findStation(i)
                            stations.add(station!!)
                        }
                    }else{
                        for(i in destination .. arrival){
                            val station=myDBHelper.findStation(i)
                            stations.add(station!!)
                        }
                    }
                }
                //stations를 recyclerView에 뿌리기
                recyclerView.layoutManager=LinearLayoutManager(activity!!,LinearLayoutManager.VERTICAL,false)
                adapter=MySectionAdapter(stations)
                adapter.itemClickListener=object :MySectionAdapter.OnItemClickListener{
                    override fun onItemClick(
                        holder: MySectionAdapter.ViewHolder,
                        view: View,
                        data: String,
                        position: Int
                    ) {
                        //아이템 클릭 시 액티비티 전환
                        Log.e("data name",data)
                        val i=Intent(activity,MetroActivity::class.java)
                        i.putExtra("station",data)
                        startActivity(i)
                    }

                }
                recyclerView.adapter=adapter
            }
            else{
                Toast.makeText(activity!!,"해당 호선에서 입력한 역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
