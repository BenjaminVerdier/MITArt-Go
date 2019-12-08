package com.example.mitartgo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_art_detail.*

class ArtDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_detail)
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val description = intent.getStringExtra("description")

        Log.d("dbg", title!!)
        Log.d("dbg", description!!)
        Log.d("dbg", image!!)

        textView.text = title
        textView2.text = description



        var bMap : Bitmap? = null
        while (bMap == null) {
            bMap = getBitmapFromURL(image)
        }

        imageView.setImageBitmap(bMap)
    }
}
