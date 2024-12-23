package com.example.hw1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import com.example.hw1.logic.GameManage
import com.example.hw1.utilities.SignalManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: List<AppCompatImageView>
    private lateinit var column1: List<AppCompatImageView>
    private lateinit var column2: List<AppCompatImageView>
    private lateinit var column3: List<AppCompatImageView>
    private lateinit var catcher: List<AppCompatImageView>
    private lateinit var leftArrow: ExtendedFloatingActionButton
    private lateinit var rightArrow: ExtendedFloatingActionButton

    private var currentCatcherIndex = 1
    private lateinit var randomDropJob: Job
    private lateinit var fallingObjectJob: Job
    private var timerOn: Boolean = false
    private lateinit var gameManager: GameManage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        gameManager = GameManage(main_IMG_hearts.size)
        initViews()
    }

    override fun onPause() {
        super.onPause()
        timerOn = false
    }

    override fun onResume() {
        super.onResume()
        timerOn = true
    }

    @SuppressLint("CutPasteId")
    private fun findViews() {
        leftArrow = findViewById(R.id.main_FAB_left_arrow)
        rightArrow = findViewById(R.id.main_FAB_right_arrow)

        main_IMG_hearts = listOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )

        column1 = listOf(
            findViewById(R.id.main_IMG_row1_col1),
            findViewById(R.id.main_IMG_row2_col1),
            findViewById(R.id.main_IMG_row3_col1),
            findViewById(R.id.main_IMG_row4_col1),
            findViewById(R.id.main_IMG_row5_col1),
            findViewById(R.id.main_IMG_row6_col1),
            findViewById(R.id.main_IMG_row7_col1),
            findViewById(R.id.main_IMG_row8_col1),
            findViewById(R.id.main_IMG_row9_col1),
            findViewById(R.id.main_IMG_row10_col1),
            findViewById(R.id.main_IMG_row11_col1),
            findViewById(R.id.main_IMG_row12_col1),
            findViewById(R.id.main_IMG_row13_col1),
            findViewById(R.id.main_IMG_row14_col1),
            findViewById(R.id.main_IMG_row15_col1)
        )
        column2 = listOf(
            findViewById(R.id.main_IMG_row1_col2),
            findViewById(R.id.main_IMG_row2_col2),
            findViewById(R.id.main_IMG_row3_col2),
            findViewById(R.id.main_IMG_row4_col2),
            findViewById(R.id.main_IMG_row5_col2),
            findViewById(R.id.main_IMG_row6_col2),
            findViewById(R.id.main_IMG_row7_col2),
            findViewById(R.id.main_IMG_row8_col2),
            findViewById(R.id.main_IMG_row9_col2),
            findViewById(R.id.main_IMG_row10_col2),
            findViewById(R.id.main_IMG_row11_col2),
            findViewById(R.id.main_IMG_row12_col2),
            findViewById(R.id.main_IMG_row13_col2),
            findViewById(R.id.main_IMG_row14_col2),
            findViewById(R.id.main_IMG_row15_col2)
        )
        column3 = listOf(
            findViewById(R.id.main_IMG_row1_col3),
            findViewById(R.id.main_IMG_row2_col3),
            findViewById(R.id.main_IMG_row3_col3),
            findViewById(R.id.main_IMG_row4_col3),
            findViewById(R.id.main_IMG_row5_col3),
            findViewById(R.id.main_IMG_row6_col3),
            findViewById(R.id.main_IMG_row7_col3),
            findViewById(R.id.main_IMG_row8_col3),
            findViewById(R.id.main_IMG_row9_col3),
            findViewById(R.id.main_IMG_row10_col3),
            findViewById(R.id.main_IMG_row11_col3),
            findViewById(R.id.main_IMG_row12_col3),
            findViewById(R.id.main_IMG_row13_col3),
            findViewById(R.id.main_IMG_row14_col3),
            findViewById(R.id.main_IMG_row15_col3)
        )
        catcher = listOf(
            findViewById(R.id.main_IMG_row13_col1), // left
            findViewById(R.id.main_IMG_row13_col2), // center
            findViewById(R.id.main_IMG_row13_col3) // right
        )
    }

    private fun initViews() {
        timerOn = true
        initCharacter()
        startRandomDropping()
        refreshUI()


        leftArrow.setOnClickListener { v -> moveCatcherLeft() }
        rightArrow.setOnClickListener { v -> moveCatcherRight() }
    }

    private fun initCharacter() {
        catcher[currentCatcherIndex].apply {
            setImageResource(R.drawable.catcher)
            visibility = View.VISIBLE
        }
    }

    private fun refreshUI() {
        if (gameManager.isGameOver) { // Lost!
            timerOn = false
            randomDropJob.cancel()
            fallingObjectJob.cancel()
            changeActivity("😭Stop eat healthy!\nGame Over!")
        } else {
            if(gameManager.injuries != 0)
                main_IMG_hearts[main_IMG_hearts.size - gameManager.injuries].visibility = View.INVISIBLE
        }
    }

    private fun changeActivity(msg: String) {
        val intent = Intent(this, GameoverActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }

    private fun moveCatcher(direction: Int) {
        catcher[currentCatcherIndex].apply {
            visibility = AppCompatImageView.INVISIBLE
            setImageResource(R.drawable.cucumber)
        }
        currentCatcherIndex = (currentCatcherIndex + direction + catcher.size) % catcher.size
        catcher[currentCatcherIndex].apply {
            setImageResource(R.drawable.catcher)
            visibility = AppCompatImageView.VISIBLE
        }
    }

    private fun moveCatcherRight() = moveCatcher(1)
    private fun moveCatcherLeft() = moveCatcher(-1)

    private fun startRandomDropping() {
        val columns = listOf(column1, column2, column3)
        randomDropJob = lifecycleScope.launch {

            while (isActive && timerOn) {
                val randomColumn = columns.random()
                startFallingObject(randomColumn, columns.indexOf(randomColumn))
                delay(3000)
            }
        }
    }

    private fun startFallingObject(column: List<AppCompatImageView>, columnNumber: Int) {
        var currentFallingPosition = 0
        fallingObjectJob = lifecycleScope.launch {
            while (currentFallingPosition < column.size && isActive) {
                if (!timerOn) {
                    delay(100)
                    continue
                }

                // Check for collision
                if (currentFallingPosition == 13 && currentCatcherIndex == columnNumber) {
                    addCollision()
                    break
                }

                // Clear previous position
                if (currentFallingPosition > 0) {
                    column[currentFallingPosition - 1].visibility = AppCompatImageView.INVISIBLE
                }

                // Show current position
                column[currentFallingPosition].visibility = AppCompatImageView.VISIBLE

                delay(300)
                currentFallingPosition++
            }

            // Clean up at the end
            column.last().visibility = AppCompatImageView.INVISIBLE
        }
    }



    private fun addCollision() {
        gameManager.addInjurie()
        SignalManager.getInstance().toast("🤮🤮🤮")
        SignalManager.getInstance().vibrate()
        refreshUI()
    }
}