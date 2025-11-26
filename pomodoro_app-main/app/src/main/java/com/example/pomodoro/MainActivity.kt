package com.example.pomodoro

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0L
    private var timerRunning = false
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to Pomofocus on title click
        binding.tvTitle.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", "https://pomofocus.io")
            startActivity(intent)
        }

        // Load preferences
        val prefs = getSharedPreferences("PomodoroPrefs", Context.MODE_PRIVATE)
        val focusTime = prefs.getInt("focusTime", 25)
        val playSound = prefs.getBoolean("playSound", true)
        timeLeftInMillis = focusTime * 60 * 1000L

        updateTimerText()

        // Start timer
        binding.btnStart.setOnClickListener {
            if (!timerRunning) startTimer()
        }

        // Short Break - 5 minutes
        binding.btnShortBreak.setOnClickListener {
            timeLeftInMillis = 5 * 60 * 1000L
            updateTimerText()
            if (timerRunning) {
                timer?.cancel()
                timerRunning = false
            }
        }

        // Long Break - 15 minutes
        binding.btnLongBreak.setOnClickListener {
            timeLeftInMillis = 15 * 60 * 1000L
            updateTimerText()
            if (timerRunning) {
                timer?.cancel()
                timerRunning = false
            }
        }

        // Reset timer with confirmation
        binding.btnReset.setOnClickListener {
            if (timerRunning) {
                showResetConfirmationDialog()
            } else {
                resetTimer()
            }
        }

        // Settings icon click
        binding.ivSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Profile icon click
        binding.ivProfile.setOnClickListener {
            Toast.makeText(this, "Profile feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                timerRunning = false
                updateTimerText()
                playAlarm()
            }
        }.start()
        timerRunning = true
    }

    private fun resetTimer() {
        timer?.cancel()
        val focusTime = getSharedPreferences("PomodoroPrefs", Context.MODE_PRIVATE)
            .getInt("focusTime", 25)
        timeLeftInMillis = focusTime * 60 * 1000L
        updateTimerText()
        timerRunning = false
        mediaPlayer?.stop()
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun playAlarm() {
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_clock)
        mediaPlayer?.start()
    }

    private fun showResetConfirmationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reset_confirmation, null)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                resetTimer()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        mediaPlayer?.release()
    }
}
