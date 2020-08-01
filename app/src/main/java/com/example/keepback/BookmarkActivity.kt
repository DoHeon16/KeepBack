package com.example.keepback

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_bookmark.*

class BookmarkActivity : AppCompatActivity() {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var rdb: DatabaseReference //database 참조 객체
    lateinit var adapter: MyBookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        init()
    }

    fun init(){
        layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager=layoutManager
        rdb=FirebaseDatabase.getInstance().getReference("Usages/bookmark")
        val query=FirebaseDatabase.getInstance().reference.child("Usages/bookmark").orderByValue().limitToLast(50)
        val option=
            FirebaseRecyclerOptions.Builder<BookMark>().setQuery(query,BookMark::class.java).build()
        adapter=MyBookmarkAdapter(option)
        adapter.itemClickListener=object :MyBookmarkAdapter.OnItemClickListener{

            override fun onItemClick(view: View, position: Int) {
                val array=adapter.getItem(position).station
                Log.e("array[1]",array)
                val i= Intent(this@BookmarkActivity,MetroActivity::class.java)
                i.putExtra("station",array)
                startActivity(i)

            }
        }
        recyclerView.adapter=adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()    //database 변경 시 자동으로 recyclerview 갱신
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
