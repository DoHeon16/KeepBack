package com.example.keepback

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [LockerFragment.OnListFragmentInteractionListener] interface.
 */
class LockerFragment(val station:String) : Fragment() {

    // TODO: Customize parameters
    lateinit var myDBHelper: ReadCSV
    private var columnCount = 1
    lateinit var info:ArrayList<String>
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_locker_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyLockerRecyclerViewAdapter(info, listener)
            }
        }
        return view
    }

    fun init(){
        myDBHelper=ReadCSV(resources.openRawResource(R.raw.place),resources.openRawResource(R.raw.quantity),activity!!)
        Log.i("이름",station)
        info= myDBHelper.findStation(station)!!
        if(info==null){
            Log.i("info","null")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        init()
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: String?)
    }

}
