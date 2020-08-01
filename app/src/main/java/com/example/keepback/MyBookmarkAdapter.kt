package com.example.keepback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MyBookmarkAdapter (options: FirebaseRecyclerOptions<BookMark>) :
//질의(option)에 따른 결과를 매핑시켜주는 어댑터
    FirebaseRecyclerAdapter<BookMark, MyBookmarkAdapter.ViewHolder>(options) {
    //주고받는 데이터는 product 클래스
    var itemClickListener:OnItemClickListener?=null//interface 정의 시 객체를 멤버로 둔다

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var productLine: TextView
        init {
            productLine=itemView.findViewById(R.id.line)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(it,adapterPosition)
            }
        }
    }

    //클릭했을 때의 이벤트 처리를 위한 인터페이스
    interface OnItemClickListener{
        fun onItemClick(view: View, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: BookMark) {
        holder.productLine.text=model.line+" "+model.station
    }
}