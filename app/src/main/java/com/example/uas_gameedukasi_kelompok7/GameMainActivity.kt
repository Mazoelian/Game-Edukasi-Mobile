package com.example.uas_gameedukasi_kelompok7

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameMainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer // Deklarasi MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_main)

        // Atur padding untuk sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi dan mulai MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.backsound) // Sesuaikan nama file audio di res/raw
        mediaPlayer.isLooping = true // Agar audio diputar terus-menerus
        mediaPlayer.start()


        // Menambahkan efek touch pada tombol IPA
        val btnIPA = findViewById<View>(R.id.btnIPA)
        btnIPA.setOnTouchListener { v, event ->
            handleHover(v, event)
            // Menambahkan return false agar event bisa diteruskan ke onClick
            return@setOnTouchListener false
        }

        btnIPA.setOnClickListener {
            val intent = Intent(this, IpaLevelActivity::class.java)
            startActivity(intent)
        }





        // Menambahkan efek touch pada tombol IPS
        val btnIPS = findViewById<View>(R.id.btnIPS)
        btnIPS.setOnTouchListener { v, event ->
            handleHover(v, event)
            return@setOnTouchListener false
        }

        btnIPS.setOnClickListener {
            val intent = Intent(this, IpsLevelActivity::class.java)
            startActivity(intent)
        }
    }



    // Fungsi untuk menangani animasi saat tombol ditekan (hover effect)
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

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan MediaPlayer saat Activity dihancurkan
        if (this::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}
