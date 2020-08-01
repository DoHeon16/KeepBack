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
import kotlinx.android.synthetic.main.fragment_station.*

/**
 * A simple [Fragment] subclass.
 */
class StationFragment : Fragment() {

    var selectedSubway="전체"
    lateinit var myDBHelper: ReadCSV
    lateinit var adapter: ArrayAdapter<String>
    var metro= mutableListOf<String>()
   // private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerStation()
        updateAdapter(selectedSubway)
        initSpinner()
        init()
    }
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if(context is OnListFragmentInteractionListener){
//            listener=context
//        }else{
//            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener=null
//    }

    override fun onResume() {
        super.onResume()
        initSpinner()
        init()
    }

    fun initSpinner(){
        subway.setSelection(0)
        subway.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){ //선택한 호선 내에서 찾아보기
                    0->selectedSubway="전체"
                    1->selectedSubway="1호선"
                    2->selectedSubway="2호선"
                    3->selectedSubway="3호선"
                    4->selectedSubway="4호선"
                    5->selectedSubway="5호선"
                    6->selectedSubway="6호선"
                    7->selectedSubway="7호선"
                    8->selectedSubway="8호선"
                }
                updateAdapter(selectedSubway)
            }
        }
    }

    fun registerStation(){    //completeView adapter 등록
        myDBHelper=ReadCSV(resources.openRawResource(R.raw.place),resources.openRawResource(R.raw.quantity),activity!!)
       // myDBHelper.readFiles()

    }

    fun updateAdapter(selectedSubway: String){
        metro.clear()
        metro= myDBHelper.makeAdapter(selectedSubway, metro)!!//table, adapter 항목 넘겨서 완성된 adapter 다시 받기
        //metro는 업데이트 되어야함.
        if(metro==null){
            Log.i("metro adapter","null")
        }
    }

    fun init(){
        //역명 힌트 어댑터 등록
        station.setOnClickListener {
            station.setText("")
            station.setTextColor(Color.BLACK)
        }
        if(metro!=null){
            adapter=ArrayAdapter(activity!!,android.R.layout.simple_dropdown_item_1line,metro)//adapter 등록
            station.setAdapter(adapter)
            station.addTextChangedListener (object :TextWatcher{
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
        search.setOnClickListener {
            if(station.text.toString()==""){//입력이 안되어 있는 경우
                Toast.makeText(activity!!,"찾으실 역을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            //검색 클릭 시, 역 이름에 일치하는 내용이면
            if(metro.contains(station.text.toString())){
                //액티비티 이동
                var i=Intent(activity,MetroActivity::class.java)
                i.putExtra("station",station.text.toString()) //역 정보 넘기기
                startActivity(i)
            }
            else{
                Toast.makeText(activity!!,"해당 호선에서 입력한 역이 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
//    interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onListFragmentInteraction(item: String?)
//    }

}
