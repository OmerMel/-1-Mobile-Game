package com.example.hw1

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView

class GameoverActivity : AppCompatActivity() {

    private lateinit var msg_LBL_msg: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        findViews()
        initViews()
    }


    private fun findViews() {
        msg_LBL_msg = findViewById(R.id.msg_LBL_msg)
    }

    private fun initViews() {
        val msg = intent.getStringExtra("msg")
        msg_LBL_msg.text = msg
    }

}