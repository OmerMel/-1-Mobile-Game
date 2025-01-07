package com.example.hw1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import com.example.hw1.interfaces.TiltCallback
import com.example.hw1.enums.DifficultyEnum
import com.example.hw1.enums.FallingObjectType
import com.example.hw1.enums.GameEffect
import com.example.hw1.logic.GameManage
import com.example.hw1.enums.ModeEnum
import com.example.hw1.utilities.FallingObject
import com.example.hw1.utilities.SignalManager
import com.example.hw1.utilities.TiltDetector
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: List<AppCompatImageView>
    private lateinit var column1: List<AppCompatImageView>
    private lateinit var column2: List<AppCompatImageView>
    private lateinit var column3: List<AppCompatImageView>
    private lateinit var column4: List<AppCompatImageView>
    private lateinit var column5: List<AppCompatImageView>
    private lateinit var catcher: List<AppCompatImageView>
    private lateinit var leftArrow: ExtendedFloatingActionButton
    private lateinit var rightArrow: ExtendedFloatingActionButton
    private lateinit var score: MaterialTextView

    private lateinit var tiltDetector: TiltDetector
    private var currentCatcherIndex = 3
    private lateinit var randomDropJob: Job
    private lateinit var fallingObjectJob: Job
    private var timerOn: Boolean = false
    private lateinit var gameManager: GameManage
    private var difficulty: DifficultyEnum = DifficultyEnum.EASY
    private var mode: ModeEnum = ModeEnum.BUTTONS
    private var delayFallingSpeed: Long = 300
    private var delaySpawnSpeed: Long = 3000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        difficulty = DifficultyEnum.entries[intent.getIntExtra("difficulty", 0)]
        mode = ModeEnum.entries[intent.getIntExtra("mode", 0)]

        findViews()
        gameManager = GameManage(main_IMG_hearts.size)
        initViews()
    }

    override fun onPause() {
        super.onPause()
        timerOn = false
        if(mode == ModeEnum.SENSORS)
            tiltDetector.stop()
    }

    override fun onResume() {
        super.onResume()
        timerOn = true
        if(mode == ModeEnum.SENSORS)
            tiltDetector.start()
    }

    @SuppressLint("CutPasteId")
    private fun findViews() {
        leftArrow = findViewById(R.id.main_FAB_left_arrow)
        rightArrow = findViewById(R.id.main_FAB_right_arrow)
        score = findViewById(R.id.main_LBL_score)

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
        column4 = listOf(
            findViewById(R.id.main_IMG_row1_col4),
            findViewById(R.id.main_IMG_row2_col4),
            findViewById(R.id.main_IMG_row3_col4),
            findViewById(R.id.main_IMG_row4_col4),
            findViewById(R.id.main_IMG_row5_col4),
            findViewById(R.id.main_IMG_row6_col4),
            findViewById(R.id.main_IMG_row7_col4),
            findViewById(R.id.main_IMG_row8_col4),
            findViewById(R.id.main_IMG_row9_col4),
            findViewById(R.id.main_IMG_row10_col4),
            findViewById(R.id.main_IMG_row11_col4),
            findViewById(R.id.main_IMG_row12_col4),
            findViewById(R.id.main_IMG_row13_col4),
            findViewById(R.id.main_IMG_row14_col4),
            findViewById(R.id.main_IMG_row15_col4)
        )
        column5 = listOf(
            findViewById(R.id.main_IMG_row1_col5),
            findViewById(R.id.main_IMG_row2_col5),
            findViewById(R.id.main_IMG_row3_col5),
            findViewById(R.id.main_IMG_row4_col5),
            findViewById(R.id.main_IMG_row5_col5),
            findViewById(R.id.main_IMG_row6_col5),
            findViewById(R.id.main_IMG_row7_col5),
            findViewById(R.id.main_IMG_row8_col5),
            findViewById(R.id.main_IMG_row9_col5),
            findViewById(R.id.main_IMG_row10_col5),
            findViewById(R.id.main_IMG_row11_col5),
            findViewById(R.id.main_IMG_row12_col5),
            findViewById(R.id.main_IMG_row13_col5),
            findViewById(R.id.main_IMG_row14_col5),
            findViewById(R.id.main_IMG_row15_col5)
        )
        catcher = listOf(
            findViewById(R.id.main_IMG_row13_col1),
            findViewById(R.id.main_IMG_row13_col2),
            findViewById(R.id.main_IMG_row13_col3),
            findViewById(R.id.main_IMG_row13_col4),
            findViewById(R.id.main_IMG_row13_col5)
        )
    }

    private fun initViews() {
        timerOn = true
        initGameMode()
        calculateGameSpeed()
        initCharacter()
        startRandomDropping()
        refreshUI()
    }

    private fun calculateGameSpeed() {
        delayFallingSpeed = difficulty.fallingSpeed.toLong() + (tiltDetector.tiltYValue.toLong() * 7)
        delaySpawnSpeed = difficulty.spawnSpeed.toLong() + (tiltDetector.tiltYValue.toLong() * 10)
    }

    private fun initGameMode() {
        if(mode == ModeEnum.SENSORS)
        {
            leftArrow.visibility = View.INVISIBLE
            rightArrow.visibility = View.INVISIBLE
            initTiltDetector()
        } else {
            leftArrow.setOnClickListener { v -> moveCatcherLeft() }
            rightArrow.setOnClickListener { v -> moveCatcherRight() }
        }
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltX() {
                    if(tiltDetector.tiltXValue < 0){
                        moveCatcherRight()
                    } else {
                        moveCatcherLeft()
                    }
                }
                override fun tiltY() {
                    calculateGameSpeed()
                }
            }
        )
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
        gameManager.score.toString().also { score.text = it }
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

    private fun moveCatcherRight() = moveCatcher(-1)
    private fun moveCatcherLeft() = moveCatcher(1)

    private fun startRandomDropping() {
        val columns = listOf(column1, column2, column3, column4, column5)
        randomDropJob = lifecycleScope.launch {

            while (isActive && timerOn) {
                val columnIndex = columns.indices.random()
                val fallingObject = FallingObject(
                    type = FallingObjectType.getRandomObject(),
                    columnIndex = columnIndex
                )
                startFallingObject(columns[columnIndex], columnIndex, fallingObject)
                delay(delaySpawnSpeed)
            }
        }
    }

    private fun startFallingObject(column: List<AppCompatImageView>, columnIndex: Int, fallingObject: FallingObject) {
        var currentFallingPosition = 0
        var collisionFlag = false
        fallingObjectJob = lifecycleScope.launch {
            while (currentFallingPosition < column.size && isActive) {
                if (!timerOn) {
                    delay(100)
                    continue
                }

                if (currentFallingPosition == 13 && currentCatcherIndex == columnIndex) {
                    handleCollision(fallingObject.type)
                    column[currentFallingPosition].setImageResource(R.drawable.catcher)
                    collisionFlag = true
                    Log.d("FallingObject", "Falling object at position $currentFallingPosition with type ${fallingObject.type}")
                    break
                }

                if (currentFallingPosition > 0) {
                    column[currentFallingPosition - 1].apply {
                        visibility = View.INVISIBLE
                    }
                }

                if(!collisionFlag){
                    column[currentFallingPosition].visibility = View.VISIBLE
                    column[currentFallingPosition].setImageResource(fallingObject.type.imageResource)
                    collisionFlag = false
                }

                val currentDelay = delayFallingSpeed
                delay(currentDelay)
                currentFallingPosition++
            }

            // Clean up at the end
            column.last().visibility = AppCompatImageView.INVISIBLE
        }
    }



    private fun handleCollision(type: FallingObjectType) {
        when (type.effect) {
            GameEffect.DAMAGE -> {
                gameManager.addInjurie()
                gameManager.addScore(type.points)
                SignalManager.getInstance().toast("🤮")
            }
            GameEffect.NONE -> {
                gameManager.addScore(type.points)
            }
        }
        SignalManager.getInstance().vibrate()
        refreshUI()
    }
}