package com.example.uas_gameedukasi_kelompok7

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Referensi tombol
        val startGameButton = findViewById<Button>(R.id.btn_start_game)
        startGameButton.setOnTouchListener { v, event ->
            handleHover(v, event)
            return@setOnTouchListener false
        }

        // Handle klik tombol
        startGameButton.setOnClickListener {
            // Pindah ke GameMainActivity
            val intent = Intent(this, GameMainActivity::class.java)
            startActivity(intent)
        }

        // Membuat status bar transparan
        window.apply {
            decorView.systemUiVisibility =
                (android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            statusBarColor = android.graphics.Color.TRANSPARENT
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
