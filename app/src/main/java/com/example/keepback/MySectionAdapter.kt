package com.example.keepback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MySectionAdapter(val items:ArrayList<String>)
    : RecyclerView.Adapter<MySectionAdapter.ViewHolder>()
{
    var itemClickListener: OnItemClickListener?=null//interface 정의 시 객체를 멤버로 둔다

    //클릭했을 때의 이벤트 처리를 위한 인터페이스
    interface OnItemClickListener{
        fun onItemClick(holder: ViewHolder,view: View,data:String,position:Int)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var productLine: TextView
        init {
            productLine=itemView.findViewById(R.id.line)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(this,it,items[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productLine.text=items[position]
    }
}
