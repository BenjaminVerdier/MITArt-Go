package com.example.mitartgo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ArtDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_detail)
    }

    fun newIntent(context: Context): Intent {
        val intent = Intent(context, ArtDetailActivity::class.java)
        return intent
    }
}
