package com.example.mitartgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mitartgo.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var loggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guestbutton.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        loginbutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("type", "login")
            startActivity(intent)
            loggedIn = true
        }

        signupbutton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("type", "signup")
            startActivity(intent)
            loggedIn = true
        }

        supportActionBar!!.hide()
    }

    override fun onResume() {
        super.onResume()
        if (loggedIn) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            loggedIn = false
        }
    }


}


