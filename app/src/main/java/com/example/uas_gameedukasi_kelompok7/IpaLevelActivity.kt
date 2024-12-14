package com.example.uas_gameedukasi_kelompok7

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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

        btnEasy.setOnTouchListener { v, event ->
            handleHover(v, event)
            return@setOnTouchListener false
        }
        val btnNormal = findViewById<Button>(R.id.btn_normal)

        btnNormal.setOnTouchListener { v, event ->
            handleHover(v, event)
            return@setOnTouchListener false
        }
        val btnHard = findViewById<Button>(R.id.btn_hard)

        btnHard.setOnTouchListener { v, event ->
            handleHover(v, event)
            return@setOnTouchListener false
        }

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

    private fun handleHover(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Mulai animasi pembesaran saat tombol ditekan
                val animatorSet = AnimatorInflater.loadAnimator(this, R.animator.hover_scale) as AnimatorSet
                animatorSet.setTarget(view)
                animatorSet.start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Mengembalikan ukuran tombol ke semula setelah dilepas
                val animatorSet = AnimatorInflater.loadAnimator(this, R.animator.hover_scale) as AnimatorSet
                animatorSet.reverse()  // Membalikkan animasi pembesaran
                animatorSet.setTarget(view)
                animatorSet.start()
            }
        }
        return true
    }
}