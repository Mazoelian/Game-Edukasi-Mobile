package com.example.uas_gameedukasi_kelompok7

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

data class QuestionIps(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

class QuizIpsActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvQuestionCount: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnOptionA: Button
    private lateinit var btnOptionB: Button
    private lateinit var btnOptionC: Button
    private lateinit var btnOptionD: Button
    private lateinit var questionList: List<QuestionIps>

    private var currentQuestionIndex = 0
    private var correctCount = 0
    private var wrongCount = 0
    private var timerDuration: Long = 3 * 60 * 1000 // Default 3 minutes
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_ips)

        // Initialize views
        tvQuestion = findViewById(R.id.tv_question)
        tvQuestionCount = findViewById(R.id.tv_question_count)
        tvTimer = findViewById(R.id.tv_timer)
        btnOptionA = findViewById(R.id.btn_option_a)
        btnOptionB = findViewById(R.id.btn_option_b)
        btnOptionC = findViewById(R.id.btn_option_c)
        btnOptionD = findViewById(R.id.btn_option_d)

        // Get level from intent
        val level = intent.getStringExtra("LEVEL")
        setupQuestions()

        startTimer()
        setupQuestion()

        btnOptionA.setOnClickListener { checkAnswer("A") }
        btnOptionB.setOnClickListener { checkAnswer("B") }
        btnOptionC.setOnClickListener { checkAnswer("C") }
        btnOptionD.setOnClickListener { checkAnswer("D") }
    }

    private fun setupQuestions() {
        questionList = listOf(
            QuestionIps("Apa ibu kota Indonesia?", listOf("Jakarta", "Surabaya", "Medan", "Bandung"), "Jakarta"),
            QuestionIps("Pulau terbesar di Indonesia adalah?", listOf("Kalimantan", "Sumatra", "Papua", "Jawa"), "Kalimantan"),
            QuestionIps("Dimanakah letak Candi Borobudur?", listOf("Jawa Tengah", "Bali", "Jawa Timur", "Sumatra Utara"), "Jawa Tengah"),
            QuestionIps("Apa nama kerajaan Islam pertama di Indonesia?", listOf("Samudera Pasai", "Majapahit", "Sriwijaya", "Mataram"), "Samudera Pasai"),
            QuestionIps("Siapa presiden pertama Indonesia?", listOf("Soekarno", "Hatta", "Soeharto", "Habibie"), "Soekarno"),
            QuestionIps("Apa nama galaksi tempat kita berada?", listOf("Bimasakti", "Andromeda", "Orion", "Alpha Centauri"), "Bimasakti"),
            QuestionIps("Dimanakah letak Danau Toba?", listOf("Sumatra Utara", "Sumatra Barat", "Jawa Timur", "Bali"), "Sumatra Utara"),
            QuestionIps("Apa warna bendera Indonesia?", listOf("Merah Putih", "Merah Biru", "Putih Hijau", "Kuning Merah"), "Merah Putih"),
            QuestionIps("Apa nama gunung tertinggi di Indonesia?", listOf("Puncak Jaya", "Semeru", "Merapi", "Rinjani"), "Puncak Jaya"),
            QuestionIps("Apa nama sungai terpanjang di Indonesia?", listOf("Kapuas", "Musi", "Mahakam", "Barito"), "Kapuas")
        )
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@QuizIpsActivity, "Waktu Habis!", Toast.LENGTH_SHORT).show()
                goToScoreActivity()
            }
        }
        timer.start()
    }

    private fun setupQuestion() {
        if (currentQuestionIndex >= questionList.size) {
            goToScoreActivity()
            return
        }

        val question = questionList[currentQuestionIndex]
        val options = question.options.shuffled()

        tvQuestion.text = question.text
        tvQuestionCount.text = "Soal: ${currentQuestionIndex + 1}/${questionList.size}"

        btnOptionA.text = "A. ${options[0]}"
        btnOptionB.text = "B. ${options[1]}"
        btnOptionC.text = "C. ${options[2]}"
        btnOptionD.text = "D. ${options[3]}"
    }

    private fun checkAnswer(answer: String) {
        val selectedAnswer = when (answer) {
            "A" -> btnOptionA.text.toString().substring(3)
            "B" -> btnOptionB.text.toString().substring(3)
            "C" -> btnOptionC.text.toString().substring(3)
            "D" -> btnOptionD.text.toString().substring(3)
            else -> ""
        }

        val selectedButton = when (answer) {
            "A" -> btnOptionA
            "B" -> btnOptionB
            "C" -> btnOptionC
            "D" -> btnOptionD
            else -> null
        }

        if (selectedAnswer == questionList[currentQuestionIndex].correctAnswer) {
            correctCount++
            selectedButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.button_correct))
            Toast.makeText(this, "Benar!", Toast.LENGTH_SHORT).show()
        } else {
            wrongCount++
            selectedButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.button_wrong))
            Toast.makeText(this, "Salah!", Toast.LENGTH_SHORT).show()
        }

        selectedButton?.postDelayed({
            resetButtonColors()
            currentQuestionIndex++
            setupQuestion()
        }, 1000) // Tunda 1 detik
    }

    private fun resetButtonColors() {
        btnOptionA.setBackgroundColor(ContextCompat.getColor(this, R.color.button_default))
        btnOptionB.setBackgroundColor(ContextCompat.getColor(this, R.color.button_default))
        btnOptionC.setBackgroundColor(ContextCompat.getColor(this, R.color.button_default))
        btnOptionD.setBackgroundColor(ContextCompat.getColor(this, R.color.button_default))
    }

    private fun goToScoreActivity() {
        val intent = Intent(this, SkorIpsActivity::class.java)
        intent.putExtra("CORRECT", correctCount)
        intent.putExtra("WRONG", wrongCount)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }
}
