package com.example.instargram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class RealsFragment : Fragment() {

    private lateinit var recycleview:RecyclerView
    private lateinit var list:MutableList<realsData>
    private lateinit var adapter: realsAdapter
    private lateinit var db:FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_reals, container, false)

        list = mutableListOf()
        recycleview = view.findViewById(R.id.realsrecycler)
        recycleview.layoutManager = LinearLayoutManager(requireContext())
        adapter = realsAdapter(requireContext(),list)
        recycleview.adapter = adapter

        getReals()

        return view
    }
    private fun getReals(){
        db = FirebaseFirestore.getInstance()
        db.collection("reals").get().addOnSuccessListener {
            for(i in it){
                if(i != null){
                    var value = i.toObject(realsData::class.java)
                    list.add(value)
                    adapter.notifyDataSetChanged()
                }
            }
        }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "error loading reals ${it}", Toast.LENGTH_SHORT).show()
            }
    }


}