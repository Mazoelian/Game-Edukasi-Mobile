package com.example.uas_gameedukasi_kelompok7

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referensi tombol
        val startGameButton = findViewById<Button>(R.id.btn_start_game)

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
}
