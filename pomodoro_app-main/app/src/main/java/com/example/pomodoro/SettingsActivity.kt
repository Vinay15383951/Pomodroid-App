package com.example.pomodoro

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val editFocus = findViewById<EditText>(R.id.editFocusTime)
        val editBreak = findViewById<EditText>(R.id.editBreakTime)
        val switchSound = findViewById<Switch>(R.id.switchSound)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val prefs = getSharedPreferences("PomodoroPrefs", Context.MODE_PRIVATE)
        editFocus.setText(prefs.getInt("focusTime", 25).toString())
        editBreak.setText(prefs.getInt("breakTime", 5).toString())
        switchSound.isChecked = prefs.getBoolean("playSound", true)

        btnSave.setOnClickListener {
            val focus = editFocus.text.toString().toIntOrNull() ?: 25
            val rest = editBreak.text.toString().toIntOrNull() ?: 5
            val sound = switchSound.isChecked

            prefs.edit().apply {
                putInt("focusTime", focus)
                putInt("breakTime", rest)
                putBoolean("playSound", sound)
                apply()
            }

            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
