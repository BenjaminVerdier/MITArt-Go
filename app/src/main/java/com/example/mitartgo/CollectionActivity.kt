package com.example.mitartgo

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_art_detail.*
import kotlinx.android.synthetic.main.activity_collection.*


class CollectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        supportActionBar!!.title = "Collection"

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels

        for (artpiece in MapsActivity.userCol) {
            val lay = LinearLayout(this)
            val img = ImageView(this)
            var bMap : Bitmap? = null
            while (bMap == null) {
                bMap = getBitmapFromURL(artpiece["image"])
            }
            val b = Bitmap.createScaledBitmap(bMap!!,250,250,false)
            img.setImageBitmap(b)

            lay.addView(img)

            val txtView = TextView(this)
            val txt = artpiece["title"] + ", " + artpiece["artist"]+ ", " + artpiece["date"]
            txtView.text = txt
            txtView.height = 250
            txtView.width = width - 250
            txtView.gravity = CENTER
            lay.addView(txtView)

            lay.isClickable = true

            lay.setOnClickListener {
                val intent = Intent(this, ArtDetailActivity::class.java)
                    intent.putExtra("title", artpiece["title"])
                    intent.putExtra("image", artpiece["image"])
                    intent.putExtra("description", artpiece["description"])
                    intent.putExtra("artist", artpiece["artist"])
                    intent.putExtra("date", artpiece["date"])
                startActivity(intent)
            }

            linLayoutCol.addView(lay)

        }
    }
}
