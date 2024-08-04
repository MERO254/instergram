package com.example.instargram

import android.app.AlertDialog
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class HomeAdapter(var context: Context, var list:List<HomeData>):RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private lateinit var db:FirebaseFirestore

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        var image:ImageView = view.findViewById(R.id.imghome)
        var txtds:TextView = view.findViewById(R.id.txtdescription)
        var imglikes:ImageView = view.findViewById(R.id.imglikes)
        var imgcomments:ImageView = view.findViewById(R.id.imgcomment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.homelayout, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]

        holder.txtds.text = data.description

        Picasso.get()
            .load(data.image)
            .into(holder.image)

        holder.imgcomments.setOnClickListener {

            db.collection("posts").get().addOnSuccessListener {
                for(i in it){
                    var value = i.toObject(HomeData::class.java)
                    var comment = value.comments

                    var intent = Intent(context, CommentActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

        holder.imglikes.setOnClickListener {

            db = FirebaseFirestore.getInstance()
            db.collection("posts").get().addOnSuccessListener {
                if(it != null){
                    for(i in it){
                        var value = i.toObject(HomeData::class.java)
                        var image = value.image
                        var description = value.description
                        var comment = value.comments
                        var name = value.name
                        var like = value.likes?.toInt()
                        var newvalue = like?.plus(1)

                        var hashMap = hashMapOf(
                            "image" to image,
                            "description" to description,
                            "comments" to comment,
                            "name" to name,
                            "likes" to newvalue
                        )



                        db.collection("elvis").document().update(hashMap).addOnSuccessListener {
                            Toast.makeText(context, "like updated", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }

        }
    }

}