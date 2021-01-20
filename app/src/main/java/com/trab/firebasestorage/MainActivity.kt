package com.trab.firebasestorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.trab.firebasestorage.databinding.ActivityMainBinding
import dmax.dialog.SpotsDialog

class MainActivity : AppCompatActivity() {
    private val imgCODE = 0xFF
    private val imgType = "image/*"

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val alertDialog by lazy<AlertDialog> {
        SpotsDialog.Builder().setContext(this).build()
    }

    val firebaseStorage by lazy<FirebaseStorage> {
        FirebaseStorage.getInstance()
    }

    fun pickImgIntent() {
        val intent = Intent()
        intent.type = imgType
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Capture image"), imgCODE)
    }

    fun uploadImg(data: Intent) {
        alertDialog.show()
        val task = firebaseStorage.getReference("image.jpg").putFile(data!!.data!!)
        task.addOnSuccessListener {
            firebaseStorage.getReference("image.jpg").downloadUrl.addOnSuccessListener {
                Picasso.get().load(it.toString()).into(
                    binding.imgSent
                )
            }

        }

        alertDialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fbImgPick.setOnClickListener {
            pickImgIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            imgCODE -> uploadImg(data!!)
        }
    }

}