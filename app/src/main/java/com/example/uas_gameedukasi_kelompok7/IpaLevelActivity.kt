package com.example.uas_gameedukasi_kelompok7

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IpaLevelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ipa_level)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referensi tombol
        val btnEasy = findViewById<Button>(R.id.btn_easy)
        val btnNormal = findViewById<Button>(R.id.btn_normal)
        val btnHard = findViewById<Button>(R.id.btn_hard)

        btnEasy.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("LEVEL", "mudah")
            startActivity(intent)
        }

        btnNormal.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("LEVEL", "normal")
            startActivity(intent)
        }

        btnHard.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("LEVEL", "sulit")
            startActivity(intent)
        }

    }
}