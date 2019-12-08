package com.example.mitartgo

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_art_detail.*
import kotlinx.android.synthetic.main.activity_collection.*


class CollectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        supportActionBar!!.title = "Collection"

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
            val txt = artpiece["title"] + ", " + artpiece["artist"]+ "," + artpiece["date"]
            txtView.text = txt
            lay.addView(txtView)
            linLayoutCol.addView(lay)
        }
    }
}
