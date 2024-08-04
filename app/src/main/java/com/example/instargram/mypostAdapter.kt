package com.example.instargram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class mypostAdapter(var context:Context, var list:List<HomeData>):RecyclerView.Adapter<mypostAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var image:ImageView = view.findViewById(R.id.imgmypost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.mypost, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]

        Picasso.get()
            .load(data.image)
            .into(holder.image)

    }

    fun searchdata(newlist:List<HomeData>){
        this.list = newlist
        notifyDataSetChanged()
    }
}