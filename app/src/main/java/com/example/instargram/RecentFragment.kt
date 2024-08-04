package com.example.instargram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class RecentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var list:MutableList<HomeData>
    private lateinit var db:FirebaseFirestore
    private lateinit var adapter: mypostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_recent, container, false)
        list = mutableListOf()
        recyclerView = view.findViewById(R.id.recentrecycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        adapter = mypostAdapter(requireContext(),list)
        recyclerView.adapter = adapter
        retrivepost()
        return view
    }
    private fun retrivepost(){
        db = FirebaseFirestore.getInstance()
        db.collection("posts").whereEqualTo("name", "elvis").get().addOnSuccessListener {
            list.clear()
            for(i in it){
               if(i != null){
                   var value = i.toObject(HomeData::class.java)
                   list.add(value!!)
                   adapter.notifyDataSetChanged()
               }
            }
        }
    }

}