package com.example.timefighter

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    internal lateinit var tapMeButton:Button
    internal lateinit var gameScoreTextView:TextView
    internal lateinit var timeLeftTextView:TextView
    private var score:Int = 0
    private  var gameStarted = false
    private var initialCountDown:Long = 6000
    internal var countDownInterval:Long = 1000
    internal lateinit var countDownTimer: CountDownTimer
    internal var timeLeftOnTimer:Long = 60000
    internal val TAG = MainActivity::class.java.simpleName
    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG,"onCreate called. Score is : $score")
        setContentView(R.layout.activity_main)
        tapMeButton = findViewById(R.id.tap_me_button)
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        gameScoreTextView.text = getString(R.string.your_score,score.toString())
        val blinkAnimation = AnimationUtils.loadAnimation(this,R.anim.blink)
        gameScoreTextView.startAnimation(blinkAnimation)
        if(savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }else{
            resetGame()
        }

        tapMeButton.setOnClickListener {
                view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce)
            val interpolator = MyBounceInterpolator(0.2, 20.0)
            bounceAnimation.setInterpolator(interpolator)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }
    }

    private fun restoreGame(){
        gameScoreTextView.text = getString(R.string.your_score,score.toString())
        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.time_left,restoredTime.toString())
        countDownTimer = object : CountDownTimer(timeLeftOnTimer,countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                var timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left,timeLeft.toString())

            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
            outState.putInt(SCORE_KEY, score)
            outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
            countDownTimer.cancel()
            Log.d(TAG, "onSaveInstanceState: Saveing Score: $score & Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }

    private fun incrementScore() {
        if(!gameStarted){
            startGame()
        }
        score++
        var scoreString = getString(R.string.your_score,score.toString())
        gameScoreTextView.text = scoreString

    }
    private fun startGame(){
        countDownTimer.start()
        gameStarted = true
    }
    private  fun resetGame(){
        score = 0
        gameScoreTextView.text = getString(R.string.your_score,score.toString())
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.time_left,initialTimeLeft.toString())
        countDownTimer = object : CountDownTimer(initialCountDown,countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left,timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
    }
    fun endGame(){
        Toast.makeText(this,getString(R.string.game_over_message,score.toString()),Toast.LENGTH_LONG).show()
        resetGame()
        gameStarted = false
    }
    /*

https://code.tutsplus.com/tutorials/android-sdk-implement-an-options-menu--mobile-9453
Menue item in android

create a new resource of type menu then drag munue item in drowing or menu activity
then set id of action item to action_about
and title to whatever you want from add new resource.
i named it About
then set icon to info icon
and then set showASAction to always

start writing code in your mainActivity that shown below.
i. only you can see the info i button on screen when you implements these methods

*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.action_about){
            showInfo()
        }
        return true
    }
    fun showInfo(){
        //define which version you are useing
        val dialogTitle = getString(R.string.about_title,BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()

    }


    /*

    Android App icon
    How to create app icon in Android



    */


}