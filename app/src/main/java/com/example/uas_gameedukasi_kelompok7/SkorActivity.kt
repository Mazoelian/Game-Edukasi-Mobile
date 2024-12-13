package com.example.uas_gameedukasi_kelompok7

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SkorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skor)

        val tvFinalScore: TextView = findViewById(R.id.tv_final_score)
        val tvCorrectAnswers: TextView = findViewById(R.id.tv_correct_answers)
        val tvWrongAnswers: TextView = findViewById(R.id.tv_wrong_answers)
        val btnRetry: Button = findViewById(R.id.btn_retry)
        val btnHome: Button = findViewById(R.id.btn_home)

        // Ambil data skor dari intent
        val correct = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val wrong = intent.getIntExtra("WRONG_ANSWERS", 0)

        // Hitung skor total
        val totalScore = correct * 10

        // Tampilkan skor dan jumlah jawaban
        tvFinalScore.text = "Skor Anda: $totalScore/100"
        tvCorrectAnswers.text = "Benar: $correct"
        tvWrongAnswers.text = "Salah: $wrong"

        // Tombol ulangi tes
        btnRetry.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tombol kembali ke beranda
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
