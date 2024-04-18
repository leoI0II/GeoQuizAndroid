package com.bignerdranch.android.geoquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private var TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private lateinit var trueBtn : Button
//    private lateinit var falseBtn : Button
//    private lateinit var nextBtn : Button
//    private lateinit var questionText : TextView

    private val givenAnswers : Answers = Answers()

    private val quizViewModel : QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        // handle the result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.app_name)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

//        trueBtn = findViewById(R.id.true_btn)
//        falseBtn = findViewById(R.id.false_btn)
//        nextBtn = findViewById(R.id.next_btn)
//        questionText = findViewById(R.id.question_text_view)

        binding.trueBtn.setOnClickListener { view : View ->
            // Do something in response to the click here
            checkAnswer(true)

            if (quizViewModel.currentQuestionAnswer)
                binding.nextBtn.callOnClick()

            // challenge 1
//            Snackbar.make(this, view, getString(R.string.correct_toast), Snackbar.LENGTH_SHORT).show()
        }

        binding.falseBtn.setOnClickListener { view : View ->
            // Do something in response to the click here
            checkAnswer(false)

            if (!quizViewModel.currentQuestionAnswer)
                binding.nextBtn.callOnClick()

            // challenge 1
//            Snackbar.make(this, view, getString(R.string.incorrect_toast), Snackbar.LENGTH_SHORT).show()
        }

        binding.nextBtn.setOnClickListener { view : View ->
            quizViewModel.moveToNext()
            updateQuestionTextView()
        }

        binding.questionTextView.setOnClickListener { view : View ->
            quizViewModel.moveToNext()
            updateQuestionTextView()
        }

        binding.prevBtn.setOnClickListener { view : View ->
            quizViewModel.moveToPrev()
            updateQuestionTextView()
        }

        binding.cheatBtn.setOnClickListener { view: View ->
            // START CHEAT ACTIVITY
            val intent = CheatActivity.newIntent(this@MainActivity, quizViewModel.currentQuestionAnswer)
//            startActivity(intent)
            cheatLauncher.launch(intent)
        }

        updateQuestionTextView()
    }

    private fun enableAllBtns(enable : Boolean) {
        binding.falseBtn.isEnabled = enable
        binding.trueBtn.isEnabled = enable
        binding.nextBtn.isEnabled = enable
        binding.prevBtn.isEnabled = enable
        binding.questionTextView.isEnabled = enable
    }

    private fun updateQuestionTextView() {
        if (checkAllQuestionHaveBeenAnswered()) {
            val questionsCount = quizViewModel.questionBankSize
            val percentageCorrect : Float = givenAnswers.correct / questionsCount.toFloat() * 100F
            val statString = getString(R.string.stat, percentageCorrect)

            Toast.makeText(this, statString, Toast.LENGTH_LONG).show()

            enableAllBtns(false)
        }
        else {
            binding.questionTextView.setText(quizViewModel.currentQuestionText)

            binding.falseBtn.isEnabled = !quizViewModel.currentQuestion.answered
            binding.trueBtn.isEnabled = !quizViewModel.currentQuestion.answered
        }
    }

    private fun checkAllQuestionHaveBeenAnswered() : Boolean {
//        return questionsBank.find { question: Question -> question.answered }?.let { true } ?: false
        val ret: Boolean = true
        for (question in quizViewModel.questionsBank) {
            if (!question.answered)
                return false
        }
        return ret
    }

    private fun checkAnswer(answer : Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (answer == correctAnswer) {
            R.string.correct_toast.also { givenAnswers.correct++ }
        }
        else{
            R.string.incorrect_toast.also { givenAnswers.incorrect++ }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        quizViewModel.currentQuestion.answered = true
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

}