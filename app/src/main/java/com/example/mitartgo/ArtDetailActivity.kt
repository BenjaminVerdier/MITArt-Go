package com.example.mitartgo

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_art_detail.*

class ArtDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_detail)
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val description = intent.getStringExtra("description")
        val date = intent.getStringExtra("date")
        val artist = intent.getStringExtra("artist")

        Log.d("dbg", title!!)
        Log.d("dbg", description!!)
        Log.d("dbg", image!!)

        art_title_view.text = title
        art_description_view.text = description
        art_date_view.text = date
        artist_name_view.text  = artist



        var bMap : Bitmap? = null
        while (bMap == null) {
            bMap = getBitmapFromURL(image)
        }

        imageView.setImageBitmap(bMap)

        var collected = false
        for (artpiece in MapsActivity.userCol) {
            if (artpiece["title"] == title) {
                collected = true
                break
            }
        }

        if (collected) {
            collectBtn.visibility = GONE
            collectedImg.visibility = VISIBLE

        } else {
            collectBtn.setOnClickListener {
                MapsActivity.addArtPiece(title)
                collectBtn.visibility = GONE
                collectedImg.visibility = VISIBLE
            }
            collectBtn.visibility = VISIBLE
            collectedImg.visibility = GONE
        }


    }
}
