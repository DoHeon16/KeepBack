package com.example.keepback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.keepback.R.layout
import kotlinx.android.synthetic.main.fragment_item.*

/**
 * A simple [Fragment] subclass.
 */
class ItemFragment : Fragment() {

    var infoNum=1

    companion object{
        fun newItemFragment(num:Int):ItemFragment{
            val itemFragment=ItemFragment()
            itemFragment.infoNum=num //처음은 time
            return itemFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //init(infoNum)
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(infoNum)
    }

    fun init(num:Int){
        when(num){
            //1=time
            1-> imageView.setImageResource(R.drawable.time)
            //2=price
            2-> imageView.setImageResource(R.drawable.price)
            //3=size
            3-> imageView.setImageResource(R.drawable.size)

        }

    }
}
