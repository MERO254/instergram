package com.example.instargram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale


class seachFragment : Fragment() {

    private lateinit var list:MutableList<HomeData>
    private lateinit var adapter: mypostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var db:FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_seach, container, false)
        list = mutableListOf()
        recyclerView = view.findViewById(R.id.searchrecycler)
        searchView = view.findViewById(R.id.searchview)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        adapter = mypostAdapter(requireContext(),list)
        recyclerView.adapter = adapter


        getpost()

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchValue(newText!!)
                return true
            }

        })

        return view
    }



    private fun getpost() {
        db = FirebaseFirestore.getInstance()
        db.collection("posts").get().addOnSuccessListener {
            list.clear()
            for(i in it){
                var value = i.toObject(HomeData::class.java)
                if(value != null){
                    list.add(value!!)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun searchValue(name:String){
        var newlist = ArrayList<HomeData>()
        for (i in list){
            if(i.name.lowercase(Locale.ROOT).contains(name)){
                newlist.add(i)
            }
        }
        if(newlist.isEmpty()){
            Toast.makeText(requireContext(), "no post found", Toast.LENGTH_SHORT).show()
        }else{
            adapter.searchdata(newlist)
        }
    }


}