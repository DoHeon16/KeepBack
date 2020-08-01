package com.example.keepback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_bookmark.*

class StateFragment(val station:String) : Fragment(),MyStateAdapter.OnItemClickListener {

    lateinit var rdb: DatabaseReference //firebase
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MyStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_state, container, false)
        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        init(recyclerView)
        initAdapter(recyclerView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(recyclerView)
        initAdapter(recyclerView)
    }



    fun init(recyclerView: RecyclerView){
        layoutManager= LinearLayoutManager(this.context!!,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager=layoutManager
        rdb=FirebaseDatabase.getInstance().getReference("Usages/locker/$station")
        //Log.e("adferhbeaheah",rdb.key)
        val query=FirebaseDatabase.getInstance().reference.child("Usages/locker/$station").limitToLast(50)
        val collect=FirebaseDatabase.getInstance().reference.child("Usages/locker/$station")
        val option=
            FirebaseRecyclerOptions.Builder<SaveInfo>().setQuery(query,SaveInfo::class.java).build()

        adapter=MyStateAdapter(option)
        adapter.itemClickListener=object :MyStateAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
               // TODO("Not yet implemented")
            }

        }
        recyclerView.adapter=adapter
    }

    fun initAdapter(recyclerView: RecyclerView){
        if(adapter!=null){
            adapter.stopListening()
        }
        rdb=FirebaseDatabase.getInstance().getReference("Usages/locker/$station")
        //Log.e("adferhbeaheah",rdb.key)
        val query=FirebaseDatabase.getInstance().reference.child("Usages/locker/$station").limitToLast(50)
        val option=
            FirebaseRecyclerOptions.Builder<SaveInfo>().setQuery(query,SaveInfo::class.java).build()

        adapter=MyStateAdapter(option)
        recyclerView.adapter=adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()    //database 변경 시 자동으로 recyclerview 갱신
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClick(view: View, position: Int) {
       // TODO("Not yet implemented")
    }

}
