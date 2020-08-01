package com.example.keepback

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this,"201811204 이도헌",Toast.LENGTH_SHORT).show()
        val hand=Handler();
        hand.postDelayed(Runnable {
            val i= Intent(this,MenuActivity::class.java)
            startActivity(i)
            finish()
        },3000)
    }
}
