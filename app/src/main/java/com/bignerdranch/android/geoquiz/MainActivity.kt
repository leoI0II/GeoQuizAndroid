package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result: ActivityResult ->
        // handle the result
//        if (result.resultCode == Activity.RESULT_OK)
        quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        manageCheating()
    }

    private fun manageCheating(){
        quizViewModel.currentQuestion.cheatedQuestion = quizViewModel.isCheater
        if (quizViewModel.isCheater) {
            binding.cheatBtn.setText(R.string.already_cheated_btn)
            binding.cheatBtn.isEnabled = false
            quizViewModel.remainCheatCount -= 1
            binding.leftCheatCount.text = getString(R.string.cheats_available, quizViewModel.remainCheatCount)
        }
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

        binding.leftCheatCount.text = getString(R.string.cheats_available, quizViewModel.remainCheatCount)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            blurCheatButton()
//        }
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
            binding.cheatBtn.isEnabled = !quizViewModel.currentQuestion.answered && !quizViewModel.currentQuestion.cheatedQuestion && quizViewModel.remainCheatCount > 0
            binding.cheatBtn.setText(
                if (quizViewModel.currentQuestion.cheatedQuestion)
                    R.string.already_cheated_btn
                else if (quizViewModel.remainCheatCount == 0)
                    R.string.exceed_cheat_count
                else if (!quizViewModel.currentQuestion.answered && !quizViewModel.currentQuestion.cheatedQuestion)
                    R.string.cheat_btn
                else
                    R.string.cheat_btn
            )
            binding.leftCheatCount.text = getString(R.string.cheats_available, quizViewModel.remainCheatCount)
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
        val givenRightAnswer = answer == correctAnswer

        val messageId = when {
            givenRightAnswer ->
                if (quizViewModel.isCheater)
                    R.string.judgment_toast
                else
                    R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
        quizViewModel.currentQuestion.answered = givenRightAnswer
        if (quizViewModel.isCheater && givenRightAnswer) {
            quizViewModel.isCheater = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(10.0f, 10.0f, Shader.TileMode.CLAMP)
        binding.cheatBtn.setRenderEffect(effect)
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