package com.example.hw1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import com.example.hw1.enums.DifficultyEnum
import com.example.hw1.enums.ModeEnum

class MainMenuActivity: AppCompatActivity() {

    private var difficultyEnum: DifficultyEnum = DifficultyEnum.EASY
    private var modeEnum: ModeEnum = ModeEnum.BUTTONS

    private lateinit var main_menu_SWITCH_difficulty: SwitchCompat

    private lateinit var main_menu_SWITCH_mode: SwitchCompat

    private lateinit var main_menu_LBL_welcome: AppCompatTextView

    private lateinit var main_menu_BTN_start: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        findViews()
        initViews()
    }

    private fun findViews() {
        main_menu_SWITCH_difficulty = findViewById(R.id.main_menu_SWITCH_difficulty)
        main_menu_SWITCH_mode = findViewById(R.id.main_menu_SWITCH_mode)
        main_menu_LBL_welcome = findViewById(R.id.main_menu_LBL_welcome)
        main_menu_BTN_start = findViewById(R.id.main_menu_BTN_start)
    }

    private fun initViews() {
        main_menu_SWITCH_difficulty.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                difficultyEnum = DifficultyEnum.HARD
            } else {
                difficultyEnum = DifficultyEnum.EASY
            }
        }
        main_menu_SWITCH_mode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                modeEnum = ModeEnum.SENSORS
            } else {
                modeEnum = ModeEnum.BUTTONS
            }
        }

        main_menu_BTN_start.setOnClickListener { startGame() }
    }

    private fun startGame() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("difficulty", difficultyEnum.ordinal)
        intent.putExtra("mode", modeEnum.ordinal)
        startActivity(intent)
        finish()
    }
}