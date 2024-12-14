package com.example.uas_gameedukasi_kelompok7

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

data class QuestionMtk(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

class QuizMtkActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvQuestionCount: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnOptionA: Button
    private lateinit var btnOptionB: Button
    private lateinit var btnOptionC: Button
    private lateinit var btnOptionD: Button
    private lateinit var questionList: List<QuestionMtk>

    private var currentQuestionIndex = 0
    private var correctCount = 0
    private var wrongCount = 0
    private var timerDuration: Long = 3 * 60 * 1000 // Default 3 minutes
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_mtk)

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
        setupQuestions(level)

        startTimer()
        setupQuestion()

        btnOptionA.setOnClickListener { checkAnswer("A") }
        btnOptionB.setOnClickListener { checkAnswer("B") }
        btnOptionC.setOnClickListener { checkAnswer("C") }
        btnOptionD.setOnClickListener { checkAnswer("D") }
    }

    private fun setupQuestions(level: String?) {
        questionList = when (level) {
            "mudah" -> {
                timerDuration = 2 * 60 * 1000 // 2 menit untuk level mudah
                generateLevel1Questions()
            }
            "normal" -> {
                timerDuration = 1 * 60 * 1000 // 1 menit untuk level normal
                generateLevel2Questions()
            }
            "sulit" -> {
                timerDuration = 45 * 1000 // 45 detik untuk level sulit
                generateLevel3Questions()
            }
            else -> {
                timerDuration = 2 * 60 * 1000 // Default ke level mudah jika level tidak ditemukan
                generateLevel1Questions()
            }
        }
    }

    private fun generateLevel1Questions(): List<QuestionMtk> {
        return listOf(
            QuestionMtk("5 + 3 = ?", listOf("6", "7", "8", "9"), "8"),
            QuestionMtk("2 + 4 = ?", listOf("5", "6", "7", "8"), "6"),
            QuestionMtk("9 - 4 = ?", listOf("3", "4", "5", "6"), "5"),
            QuestionMtk("6 + 2 = ?", listOf("7", "8", "9", "10"), "8"),
            QuestionMtk("3 + 3 = ?", listOf("5", "6", "7", "8"), "6"),
            QuestionMtk("7 - 2 = ?", listOf("4", "5", "6", "7"), "5"),
            QuestionMtk("8 - 4 = ?", listOf("3", "4", "5", "6"), "4"),
            QuestionMtk("3 x 2 = ?", listOf("5", "6", "7", "8"), "6"),
            QuestionMtk("4 x 2 = ?", listOf("7", "8", "9", "10"), "8"),
            QuestionMtk("5 x 3 = ?", listOf("12", "13", "14", "15"), "15")
        )
    }

    private fun generateLevel2Questions(): List<QuestionMtk> {
        return listOf(
            QuestionMtk("12 + 8 = ?", listOf("18", "19", "20", "21"), "20"),
            QuestionMtk("15 - 7 = ?", listOf("7", "8", "9", "10"), "8"),
            QuestionMtk("5 x 6 = ?", listOf("25", "30", "35", "40"), "30"),
            QuestionMtk("18 ÷ 2 = ?", listOf("6", "7", "8", "9"), "9"),
            QuestionMtk("20 - 4 = ?", listOf("14", "15", "16", "17"), "16"),
            QuestionMtk("10 + 15 = ?", listOf("24", "25", "26", "27"), "25"),
            QuestionMtk("7 x 4 = ?", listOf("28", "29", "30", "31"), "28"),
            QuestionMtk("16 ÷ 4 = ?", listOf("3", "4", "5", "6"), "4"),
            QuestionMtk("9 + 12 = ?", listOf("20", "21", "22", "23"), "21"),
            QuestionMtk("14 x 2 = ?", listOf("27", "28", "29", "30"), "28")
        )
    }

    private fun generateLevel3Questions(): List<QuestionMtk> {
        return listOf(
            QuestionMtk("24 ÷ 6 = ?", listOf("3", "4", "5", "6"), "4"),
            QuestionMtk("18 + 13 = ?", listOf("30", "31", "32", "33"), "31"),
            QuestionMtk("15 x 3 = ?", listOf("44", "45", "46", "47"), "45"),
            QuestionMtk("27 ÷ 3 = ?", listOf("8", "9", "10", "11"), "9"),
            QuestionMtk("11 + 15 = ?", listOf("25", "26", "27", "28"), "26"),
            QuestionMtk("36 ÷ 9 = ?", listOf("3", "4", "5", "6"), "4"),
            QuestionMtk("14 x 5 = ?", listOf("65", "70", "75", "80"), "70"),
            QuestionMtk("20 + 28 = ?", listOf("48", "49", "50", "51"), "48"),
            QuestionMtk("32 ÷ 4 = ?", listOf("7", "8", "9", "10"), "8"),
            QuestionMtk("16 x 3 = ?", listOf("45", "46", "47", "48"), "48")
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
                Toast.makeText(this@QuizMtkActivity, "Waktu Habis!", Toast.LENGTH_SHORT).show()
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
        val intent = Intent(this, SkorMtkActivity::class.java)
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
