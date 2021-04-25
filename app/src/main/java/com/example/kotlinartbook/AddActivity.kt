package com.example.kotlinartbook

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.*
import java.lang.Exception
import java.lang.reflect.Executable
import androidx.core.content.ContextCompat as ContextCompat


class AddActivity : AppCompatActivity() {
    lateinit var selectedPicture: Uri
    lateinit var imageView: ImageView
    lateinit var artNameText: EditText
    lateinit var artistNameText: EditText
    lateinit var yearText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        imageView = findViewById(R.id.imageView)
        artNameText=findViewById(R.id.artNameText)
        artistNameText=findViewById(R.id.artistNameText)
        yearText=findViewById(R.id.yearText)
    }

    fun save(view: View) {

        val artName=artNameText.text.toString()
        val artistName=artistNameText.text.toString()
        val year=yearText.text.toString().toIntOrNull()

        val intent= Intent(this,MainActivity::class.java)

        intent.putExtra("artname",artName)
        intent.putExtra("artistname",artistName)
        intent.putExtra("year",year)

        startActivity(intent)

    }

    fun selectImage(view: View) {//this, Manifest.permisson.RREAD_EXTERNAL_STORAGE-> izin erişim işleri için

        if (ContextCompat.checkSelfPermission(AddActivity@this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {// Kullanıcı erişim izni sorgusu
            ActivityCompat.requestPermissions(AddActivity@this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)//Erişim izni yoksa izin alınıyor
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery, 2)
        }
    }

    override fun onRequestPermissionsResult(//İzin istedikten sonra direk galeri açılması
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {

        if (requestCode == 1) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPicture = data.data!! // çift önlem kullanılmak gerektiğinde if kontrolü yapılmalı sağlıklı olur
            try {
                if (selectedPicture != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(this.contentResolver, selectedPicture)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        imageView.setImageBitmap(bitmap)

                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }catch (ex: Exception){
                println(ex)
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}