package com.example.hw1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.hw1.enums.DifficultyEnum
import com.example.hw1.enums.ModeEnum
import com.example.hw1.utilities.Constants
import com.example.hw1.utilities.SharedPreferencesManager
import com.google.android.material.textfield.TextInputEditText
import kotlin.text.clear

class MainMenuActivity: AppCompatActivity() {

    private var difficultyEnum: DifficultyEnum = DifficultyEnum.EASY
    private var modeEnum: ModeEnum = ModeEnum.BUTTONS

    private lateinit var main_menu_SWITCH_difficulty: SwitchCompat

    private lateinit var main_menu_SWITCH_mode: SwitchCompat

    private lateinit var main_menu_LBL_welcome: AppCompatTextView

    private lateinit var main_menu_BTN_start: AppCompatButton

    private lateinit var main_menu_BTN_leaderboard: AppCompatButton

    private lateinit var main_menu_EDT_name: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

//        val sharedPreferences: SharedPreferences =
//            getSharedPreferences(Constants.Leaderboard.LEADERBOARD_KEY, Context.MODE_PRIVATE) // Replace with your prefs name
//
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply() // Use apply for background commit

        findViews()
        initViews()
    }

    private fun findViews() {
        main_menu_SWITCH_difficulty = findViewById(R.id.main_menu_SWITCH_difficulty)
        main_menu_SWITCH_mode = findViewById(R.id.main_menu_SWITCH_mode)
        main_menu_LBL_welcome = findViewById(R.id.main_menu_LBL_welcome)
        main_menu_BTN_start = findViewById(R.id.main_menu_BTN_start)
        main_menu_BTN_leaderboard = findViewById(R.id.main_menu_BTN_leaderboard)
        main_menu_EDT_name = findViewById(R.id.main_menu_EDT_name)
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
        main_menu_BTN_leaderboard.setOnClickListener { showLeaderboard() }
    }

    private fun showLeaderboard() {
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent)
    }

    private fun startGame() {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("name", main_menu_EDT_name.text.toString())
        bundle.putInt("difficulty", difficultyEnum.ordinal)
        bundle.putInt("mode", modeEnum.ordinal)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}