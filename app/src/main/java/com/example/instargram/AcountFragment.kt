package com.example.instargram

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class AcountFragment : Fragment() {

    private lateinit var dbfirestore: FirebaseFirestore
    private lateinit var dbstorage: FirebaseStorage
    private lateinit var imageAccount: ImageView
    private lateinit var postlist:MutableList<HomeData>

    private var filepath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_acount, container, false)

        postlist = mutableListOf()

        val tablayout: TabLayout = view.findViewById(R.id.tablayout)
        val viewPager2: ViewPager2 = view.findViewById(R.id.viewpager)

        val btnProfile: Button = view.findViewById(R.id.btneditprofile)
        val btnaddpost:Button = view.findViewById(R.id.btnaddpost)

        imageAccount = view.findViewById(R.id.imgaccount)

        val adapter = AccountAdapter(this)
        viewPager2.adapter = adapter

        TabLayoutMediator(tablayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Recent"
                1 -> tab.text = "Favorite"
            }
        }.attach()

        btnProfile.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alertdialog, null)

            val edtName = dialogView.findViewById<TextInputEditText>(R.id.edtname)
            val edtEmail = dialogView.findViewById<TextInputEditText>(R.id.edtemail)
            val edtContact = dialogView.findViewById<TextInputEditText>(R.id.edtcontact)

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Profile")
                .setView(dialogView)
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialogView.findViewById<Button>(R.id.btnedit).setOnClickListener {
                var image: String = ""
                val name = edtName.text.toString()
                val email = edtEmail.text.toString()
                val contact = edtContact.text.toString()

                if (filepath != null) {
                    dbstorage = FirebaseStorage.getInstance()
                    dbstorage.getReference().child("image/elvis").downloadUrl.addOnCompleteListener {
                        image = it.result.toString()
                        if (name.isNotEmpty() && image.isNotEmpty() && email.isNotEmpty() && contact.isNotEmpty()) {
                            editProfile(name, email, contact, image)
                        } else {
                            Toast.makeText(requireContext(), "Please enter all credentials", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
                }
            }

            dialogView.findViewById<Button>(R.id.btneditimage).setOnClickListener {
                chooseImage()
            }

            dialog.show()
        }

        imageAccount.setOnClickListener {

        }

        btnaddpost.setOnClickListener {

            var view = LayoutInflater.from(requireContext()).inflate(R.layout.addpostlayout,null)


            var dialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle("add post")
                .setPositiveButton("close"){dialog,bn ->
                    dialog.dismiss()
                }
                .create()

            view.findViewById<ImageView>(R.id.imgpost).setOnClickListener {
                choosePost()
            }

            view.findViewById<TextInputEditText>(R.id.edtdescription)
            view.findViewById<Button>(R.id.btnaddpostselect).setOnClickListener {
                uploadPost()
            }
            dialog.show()
        }

        dbfirestore = FirebaseFirestore.getInstance()

        dbfirestore.collection("profiles").whereEqualTo("name","elvis").get().addOnSuccessListener {
            for(i in it){
               if(i != null){
                   var value = i.toObject(profileData::class.java)
                   var image = value.image
                   Picasso.get()
                       .load(image)
                       .transform(CircleTransform())
                       .into(imageAccount)
               }
            }
        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filepath = data.data
            uploadImage()
        }
    }
    //choose an image for your profile
    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
    //upload the selected image for your profile
    private fun uploadImage() {
        if (filepath != null) {
            dbstorage = FirebaseStorage.getInstance()
            dbstorage.getReference().child("image/elvis").putFile(filepath!!)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Image uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
        }
    }
   // edit your profile
    private fun editProfile(name: String, email: String, contact: String, image: String) {
        dbfirestore = FirebaseFirestore.getInstance()
        val values = hashMapOf(
            "name" to name,
            "email" to email,
            "contact" to contact,
            "image" to image
        )
        dbfirestore.collection("profiles").add(values).addOnCompleteListener {
            Toast.makeText(requireContext(), "Profile edited", Toast.LENGTH_SHORT).show()
        }
    }
    // select a image to post
    private fun choosePost(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
    //uploadpost to firestore and firebase
    private fun uploadPost(){
        if (filepath != null) {
            dbfirestore = FirebaseFirestore.getInstance()
            dbstorage = FirebaseStorage.getInstance()
            dbstorage.getReference().child("image/elvispost").putFile(filepath!!)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Image uploaded", Toast.LENGTH_SHORT).show()
                    dbstorage.getReference().child("image/elvispost").downloadUrl.addOnCompleteListener {
                        var url = it.result.toString()
                        var value = HomeData(mutableListOf(),"elvis added a post","${url}","0","elvis")
                        dbfirestore.collection("posts").add(value).addOnSuccessListener{
                            Toast.makeText(requireContext(), "post added", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
        }
    }





}
