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
            Question("Apa warna daun pada tanaman?", listOf("Hijau", "Merah", "Biru", "Kuning"), "Hijau"),
            Question("Hewan apa yang bisa terbang?", listOf("Burung", "Kucing", "Anjing", "Ikan"), "Burung"),
            Question("Apa yang diperlukan tanaman untuk tumbuh?", listOf("Air", "Batu", "Logam", "Kaca"), "Air"),
            Question("Berapa banyak jari yang dimiliki manusia?", listOf("10", "5", "12", "6"), "10"),
            Question("Hewan apa yang hidup di air?", listOf("Ikan", "Ayam", "Sapi", "Kuda"), "Ikan"),
            Question("Apa yang dihasilkan oleh tanaman untuk oksigen?", listOf("Daun", "Bunga", "Akar", "Batang"), "Daun"),
            Question("Apa itu pelangi?", listOf("Cahaya yang terpecah menjadi warna-warna", "Bunga", "Hujan es", "Tanah basah"), "Cahaya yang terpecah menjadi warna-warna"),
            Question("Berapa banyak planet di tata surya kita?", listOf("8", "5", "7", "9"), "8"),
            Question("Apa yang dibutuhkan manusia untuk bernapas?", listOf("Oksigen", "Karbon dioksida", "Air", "Gula"), "Oksigen"),
            Question("Hewan apa yang bisa melompat tinggi?", listOf("Kanguru", "Kuda", "Singa", "Kucing"), "Kanguru")
        )

        val questionsNormal = listOf(
            Question("Hewan apa yang memiliki sayap?", listOf("Burung", "Gajah", "Kuda", "Pinguin"), "Burung"),
            Question("Apa yang digunakan untuk melihat benda jauh?", listOf("Teleskop", "Kamera", "Lensa", "Mikroskop"), "Teleskop"),
            Question("Apa yang mengalir di sungai?", listOf("Air", "Minyak", "Uap", "Gas"), "Air"),
            Question("Tanaman yang menghasilkan buah apel, termasuk jenis apa?", listOf("Pohon buah", "Tanaman hias", "Tanaman obat", "Tanaman air"), "Pohon buah"),
            Question("Apa yang terjadi jika air dipanaskan?", listOf("Air mendidih", "Air membeku", "Air berubah warna", "Air menguap"), "Air mendidih"),
            Question("Planet mana yang dikenal sebagai planet merah?", listOf("Mars", "Jupiter", "Venus", "Merkurius"), "Mars"),
            Question("Apa yang dimakan oleh sapi?", listOf("Rumput", "Daging", "Buah", "Ikan"), "Rumput"),
            Question("Benda apa yang mengapung di air?", listOf("Kayu", "Batu", "Kaca", "Besi"), "Kayu"),
            Question("Apa yang dilakukan oleh bunga?", listOf("Menghasilkan madu", "Menarik petir", "Menghasilkan oksigen", "Menyerap air"), "Menghasilkan madu"),
            Question("Bagaimana tanaman mendapatkan air?", listOf("Melalui akar", "Melalui daun", "Melalui bunga", "Melalui batang"), "Melalui akar")
        )

        val questionsHard = listOf(
            Question("Siapa penemu listrik?", listOf("Thomas Edison", "Nikola Tesla", "Albert Einstein", "Isaac Newton"), "Thomas Edison"),
            Question("Apa nama bintang yang menjadi pusat tata surya?", listOf("Matahari", "Bulan", "Mars", "Jupiter"), "Matahari"),
            Question("Di mana kita bisa menemukan kutub utara?", listOf("Di atas bumi", "Di bawah bumi", "Di tengah bumi", "Di sebelah timur bumi"), "Di atas bumi"),
            Question("Apa yang terjadi saat air membeku?", listOf("Menjadi es", "Menjadi gas", "Menjadi awan", "Menjadi batu"), "Menjadi es"),
            Question("Apa yang menyebabkan bumi berputar?", listOf("Gravitasi", "Angin", "Kekuatan manusia", "Panas matahari"), "Gravitasi"),
            Question("Apakah benda yang paling keras di bumi?", listOf("Diamond", "Besi", "Emas", "Tembaga"), "Diamond"),
            Question("Apa yang terjadi pada bumi jika terlalu banyak polusi?", listOf("Panas", "Hujan terus-menerus", "Banjir", "Tidak ada perubahan"), "Panas"),
            Question("Apa yang digunakan manusia untuk menghitung waktu?", listOf("Jam", "Kompas", "Peta", "Buku"), "Jam"),
            Question("Berapa jumlah gigi dewasa pada manusia?", listOf("32", "28", "30", "34"), "32"),
            Question("Apa yang menyebabkan pelapukan batuan?", listOf("Air dan angin", "Batu keras", "Api", "Petir"), "Air dan angin")
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
