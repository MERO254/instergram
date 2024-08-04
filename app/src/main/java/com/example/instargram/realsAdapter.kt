package com.example.instargram

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView

class realsAdapter(var context:Context, var list:List<realsData>):RecyclerView.Adapter<realsAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var video:VideoView = view.findViewById(R.id.videoView)
        var description:TextView = view.findViewById(R.id.txtrealdescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.realslayout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]

        holder.description.text = data.videoDescription

        var uri:Uri = Uri.parse(data.video)

        var mediacontoler = MediaController(context)
        mediacontoler.setAnchorView(holder.video)

        holder.video.setMediaController(mediacontoler)

        holder.video.setVideoURI(uri)

        holder.video.start()

    }
}
