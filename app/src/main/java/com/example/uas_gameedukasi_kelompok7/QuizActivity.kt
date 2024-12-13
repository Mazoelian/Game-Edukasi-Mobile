package com.example.uas_gameedukasi_kelompok7

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvQuestionCount: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnOptionA: Button
    private lateinit var btnOptionB: Button
    private lateinit var btnOptionC: Button
    private lateinit var btnOptionD: Button
    private var correctAnswer: String = ""

    private var currentQuestionIndex = 0
    private lateinit var questionList: List<Question>
    private var correctCount = 0
    private var wrongCount = 0
    private var timerDuration: Long = 3 * 60 * 1000 // Default 3 menit
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Inisialisasi view
        tvQuestion = findViewById(R.id.tv_question)
        tvQuestionCount = findViewById(R.id.tv_question_count)
        tvTimer = findViewById(R.id.tv_timer)
        btnOptionA = findViewById(R.id.btn_option_a)
        btnOptionB = findViewById(R.id.btn_option_b)
        btnOptionC = findViewById(R.id.btn_option_c)
        btnOptionD = findViewById(R.id.btn_option_d)


        // Ambil data level dari intent
        val level = intent.getStringExtra("LEVEL")
        setupLevel(level)

        // Mulai timer
        startTimer()

        // Atur soal pertama
        setupQuestion()

        // Event listener untuk tombol pilihan
        btnOptionA.setOnClickListener { checkAnswer("A") }
        btnOptionB.setOnClickListener { checkAnswer("B") }
        btnOptionC.setOnClickListener { checkAnswer("C") }
        btnOptionD.setOnClickListener { checkAnswer("D") }
    }

    // Pilih soal berdasarkan level
    private fun setupLevel(level: String?) {
        val questionsEasy = listOf(
            Question("Apa ibu kota Indonesia?", listOf("Jakarta", "Surabaya", "Medan", "Bandung"), "Jakarta"),
            Question("2 + 2 = ?", listOf("4", "3", "5", "6"), "4"),
            Question("Apa warna langit saat cerah?", listOf("Biru", "Hijau", "Merah", "Kuning"), "Biru"),
            Question("Berapa jumlah hari dalam seminggu?", listOf("7", "5", "10", "6"), "7"),
            Question("Hewan apa yang memiliki belalai?", listOf("Gajah", "Harimau", "Kuda", "Rusa"), "Gajah"),
            Question("Apa ibu kota Jepang?", listOf("Tokyo", "Osaka", "Kyoto", "Nagasaki"), "Tokyo"),
            Question("7 x 7 = ?", listOf("49", "42", "36", "56"), "49"),
            Question("Apa lambang kimia untuk air?", listOf("H2O", "O2", "CO2", "HO"), "H2O"),
            Question("Apa hewan tercepat di darat?", listOf("Cheetah", "Kuda", "Kelinci", "Singa"), "Cheetah"),
            Question("Berapa bulan dalam setahun?", listOf("12", "10", "11", "13"), "12")
        )

        val questionsNormal = listOf(
            Question("Siapa presiden pertama Indonesia?", listOf("Soekarno", "Hatta", "Soeharto", "Habibie"), "Soekarno"),
            Question("7 x 8 = ?", listOf("56", "54", "48", "64"), "56"),
            Question("Berapa jumlah planet dalam tata surya?", listOf("8", "7", "9", "10"), "8"),
            Question("Apa nama galaksi tempat kita berada?", listOf("Bimasakti", "Andromeda", "Orion", "Alpha Centauri"), "Bimasakti"),
            Question("Siapa yang menulis teori relativitas?", listOf("Albert Einstein", "Isaac Newton", "Marie Curie", "Nikola Tesla"), "Albert Einstein"),
            Question("Apa warna bendera Indonesia?", listOf("Merah dan Putih", "Merah dan Biru", "Putih dan Hijau", "Kuning dan Merah"), "Merah dan Putih"),
            Question("Hewan apa yang hidup di air dan darat?", listOf("Amfibi", "Reptil", "Mamalia", "Burung"), "Amfibi"),
            Question("Siapa penemu bola lampu?", listOf("Thomas Edison", "Nikola Tesla", "James Watt", "Alexander Graham Bell"), "Thomas Edison"),
            Question("Berapa jumlah sisi dalam segitiga?", listOf("3", "4", "5", "6"), "3"),
            Question("Apa ibu kota Inggris?", listOf("London", "Paris", "Berlin", "Madrid"), "London")
        )

        val questionsHard = listOf(
            Question("Apa nama hukum aksi-reaksi?", listOf("Newton III", "Newton II", "Hukum Archimedes", "Hukum Ohm"), "Newton III"),
            Question("Berapa hasil 15 x 15?", listOf("225", "250", "215", "200"), "225"),
            Question("Siapa penemu radio?", listOf("Guglielmo Marconi", "Alexander Graham Bell", "James Watt", "Isaac Newton"), "Guglielmo Marconi"),
            Question("Apa lambang kimia dari emas?", listOf("Au", "Ag", "Fe", "Cu"), "Au"),
            Question("Apa nama gunung tertinggi di dunia?", listOf("Everest", "Kilimanjaro", "K2", "Elbrus"), "Everest"),
            Question("Apa ibu kota Australia?", listOf("Canberra", "Sydney", "Melbourne", "Perth"), "Canberra"),
            Question("Siapa yang menulis buku 'Origin of Species'?", listOf("Charles Darwin", "Gregor Mendel", "Isaac Newton", "Stephen Hawking"), "Charles Darwin"),
            Question("Berapa jumlah rusuk dalam kubus?", listOf("12", "8", "6", "10"), "12"),
            Question("Siapa penemu mesin cetak?", listOf("Johannes Gutenberg", "Alexander Graham Bell", "Isaac Newton", "Thomas Edison"), "Johannes Gutenberg"),
            Question("Apa nama planet terbesar dalam tata surya?", listOf("Jupiter", "Saturnus", "Bumi", "Mars"), "Jupiter")
        )

        questionList = when (level) {
            "mudah" -> questionsEasy
            "normal" -> questionsNormal
            "sulit" -> questionsHard
            else -> questionsEasy
        }

        timerDuration = when (level) {
            "mudah" -> 3 * 60 * 1000
            "normal" -> 2 * 60 * 1000
            "sulit" -> 1 * 60 * 1000
            else -> 3 * 60 * 1000
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@QuizActivity, "Waktu Habis!", Toast.LENGTH_SHORT).show()
                goToSkorActivity()
            }
        }
        timer.start()
    }

    private fun setupQuestion() {
        val currentQuestion = questionList[currentQuestionIndex]
        val shuffledOptions = currentQuestion.options.shuffled()

        tvQuestion.text = currentQuestion.text
        tvQuestionCount.text = "Soal: ${currentQuestionIndex + 1}/${questionList.size}"

        btnOptionA.text = "A. ${shuffledOptions[0]}"
        btnOptionB.text = "B. ${shuffledOptions[1]}"
        btnOptionC.text = "C. ${shuffledOptions[2]}"
        btnOptionD.text = "D. ${shuffledOptions[3]}"

        correctAnswer = currentQuestion.correctAnswer
    }

    private fun checkAnswer(answer: String) {
        val selectedAnswer = when (answer) {
            "A" -> btnOptionA.text.toString().substring(3)
            "B" -> btnOptionB.text.toString().substring(3)
            "C" -> btnOptionC.text.toString().substring(3)
            "D" -> btnOptionD.text.toString().substring(3)
            else -> ""
        }

        // Tentukan tombol yang dipilih
        val selectedButton = when (answer) {
            "A" -> btnOptionA
            "B" -> btnOptionB
            "C" -> btnOptionC
            "D" -> btnOptionD
            else -> null
        }

        // Ubah warna tombol berdasarkan jawaban
        if (selectedAnswer == correctAnswer) {
            correctCount++
            selectedButton?.setBackgroundColor(getColor(R.color.button_correct))
            Toast.makeText(this, "Benar!", Toast.LENGTH_SHORT).show()
        } else {
            wrongCount++
            selectedButton?.setBackgroundColor(getColor(R.color.button_wrong))
            Toast.makeText(this, "Salah!", Toast.LENGTH_SHORT).show()
        }

        // Tunda sebelum melanjutkan ke soal berikutnya
        selectedButton?.postDelayed({
            resetButtonColors()
            currentQuestionIndex++
            if (currentQuestionIndex < questionList.size) {
                setupQuestion()
            } else {
                goToSkorActivity()
            }
        }, 1000) // Tunda 1 detik
    }

    private fun resetButtonColors() {
        btnOptionA.setBackgroundColor(getColor(R.color.button_default))
        btnOptionB.setBackgroundColor(getColor(R.color.button_default))
        btnOptionC.setBackgroundColor(getColor(R.color.button_default))
        btnOptionD.setBackgroundColor(getColor(R.color.button_default))
    }



    private fun goToSkorActivity() {
        val intent = Intent(this, SkorActivity::class.java)
        intent.putExtra("CORRECT_ANSWERS", correctCount)
        intent.putExtra("WRONG_ANSWERS", wrongCount)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
