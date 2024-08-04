package com.example.instargram

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var list: MutableList<HomeData>
    private lateinit var adapter: HomeAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        list = mutableListOf()
        val recycler: RecyclerView = view.findViewById(R.id.homerecycler)
        val message: ImageView = view.findViewById(R.id.imgmessage)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeAdapter(requireContext(), list)
        recycler.adapter = adapter

        findPosts()

        message.setOnClickListener {
            val intent = Intent(requireContext(), MessageActivity2::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun findPosts() {
        db = FirebaseFirestore.getInstance()
        db.collection("posts").get()
            .addOnSuccessListener { documents ->
                list.clear()
                for (document in documents) {
                    val value = document.toObject(HomeData::class.java)
                    list.add(value!!)
                }
                adapter.notifyDataSetChanged()
//                Toast.makeText(requireContext(), "Connected \uD83D\uDE01", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error loading posts", Toast.LENGTH_SHORT).show()
            }
    }
}
